package com.truek;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Chat extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText messageInput;
    private Button sendButton;
    private ChatAdapter chatAdapter;
    private List<Message> messages;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Inicializa la vista
        recyclerView = findViewById(R.id.recyclerView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        // Inicializa la lista de mensajes
        messages = new ArrayList<>();

        // Establece el LayoutManager para el RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Crea el adaptador y lo asigna al RecyclerView
        chatAdapter = new ChatAdapter(messages);
        recyclerView.setAdapter(chatAdapter);

        // Obtén la referencia a la base de datos de Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("messages");

        // Configura el botón de enviar
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageInput.getText().toString().trim();

                if (!messageText.isEmpty()) {
                    // Crea un nuevo mensaje
                    Message newMessage = new Message(messageText, "User1");  // Cambiar "User1" por el nombre del usuario si usas autenticación

                    // Guarda el mensaje en Firebase
                    databaseReference.push().setValue(newMessage);

                    // Agrega el mensaje a la lista y actualiza el RecyclerView
                    messages.add(newMessage);
                    chatAdapter.notifyItemInserted(messages.size() - 1);

                    // Desplaza el RecyclerView hacia el último mensaje
                    recyclerView.scrollToPosition(messages.size() - 1);

                    // Limpia el campo de texto
                    messageInput.setText("");
                }
            }
        });

        // Escuchar los mensajes en tiempo real
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                // Obtén el mensaje desde Firebase
                Message message = dataSnapshot.getValue(Message.class);
                if (message != null) {
                    messages.add(message);
                    chatAdapter.notifyItemInserted(messages.size() - 1);

                    // Desplaza hacia el último mensaje
                    recyclerView.scrollToPosition(messages.size() - 1);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                // Implementa si necesitas manejar cambios
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // Implementa si necesitas manejar eliminaciones
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                // Implementa si necesitas manejar movimientos
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Maneja el error
            }
        });
    }
}