package fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
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
import com.truek.MainActivity;
import com.truek.MainView;
import com.truek.R;

public class Fragment_Login extends Fragment {

    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button loginButton = view.findViewById(R.id.button1);
        Button cancelButton = view.findViewById(R.id.button2);
        emailEditText = view.findViewById(R.id.email);
        passwordEditText = view.findViewById(R.id.password);

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(v -> {
            if (validateInputs()) {
                loginUser();
            }
        });

        cancelButton.setOnClickListener(v -> animateAndOpenSignup(view));

        return view;
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Toast.makeText(getContext(), "¡Inicio de sesión exitoso!", Toast.LENGTH_SHORT).show();
                            openMainActivity();
                        }
                    } else {
                        showFirebaseError(task.getException().getMessage());
                    }
                });
    }

    private void showFirebaseError(String errorMessage) {
        if (errorMessage != null && errorMessage.contains("password")) {
            Toast.makeText(getContext(), "Contraseña incorrecta", Toast.LENGTH_LONG).show();
        } else if (errorMessage != null && errorMessage.contains("no user record")) {
            Toast.makeText(getContext(), "El correo no está registrado", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Error de autenticación", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validateInputs() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(getContext(), "El correo es obligatorio", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.isEmpty()) {
            Toast.makeText(getContext(), "La contraseña es obligatoria", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void openMainActivity() {
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.anim_rigth_left, android.R.anim.fade_in);
    }

    private void animateAndOpenSignup(View view) {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_left_rigth);
        animation.setDuration(500);
        view.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                openSignup();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    private void openSignup() {
        Intent intent = new Intent(requireActivity(), MainView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.anim_rigth_left, android.R.anim.fade_in);
    }
}
