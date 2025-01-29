package com.truek;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class Profile extends AppCompatActivity {
    // Declarar las variables
    private TextInputEditText emailEditText;
    private TextInputEditText nameEditText;
    private TextInputEditText tlEditText;
    private TextInputEditText pwEditText;
    private TextView fullname, payment, exchange;
    String name, email, tl, pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Inicializar los campos de texto
        nameEditText = findViewById(R.id.name);
        emailEditText = findViewById(R.id.email);
        tlEditText = findViewById(R.id.tl);
        pwEditText = findViewById(R.id.pw);

        fullname = findViewById(R.id.full_name);

        // Inicializar los TextView de monedero
        payment = findViewById(R.id.payment);
        exchange = findViewById(R.id.exchange);



        // Botón para actualizar y mostrar datos
        Button showEmailButton = findViewById(R.id.btnUpdate);
        showEmailButton.setOnClickListener(v -> {
            // Recoge el texto ingresado
            name = nameEditText.getText().toString();
            email = emailEditText.getText().toString();
            tl = tlEditText.getText().toString();
            pw = pwEditText.getText().toString();

            // Muestra la info en un Toast (puedes mostrar más información si deseas)
            Toast.makeText(Profile.this, "Correo: " + email + "\nNombre: " + name, Toast.LENGTH_SHORT).show();
        });

        // Botón para modificar la información
        Button modifyButton = findViewById(R.id.btnSave);  // Asegúrate de usar el ID correcto
        modifyButton.setOnClickListener(v -> {
            // Validar si los campos no están vacíos antes de actualizar
            if (!name.isEmpty()) {
                fullname.setText(name);
            }
            if (!email.isEmpty()) {
                emailEditText.setText(email);
            }
            if (!tl.isEmpty()) {
                tlEditText.setText(tl);
            }
            if (!pw.isEmpty()) {
                pwEditText.setText(pw);
            }

            // Mensaje para confirmar que la información fue modificada
            Toast.makeText(Profile.this, "Información actualizada.", Toast.LENGTH_SHORT).show();
        });



       /* // Botón para modificar el monedero
        Button modifyComprar = findViewById(R.id.btnComprar);  // Botón de compra
        modifyComprar.setOnClickListener(v -> {
            int contador = Integer.parseInt(exchange.getText().toString());
            int valor = 7; // Valor del producto que queremos comprar
            final int AUMENTO_MONEDERO = 15; // Cada vez que haga un intercambio, aumenta 15 euros

            // Actualiza el valor del monedero después de la compra
            int saldoActual = Integer.parseInt(payment.getText().toString());
            payment.setText(String.valueOf(saldoActual - valor)); // Resta el valor del producto del monedero
            contador++;

            if (contador > 0) {
                // Actualiza los intercambios realizados y aumenta el monedero
                exchange.setText(String.valueOf(contador));
                payment.setText(String.valueOf(Integer.parseInt(payment.getText().toString()) + AUMENTO_MONEDERO));
            } else {
                // Si no hay intercambios, muestra 0
                exchange.setText("0");
            }

            // Muestra un Toast
            Toast.makeText(Profile.this, "Compra realizada", Toast.LENGTH_SHORT).show();
        });*/
    }
}
