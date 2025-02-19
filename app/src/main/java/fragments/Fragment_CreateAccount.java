package fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.truek.MainActivity;
import com.truek.MainView;
import com.truek.R;

public class Fragment_CreateAccount extends Fragment {

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

    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            if (user != null) {
                                Toast.makeText(getContext(), "Account ready!", Toast.LENGTH_SHORT).show();
                                openMain();
                            }
                        }
                    } else {
                        Exception exception = task.getException();
                        if (exception instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(getContext(), "Cuenta existente", Toast.LENGTH_LONG).show();
                        } else {
                            String errorMessage = exception.getMessage();
                            Toast.makeText(getContext(), "Fallo de registro: " + errorMessage, Toast.LENGTH_LONG).show();
                            Log.e("Fragment_CreateAccount", "Error de registro", exception); // Log para depuración
                        }
                    }
                });
    }


    private boolean validateInputs() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(getContext(), "Email is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.isEmpty()) {
            Toast.makeText(getContext(), "Password is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 6) {
            Toast.makeText(getContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void openMain() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.anim_rigth_left, android.R.anim.fade_in);
    }

    private void openSignup() {
        Intent intent = new Intent(getActivity(), MainView.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.anim_rigth_left, android.R.anim.fade_in);
    }
}
