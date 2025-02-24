package fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.truek.MainActivity;
import com.truek.MainView;
import com.truek.R;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;

public class Fragment_Login extends Fragment {
    // URL completa para el login en Supabase
    private static final String SUPABASE_URL = "https://pgosafydlwskwtvnuokk.supabase.co/auth/v1/token?grant_type=password";
    // Tu clave pública real
    private static final String SUPABASE_API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InBnb3NhZnlkbHdza3d0dm51b2trIiwicm9sZSI6ImFub24iLCJpYXQiOjE3Mzk0MzgyMTcsImV4cCI6MjA1NTAxNDIxN30.EmB_NLAqXji3UhtgaQsc4VmGrtnUHQlNiAb6Oau3fQo";

    private EditText emailEditText, passwordEditText;
    private final OkHttpClient client = new OkHttpClient();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button loginButton = view.findViewById(R.id.button1);
        Button cancelButton = view.findViewById(R.id.button2);
        emailEditText = view.findViewById(R.id.email);
        passwordEditText = view.findViewById(R.id.password);

        loginButton.setOnClickListener(v -> {
            if (validateInputs()) {
                if (isConnectedToInternet()) {
                    loginUser();
                } else {
                    Toast.makeText(getContext(), "No hay conexión a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton.setOnClickListener(v -> openSignup());

        return view;
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        new Thread(() -> {
            try {
                // Construimos el JSON con las credenciales
                JSONObject json = new JSONObject();
                json.put("email", email);
                json.put("password", password);

                Log.d("Fragment_Login", "JSON: " + json.toString());

                RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));
                Request request = new Request.Builder()
                        .url(SUPABASE_URL)
                        .header("apikey", SUPABASE_API_KEY)
                        .header("Content-Type", "application/json")
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();
                String responseBody = response.body() != null ? response.body().string() : "";

                if (response.isSuccessful()) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "¡Inicio de sesión exitoso!", Toast.LENGTH_SHORT).show();
                        openMainActivity();
                    });
                } else {
                    Log.e("Fragment_Login", "Error response: " + responseBody);
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Error al iniciar sesión: " + responseBody, Toast.LENGTH_LONG).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Error de conexión: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    private boolean validateInputs() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(getContext(), "El correo es obligatorio", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getContext(), "Correo inválido", Toast.LENGTH_SHORT).show();
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
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.anim_rigth_left, android.R.anim.fade_in);
    }

    private void openSignup() {
        Intent intent = new Intent(requireActivity(), MainView.class);
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.anim_rigth_left, android.R.anim.fade_in);
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager cm = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
