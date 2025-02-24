package fragments;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.truek.MainActivity;
import com.truek.MainView;
import com.truek.R;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;
import java.io.IOException;

public class Fragment_Login extends Fragment {
    private static final String SUPABASE_URL = "https://pgosafydlwskwtvnuokk.supabase.co";
    private static final String SUPABASE_API_KEY = "TU_SUPABASE_API_KEY"; // ⚠️ Usa una variable segura en producción

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

        cancelButton.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction().commit();
            openSignup();
        });

        return view;
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        new Thread(() -> {
            try {
                JSONObject json = new JSONObject();
                json.put("email", email);
                json.put("password", password);

                RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));
                Request request = new Request.Builder()
                        .url(SUPABASE_URL + "/auth/v1/token?grant_type=password")
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
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Error al iniciar sesión", Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show());
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
