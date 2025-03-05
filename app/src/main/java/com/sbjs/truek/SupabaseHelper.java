package com.sbjs.truek;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;

public class SupabaseHelper {


    // Método para guardar el usuario en Supabase
    public static void saveUserInSupabase(Context context, String firebaseToken) {
        // La URL de tu endpoint en Supabase (reemplaza con tu propio proyecto)
        String url = "https://your-supabase-project.supabase.co/auth/v1/users";

        // Crear el cuerpo de la solicitud
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("provider", "firebase");
            requestBody.put("firebase_token", firebaseToken); // Token JWT de Firebase
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Crear la cola de solicitudes
        RequestQueue queue = Volley.newRequestQueue(context);

        // Crear la solicitud POST
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Procesar la respuesta de Supabase
                        Log.d("Supabase", "Usuario autenticado correctamente: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores en la solicitud
                        Log.e("Supabase", "Error en la solicitud: ", error);
                    }
                }) {
            @Override
            public byte[] getBody() {
                return requestBody.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        // Añadir la solicitud a la cola
        queue.add(stringRequest);
    }
}