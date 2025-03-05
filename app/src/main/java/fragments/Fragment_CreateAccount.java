package fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sbjs.truek.MainActivity;
import com.sbjs.truek.MainView;
import com.sbjs.truek.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Fragment_CreateAccount extends Fragment {

    private EditText emailEditText, passwordEditText;
    // Endpoint para signup en Supabase
    private static final String SUPABASE_URL = "https://pgosafydlwskwtvnuokk.supabase.co/auth/v1/signup";
    // Clave pública (API Key) de tu proyecto en Supabase
    private static final String SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InBnb3NhZnlkbHdza3d0dm51b2trIiwicm9sZSI6ImFub24iLCJpYXQiOjE3Mzk0MzgyMTcsImV4cCI6MjA1NTAxNDIxN30.EmB_NLAqXji3UhtgaQsc4VmGrtnUHQlNiAb6Oau3fQo";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_account, container, false);

        Button nextButton = view.findViewById(R.id.buttoncrear);
        Button cancelButton = view.findViewById(R.id.button2);
        emailEditText = view.findViewById(R.id.email);
        passwordEditText = view.findViewById(R.id.password);

        nextButton.setOnClickListener(v -> {
            if (validateInputs()) {
                registerUser();
            }
        });

        cancelButton.setOnClickListener(v -> openSignup());

        return view;
    }

    private void registerUser() {
        new Thread(() -> {
            HttpURLConnection conn = null;
            try {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                URL url = new URL(SUPABASE_URL);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("apikey", SUPABASE_KEY);
                conn.setRequestProperty("Authorization", "Bearer " + SUPABASE_KEY);
                conn.setDoOutput(true);

                // Construimos el cuerpo JSON para la solicitud
                JSONObject json = new JSONObject();
                json.put("email", email);
                json.put("password", password);

                // Enviamos el cuerpo de la solicitud
                OutputStream os = conn.getOutputStream();
                os.write(json.toString().getBytes());
                os.close();

                // Obtenemos el código de respuesta (p.ej. 200, 400, 401, etc.)
                int responseCode = conn.getResponseCode();
                Log.d("RegisterUser", "Response Code: " + responseCode);

                // Leemos la respuesta (o el error) que nos devuelve Supabase
                StringBuilder response = new StringBuilder();
                Scanner scanner;
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Si todo fue bien, leemos el stream normal
                    scanner = new Scanner(conn.getInputStream());
                } else {
                    // Si hubo error, leemos el stream de error
                    scanner = new Scanner(conn.getErrorStream());
                }
                while (scanner.hasNext()) {
                    response.append(scanner.nextLine());
                }
                scanner.close();

                String responseStr = response.toString();
                Log.d("RegisterUser", "Response: " + responseStr);

                // Analizamos el resultado en el hilo principal para actualizar la UI
                requireActivity().runOnUiThread(() -> {
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // Registro exitoso
                        Toast.makeText(getContext(), "¡Cuenta creada exitosamente!", Toast.LENGTH_SHORT).show();
                        JSONObject jsonResponse = null;
                        try {
                            jsonResponse = new JSONObject(responseStr);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        String accessToken = null;
                        try {
                            accessToken = jsonResponse.getString("access_token");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        // Aquí obtienes el JWT
                        Log.d("JWT", "Access Token: " + accessToken);

                        // Puedes almacenar el token en SharedPreferences para usarlo más tarde
                        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("auth", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("jwt", accessToken);
                        editor.apply();

                        // Navegar a la pantalla principal
                        openMain();
                    } else {
                        // Ocurrió algún error, parseamos el JSON de error si existe
                        String userFriendlyMessage = parseErrorMessage(responseStr);
                        Toast.makeText(getContext(), userFriendlyMessage, Toast.LENGTH_LONG).show();
                    }
                });

            } catch (Exception e) {
                // Manejo de excepción en la conexión
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(),
                            "Fallo de registro: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    Log.e("Fragment_CreateAccount", "Error de registro", e);
                });
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }).start();
    }

    private String parseErrorMessage(String responseStr) {
        try {
            JSONObject errorJson = new JSONObject(responseStr);
            String errorCode = errorJson.optString("error_code", "");
            String msg = errorJson.optString("msg", "");

            switch (errorCode) {
                case "email_address_invalid":
                    return "El correo electrónico no es válido. Por favor, revisa su formato.";
                case "email_already_registered":
                    return "Correo registrado. Intenta iniciar sesión.";
                case "invalid_password":
                    return "La contraseña proporcionada no cumple con los requisitos.";
                default:
                    return !msg.isEmpty() ? msg : "No se pudo crear la cuenta. Inténtalo de nuevo.";
            }
        } catch (Exception e) {
            return "No se pudo crear la cuenta. Inténtalo de nuevo.";
        }
    }

    private boolean validateInputs() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(getContext(), "Por favor ingresa tu correo electrónico.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.isEmpty()) {
            Toast.makeText(getContext(), "Por favor ingresa una contraseña.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 6) {
            Toast.makeText(getContext(), "La contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void openMain() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        // Animación de transición opcional
        getActivity().overridePendingTransition(R.anim.anim_rigth_left, android.R.anim.fade_in);
    }

    private void openSignup() {
        Intent intent = new Intent(getActivity(), MainView.class);
        startActivity(intent);
        // Animación de transición opcional
        getActivity().overridePendingTransition(R.anim.anim_rigth_left, android.R.anim.fade_in);
    }
}
