package fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
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

import com.truek.R;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.*;

public class FragmentShare extends Fragment {

    private static final String SUPABASE_URL = "https://pgosafydlwskwtvnuokk.supabase.co";
    private static final String SUPABASE_API_KEY = "TU_CLAVE_API";
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

    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    Bitmap imageBitmap = (Bitmap) result.getData().getExtras().get("data");
                    imageView.setImageBitmap(imageBitmap);
                    imageUri = getImageUriFromBitmap(imageBitmap);
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

        Button cameraButton = view.findViewById(R.id.cameraButton);
        Button galleryButton = view.findViewById(R.id.galleryButton);
        Button uploadButton = view.findViewById(R.id.uploadProductButton);

        cameraButton.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraLauncher.launch(cameraIntent);
        });

        galleryButton.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(galleryIntent);
        });

        uploadButton.setOnClickListener(v -> uploadImage());
        return view;
    }

    private Uri getImageUriFromBitmap(Bitmap bitmap) {
        String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "title", null);
        return Uri.parse(path);
    }

    private void uploadImage() {
        if (imageUri == null) {
            Toast.makeText(getActivity(), "Selecciona una imagen primero", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
            sendToSupabase(base64Image);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error al leer la imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendToSupabase(String base64Image) {
        String filename = "imagen_" + System.currentTimeMillis() + ".jpg";
        String url = SUPABASE_URL + "/storage/v1/object/" + BUCKET_NAME + "/" + filename;

        RequestBody body = RequestBody.create(Base64.decode(base64Image, Base64.DEFAULT), MediaType.parse("image/jpeg"));

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                .header("Content-Type", "image/jpeg")
                .put(body)
                .build();

        OkHttpClient client = new OkHttpClient();
        new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String imageUrl = SUPABASE_URL + "/storage/v1/object/" + BUCKET_NAME + "/" + filename;
                    saveProductToDatabase(imageUrl);
                } else {
                    getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Error al subir imagen: " + response.message(), Toast.LENGTH_SHORT).show());
                }
            } catch (IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Error de conexión", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void saveProductToDatabase(String imageUrl) {
        String name = productName.getText().toString();
        String price = productPrice.getText().toString();
        String description = productDescription.getText().toString();

        String json = "{" +
                "\"name\": \"" + name + "\"," +
                "\"price\": " + price + "," +
                "\"description\": \"" + description + "\"," +
                "\"image_url\": \"" + imageUrl + "\"}";

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/rest/v1/DB_Truek")
                .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient();
        new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Producto subido con éxito", Toast.LENGTH_SHORT).show());
                } else {
                    getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Error al guardar el producto", Toast.LENGTH_SHORT).show());
                }
            } catch (IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Error de conexión", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
