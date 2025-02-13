package com.truek;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.*;

public class UploadProduct extends AppCompatActivity {

    private static final String SUPABASE_URL = "https://pgosafydlwskwtvnuokk.supabase.co"; // Reemplaza con tu URL
    private static final String SUPABASE_API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InBnb3NhZnlkbHdza3d0dm51b2trIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTczOTQzODIxNywiZXhwIjoyMDU1MDE0MjE3fQ.2kKFUfVEo9pXvHa4GfdRR20KoFKJEUa55r21AF_K1bE"; // Reemplaza con tu clave API
    private static final String BUCKET_NAME = "Imagenes_1"; // Nombre del bucket en Supabase

    private ImageView imageView;
    private EditText productName, productPrice, productDescription;
    private Uri imageUri;

    private final ActivityResultLauncher<Intent> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    imageView.setImageURI(imageUri);
                }
            });

    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bitmap imageBitmap = (Bitmap) result.getData().getExtras().get("data");
                    imageView.setImageBitmap(imageBitmap);
                    imageUri = getImageUriFromBitmap(imageBitmap); // Convertir Bitmap a Uri
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_product);

        // Inicialización de los elementos UI
        imageView = findViewById(R.id.imageView);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productDescription = findViewById(R.id.productDescription);

        Button cameraButton = findViewById(R.id.cameraButton);
        Button galleryButton = findViewById(R.id.galleryButton);
        Button uploadButton = findViewById(R.id.uploadProductButton);

        // Abre la cámara
        cameraButton.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraLauncher.launch(cameraIntent);
        });

        // Abre la galería
        galleryButton.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(galleryIntent);
        });

        // Subir el producto
        uploadButton.setOnClickListener(v -> uploadImage());
    }

    private Uri getImageUriFromBitmap(Bitmap bitmap) {
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", null);
        return Uri.parse(path);
    }

    private void uploadImage() {
        // Verificar que se haya seleccionado una imagen
        if (imageUri == null) {
            Toast.makeText(this, "Selecciona una imagen primero", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener el archivo de la imagen
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);

            // Convertir la imagen a Base64
            String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);

            // Subir la imagen y obtener la URL
            sendToSupabase(base64Image);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al leer la imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendToSupabase(String base64Image) {
        String filename = "imagen_" + System.currentTimeMillis() + ".jpg";
        String url = SUPABASE_URL + "/storage/v1/object/" + BUCKET_NAME + "/" + filename;

        // Crear el cuerpo de la solicitud para subir la imagen
        RequestBody body = RequestBody.create(
                Base64.decode(base64Image, Base64.DEFAULT),
                MediaType.parse("image/jpeg")
        );

        // Crear la solicitud
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                .header("Content-Type", "image/jpeg")
                .put(body)
                .build();

        // Ejecutar la solicitud en un hilo en segundo plano
        OkHttpClient client = new OkHttpClient();
        new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String imageUrl = SUPABASE_URL + "/storage/v1/object/" + BUCKET_NAME + "/" + filename;
                    // Ahora, guarda los detalles del producto en la base de datos
                    saveProductToDatabase(imageUrl);
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Error al subir imagen: " + response.message(), Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }



    private void saveProductToDatabase(String imageUrl) {
        String name = productName.getText().toString();
        String price = productPrice.getText().toString();
        String description = productDescription.getText().toString();

        // Crear un objeto JSON con los datos del producto
        String json = "{" +
                "\"name\": \"" + name + "\"," +
                "\"price\": " + price + "," +
                "\"description\": \"" + description + "\"," +
                "\"image_url\": \"" + imageUrl + "\"" +
                "}";

        Log.d("SaveProduct", "Datos del producto: " + json);


        // Hacer la solicitud a la base de datos de Supabase para guardar el producto
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/rest/v1/DB_Truek") // URL de la tabla de productos
                .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient();
        new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Producto subido con éxito", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    // Aquí obtenemos la respuesta detallada del error
                    String errorResponse = response.body() != null ? response.body().string() : "Error desconocido";
                    Log.e("Supabase", "Error al guardar el producto: " + errorResponse);
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Error al guardar el producto", Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

}
