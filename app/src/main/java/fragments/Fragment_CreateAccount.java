package fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.truek.FirebaseTokenManager;
import com.truek.MainActivity;
import com.truek.MainView;
import com.truek.R;

public class Fragment_CreateAccount extends Fragment {

    public static String firebaseToken;
    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_account, container, false);

        Button nextButton = view.findViewById(R.id.buttoncrear);
        Button cancelButton = view.findViewById(R.id.button2);
        emailEditText = view.findViewById(R.id.email);
        passwordEditText = view.findViewById(R.id.password);

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        nextButton.setOnClickListener(v -> {
            if (validateInputs()) {
                registerUser();
            }
        });

        cancelButton.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .commit();
            openSignup();
        });

        return view;
    }

    // Método para registrar un nuevo usuario en Firebase
    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        if (user != null) {
                            // Obtener el token de Firebase
                            user.getIdToken(true).addOnCompleteListener(tokenTask -> {
                                if (tokenTask.isSuccessful()) {
                                    firebaseToken = tokenTask.getResult().getToken();
                                    // Guardar el token en el Singleton
                                    FirebaseTokenManager.getInstance().setFirebaseToken(firebaseToken);
                                } else {
                                    Log.e("FirebaseAuthHelper", "Error obteniendo token JWT", tokenTask.getException());
                                    Toast.makeText(getContext(), "Error al obtener el token", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
    }

    // Método para validar los datos de entrada
    private boolean validateInputs() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(getContext(), "El correo electrónico es requerido", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getContext(), "Por favor, introduce un correo electrónico válido", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.isEmpty()) {
            Toast.makeText(getContext(), "La contraseña es requerida", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 6) {
            Toast.makeText(getContext(), "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Método para abrir la actividad principal después de registro exitoso
    private void openMain() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.anim_rigth_left, android.R.anim.fade_in);
    }

    // Método para abrir la vista de inicio de sesión
    private void openSignup() {
        Intent intent = new Intent(getActivity(), MainView.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.anim_rigth_left, android.R.anim.fade_in);
    }


}
