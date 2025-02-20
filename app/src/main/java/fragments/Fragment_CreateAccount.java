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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Fragment_CreateAccount extends Fragment {

    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;
    private static final String SUPABASE_API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InBnb3NhZnlkbHdza3d0dm51b2trIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTczOTQzODIxNywiZXhwIjoyMDU1MDE0MjE3fQ.2kKFUfVEo9pXvHa4GfdRR20KoFKJEUa55r21AF_K1bE";
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
                        // Registro exitoso
                        FirebaseUser user = task.getResult().getUser();
                        if (user != null) {
                            String uid = user.getUid();
                            String emailS = user.getEmail();

                            // Obtener el token de ID de Firebase (forzar la actualización del token con true)
                            user.getIdToken(true).addOnCompleteListener(tokenTask -> {
                                if (tokenTask.isSuccessful()) {
                                    // El token JWT de Firebase
                                    String firebaseToken = tokenTask.getResult().getToken();

                                    // Aquí puedes utilizar este token para autenticar la sesión en Supabase
                                    //SupabaseHelper.saveUserInSupabase(getContext(), firebaseToken);

                                    // Ahora guardamos este usuario en la base de datos de Supabase
                                    //saveUserToSupabase(uid, emailS);

                                    // Navegar a la pantalla principal
                                    Toast.makeText(getContext(), "Account ready!", Toast.LENGTH_SHORT).show();
                                    openMain();
                                } else {
                                    Log.e("Fragment_CreateAccount", "Error obteniendo token JWT", tokenTask.getException());
                                }
                            });
                        }
                    } else {
                        // Manejo de errores en el registro
                        Exception exception = task.getException();
                        if (exception instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(getContext(), "Cuenta existente", Toast.LENGTH_LONG).show();
                        } else {
                            String errorMessage = exception.getMessage();
                            Toast.makeText(getContext(), "Fallo de registro: " + errorMessage, Toast.LENGTH_LONG).show();
                            Log.e("Fragment_CreateAccount", "Error de registro", exception);
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

   /* private void saveUserToSupabase(String uid, String email) {
        // URL de la API de Supabase para insertar el usuario en la base de datos
        String url = "https://your-supabase-project.supabase.co/rest/v1/users";  // Asegúrate de que la tabla se llame "users"

        // Crear un JSON con los datos del usuario
        JSONObject userObject = new JSONObject();
        try {
            userObject.put("uid", uid);  // Firebase UID
            userObject.put("email", email);  // Correo electrónico
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Crear la solicitud POST usando Volley
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, userObject,
                response -> {
                    // Si la solicitud fue exitosa, puedes procesar la respuesta
                    Log.d("Supabase", "Usuario guardado exitosamente: " + response.toString());
                },
                error -> {
                    // Si la solicitud falla, muestra un mensaje de error
                    Log.e("Supabase", "Error al guardar usuario en Supabase: " + error.getMessage());
                    Toast.makeText(getContext(), "Error al guardar el usuario en Supabase", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                // Los encabezados necesarios para autenticar la solicitud
                Map<String, String> headers = new HashMap<>();
                // Agregar la clave de API de tu proyecto en Supabase
                headers.put("Authorization", "Bearer"+ SUPABASE_API_KEY);  // Sustituye con tu API Key
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Enviar la solicitud
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjectRequest);*/
}