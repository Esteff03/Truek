package fragments;



import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.truek.FirebaseTokenManager;
import com.truek.R;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FragmentShare extends Fragment {

    private static final int REQUEST_CODE_CAMERA = 100;
    private static final int REQUEST_CODE_STORAGE = 101;

    private static final String SUPABASE_URL = "https://pgosafydlwskwtvnuokk.supabase.co";
    private static final String SUPABASE_API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InBnb3NhZnlkbHdza3d0dm51b2trIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTczOTQzODIxNywiZXhwIjoyMDU1MDE0MjE3fQ.2kKFUfVEo9pXvHa4GfdRR20KoFKJEUa55r21AF_K1bE";
    private static final String BUCKET_NAME = "Imagenes_1";
    static final    String token = FirebaseTokenManager.getInstance().getFirebaseToken();
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
            if (checkCameraPermissions()) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraLauncher.launch(cameraIntent);
            } else {
                requestCameraPermission();
            }
        });

        galleryButton.setOnClickListener(v -> {
            if (checkStoragePermissions()) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickImageLauncher.launch(galleryIntent);
            } else {
                requestStoragePermission();
            }
        });

        uploadButton.setOnClickListener(v -> uploadImage());

        return view;

    }

    private boolean checkCameraPermissions() {
        return ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkStoragePermissions() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
    }

    private void requestStoragePermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_CODE_STORAGE);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE);
        }
    }

    // Manejar la respuesta de los permisos mirar
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_CODE_CAMERA) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                cameraLauncher.launch(cameraIntent);
//            } else {
//                Toast.makeText(getActivity(), "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
//            }
//        } else if (requestCode == REQUEST_CODE_STORAGE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                pickImageLauncher.launch(galleryIntent);
//            } else {
//                Toast.makeText(getActivity(), "Permiso de almacenamiento denegado", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    private Uri getImageUriFromBitmap(Bitmap bitmap) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "image_: "+System.currentTimeMillis() + ".jpg");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES); // Guarda en el directorio de imágenes

        Uri uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try (OutputStream outputStream = getActivity().getContentResolver().openOutputStream(uri)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream); // Guarda la imagen en el OutputStream
        } catch (IOException e) {
            e.printStackTrace();
        }

        return uri;
    }

    private void uploadImage() {
        if (imageUri == null) {
            Toast.makeText(getActivity(), "Selecciona una imagen primero", Toast.LENGTH_SHORT).show();
            return;
        }

        try (InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri)) {
            if (inputStream != null) {
                byte[] bytes = new byte[inputStream.available()];
                inputStream.read(bytes);
                sendToSupabase(bytes);  // Enviar directamente los bytes a Supabase
            } else {
                Toast.makeText(getActivity(), "No se pudo acceder a la imagen", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error al leer la imagen", Toast.LENGTH_SHORT).show();
        }
    }



    private void sendToSupabase(byte[] imageBytes) {
        String filename = "imagen_ uid"+ token +":"+ System.currentTimeMillis() +  ".jpg";  // Asumiendo que es JPG
        String url = SUPABASE_URL + "/storage/v1/object/" + BUCKET_NAME + "/" + filename;

        // Realiza la solicitud PUT a Supabase con el contenido de la imagen
        RequestBody body = RequestBody.create(imageBytes, MediaType.parse("image/jpeg"));

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
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getActivity(), "Error al subir imagen: " + response.message(), Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getActivity(), "Error de conexión", Toast.LENGTH_SHORT).show();
                });
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
