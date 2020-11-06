package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    private RecyclerView recyclerViewMessages;
    private MessageAdapter adapter;

    private EditText editTextMessage;
    private ImageView imageViewSendMessage;

    private String author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ссылка на базу
        db = FirebaseFirestore.getInstance();
        // ссылка на сообщеие . которое будем отпралять
        editTextMessage = findViewById(R.id.editTextMessage);
        // ссылка на картинку, при нажатии на которой  соощение будет отправляться
        imageViewSendMessage = findViewById(R.id.imageViewSendMessage);
        adapter = new MessageAdapter();

        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMessages.setAdapter(adapter);

        author = "Сергей";
        // слушатель на нажатие на картинку
        imageViewSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        // создаем слушателя , который при изменении данных на сервере , будет автоматически их загружать
        db.collection("messages").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                    List<Message> messages = queryDocumentSnapshots.toObjects(Message.class);
                    adapter.setMessages(messages);
              
            }
        });
    }

    // отправляем сообщение
    private void sendMessage() {
        String textOfMessage = editTextMessage.getText().toString().trim();

        if (!textOfMessage.isEmpty()) {
            recyclerViewMessages.scrollToPosition(adapter.getItemCount() - 1);

            db.collection("messages").add(new Message(author, textOfMessage)).
                    // слушатель, если данные добавелись  на сервер
                            addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                           // очистка
                            editTextMessage.setText("");
                        }
                    }).
                    // слушатель , если не добавилась запись на сервер
                            addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Сообщение не отправлено", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}