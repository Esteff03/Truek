package fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.sbjs.truek.R;

public class FragmentProfile extends Fragment {
    // Declarar las variables
    private TextInputEditText adressEditText;
    private TextInputEditText nameEditText;
    private TextInputEditText tlEditText;
    private TextView fullname, payment, purchaseCount;
    String name, adress, tl;



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
        adressEditText = view.findViewById(R.id.adress);
        nameEditText = view.findViewById(R.id.name);
        tlEditText = view.findViewById(R.id.tl);
        fullname = view.findViewById(R.id.full_name);
        payment = view.findViewById(R.id.payment);
        purchaseCount = view.findViewById(R.id.exchange);  // Añadir TextView para mostrar el contador de compras
        // Verificar si el saldo ya ha sido guardado, si no, establecerlo a 20 euros
        float currentBalance = getWalletBalance(getContext());
        if (currentBalance == 0.0f) {
            setInitialBalance(getContext(), 20.0f);  // Establece el saldo inicial de 20 euros
            currentBalance = 20.0f;  // Actualiza el saldo local
        }
        payment.setText("Saldo: €" + currentBalance);  // Muestra el saldo actual


        // Obtener y mostrar el contador de compras
        int currentPurchaseCount = getPurchaseCount(getContext());
        purchaseCount.setText("Compras realizadas: " + currentPurchaseCount);



        // Botón para modificar la información
        Button modifyButton = view.findViewById(R.id.btnSave);  // Asegúrate de usar el ID correcto
        modifyButton.setOnClickListener(v -> {
            // Obtener el texto de los campos antes de validar
            name = nameEditText.getText().toString().trim();
            adress = adressEditText.getText().toString().trim();
            tl = tlEditText.getText().toString().trim();

            // Validar si los campos no están vacíos antes de actualizar
            if (!name.isEmpty()) {
                fullname.setText(name);
            }
            if (!adress.isEmpty()) {
                adressEditText.setText(adress);
            }
            if (!tl.isEmpty()) {
                tlEditText.setText(tl);
            }

            // Mensaje para confirmar que la información fue modificada
            Toast.makeText(getActivity(), "Información actualizada.", Toast.LENGTH_SHORT).show();

            // Establece el ítem seleccionado en el BottomNavigationView
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav_bar);
            bottomNavigationView.setSelectedItemId(R.id.profile);
        });

        return view;
    }


    // Función para obtener el saldo del monedero
    public float getWalletBalance(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        return sharedPreferences.getFloat("wallet_balance", 0.0f); // Retorna 0.0 si no se encuentra el saldo
    }

    // Función para establecer el saldo inicial de 20 euros en SharedPreferences
    public void setInitialBalance(Context context, float balance) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("wallet_balance", balance);
        editor.apply();  // Aplica el cambio
    }
    // Función para obtener el contador de compras
    public int getPurchaseCount(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("purchase_count", 0); // Retorna 0 si no se encuentra el contador
    }

    // Función para actualizar el contador de compras
    public void incrementPurchaseCount(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int currentCount = getPurchaseCount(context);
        editor.putInt("purchase_count", currentCount + 1);  // Incrementa el contador
        editor.apply();  // Aplica el cambio

        // Actualizar el contador en el perfil (recargar la vista)
        updatePurchaseCountDisplay(context);
    }

    // Función para actualizar el contador de compras en la vista
    private void updatePurchaseCountDisplay(Context context) {
        int currentPurchaseCount = getPurchaseCount(context);
        if (purchaseCount != null) {
            purchaseCount.setText("Compras realizadas: " + currentPurchaseCount);  // Mostrar el contador actualizado
        }
    }
}
