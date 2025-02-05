package fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.truek.R;

public class FragmentProfile extends Fragment {
    // Declarar las variables
    private TextInputEditText emailEditText;
    private TextInputEditText nameEditText;
    private TextInputEditText tlEditText;
    private TextInputEditText pwEditText;
    private TextView fullname, payment, exchange;
    String name, email, tl, pw;



    public FragmentProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Aquí puedes agregar código de inicialización si es necesario.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el diseño para este fragmento
        View view = inflater.inflate(R.layout.fragment_profile, container, false);



        // Inicializar las vistas
        emailEditText = view.findViewById(R.id.email);
        nameEditText = view.findViewById(R.id.name);
        tlEditText = view.findViewById(R.id.tl);
        pwEditText = view.findViewById(R.id.pw);
        fullname = view.findViewById(R.id.full_name);
        payment = view.findViewById(R.id.payment);
        exchange = view.findViewById(R.id.exchange);



        // Botón para modificar la información
        Button modifyButton = view.findViewById(R.id.btnSave);  // Asegúrate de usar el ID correcto
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
            Toast.makeText(getActivity(), "Información actualizada.", Toast.LENGTH_SHORT).show();

            // Establece el ítem seleccionado en el BottomNavigationView
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav_bar);
            bottomNavigationView.setSelectedItemId(R.id.profile);
        });

        /* // Botón para modificar el monedero
        Button modifyComprar = view.findViewById(R.id.btnComprar);  // Botón de compra
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
            Toast.makeText(getActivity(), "Compra realizada", Toast.LENGTH_SHORT).show();
        }); */

        return view;
    }
}
