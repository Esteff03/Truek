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

import java.util.ArrayList;
import java.util.List;

public class Chat extends AppCompatActivity {


    private RecyclerView recyclerView;
    private EditText messageInput;
    private Button sendButton;
    private ChatAdapter chatAdapter;
    private List<String> messages;

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

        // Configura el botón de enviar
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageInput.getText().toString().trim();

                if (!message.isEmpty()) {
                    // Agrega el nuevo mensaje a la lista
                    messages.add(message);

                    // Notifica al adaptador que los datos han cambiado
                    chatAdapter.notifyItemInserted(messages.size() - 1);

                    // Desplaza el RecyclerView hacia el último mensaje
                    recyclerView.scrollToPosition(messages.size() - 1);

                    // Limpia el campo de texto
                    messageInput.setText("");
                }
            }
        });
    }
}