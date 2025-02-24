package fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.truek.Product;
import com.truek.R;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FragmentShare extends Fragment {
    private static final String SUPABASE_URL = "https://pgosafydlwskwtvnuokk.supabase.co";
    private static final String SUPABASE_API_KEY = "TU_SUPABASE_API_KEY"; // ⚠️ Usa una variable segura en producción
    private static final String BUCKET_NAME = "Imagenes_1";

    private ImageView imageView;
    private EditText productName, productPrice, productDescription;
    private Uri imageUri;

    private final ActivityResultLauncher<Intent> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    imageView.setImageURI(imageUri);
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);

        imageView = view.findViewById(R.id.imageView);
        productName = view.findViewById(R.id.productName);
        productPrice = view.findViewById(R.id.productPrice);
        productDescription = view.findViewById(R.id.productDescription);

        Button galleryButton = view.findViewById(R.id.galleryButton);
        Button uploadButton = view.findViewById(R.id.uploadProductButton);

        galleryButton.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(galleryIntent);
        });

        uploadButton.setOnClickListener(v -> uploadImage());

        return view;
    }

    private void uploadImage() {
        if (imageUri == null) {
            Toast.makeText(getActivity(), "Selecciona una imagen primero", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            try (InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri)) {
                if (inputStream != null) {
                    byte[] bytes = new byte[0];
                    sendToSupabase(bytes);
                } else {
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getActivity(), "Error al leer la imagen", Toast.LENGTH_SHORT).show());
                }
            } catch (IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getActivity(), "Error al procesar la imagen", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void sendToSupabase(byte[] imageBytes) {
        String filename = "imagen_" + System.currentTimeMillis() + ".jpg";

        // Subir la imagen al bucket 'Imagenes_1' usando OkHttp
        String uploadUrl = SUPABASE_URL + "/storage/v1/object/upload/public/" + BUCKET_NAME + "/" + filename;

        RequestBody requestBody = RequestBody.create(imageBytes, MediaType.parse("image/jpeg"));
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("file", filename, requestBody);

        Request request = new Request.Builder()
                .url(uploadUrl)
                .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                .header("apikey", SUPABASE_API_KEY)
                .header("Content-Type", "image/jpeg")
                .post(builder.build())
                .build();

        OkHttpClient client = new OkHttpClient();
        new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                String responseBody = response.body() != null ? response.body().string() : "";
                if (!response.isSuccessful()) {
                    Log.e("UPLOAD", "Error al subir imagen: " + response.code() + " - " + responseBody);
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getActivity(), "Error al subir imagen", Toast.LENGTH_SHORT).show());
                    return;
                }

                String imageUrl = SUPABASE_URL + "/storage/v1/object/public/" + BUCKET_NAME + "/" + filename;
                Log.d("UPLOAD", "Imagen subida correctamente: " + imageUrl);
                saveProductToDatabase(imageUrl);
            } catch (IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getActivity(), "Error de conexión", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void saveProductToDatabase(String imageUrl) {
        String name = productName.getText().toString();
        String price = productPrice.getText().toString();
        String description = productDescription.getText().toString();

        if (name.isEmpty() || price.isEmpty() || description.isEmpty()) {
            getActivity().runOnUiThread(() ->
                    Toast.makeText(getActivity(), "Completa todos los campos", Toast.LENGTH_SHORT).show());
            return;
        }

        // Guardar en la base de datos usando la API REST de Supabase (PostgREST)
        String json = "{" +
                "\"name\": \"" + name + "\"," +
                "\"price\": " + (price.isEmpty() ? "0" : price) + "," +
                "\"description\": \"" + description + "\"," +
                "\"image_url\": \"" + imageUrl + "\"}";

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/rest/v1/products")
                .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient();
        new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    Log.d("DATABASE", "Producto guardado correctamente.");
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getActivity(), "Producto subido correctamente", Toast.LENGTH_SHORT).show());
                } else {
                    Log.e("DATABASE", "Error al guardar el producto: " + response.code());
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getActivity(), "Error al guardar el producto", Toast.LENGTH_SHORT).show());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
