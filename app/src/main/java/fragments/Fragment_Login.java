package fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.truek.MainActivity;
import com.truek.MainView;
import com.truek.R;

public class Fragment_Login extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el diseño del fragmento
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Encuentra los botones por su ID
        Button nextButton = view.findViewById(R.id.button1);
        Button cancelButton = view.findViewById(R.id.button2);


        nextButton.setOnClickListener(v -> openMainActivity());

        cancelButton.setOnClickListener(v -> {
            // Cargar la animación de salida (de izquierda a derecha)
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_left_rigth);
            animation.setDuration(500);

            // Aplicar la animación a la vista del fragmento
            view.startAnimation(animation);

            // Añadir un listener para realizar la navegación una vez que termine la animación
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                // Eliminar el fragmento antes de navegar
                @Override
                public void onAnimationEnd(Animation animation) {

                    getFragmentManager().beginTransaction().remove(Fragment_Login.this).commitAllowingStateLoss();
                    openSignup();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
        });

        return view;
    }

    private void openMainActivity() {
        // Intent para abrir MainActivity
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    private void openSignup() {
        // Intent para volver a MainView
        Intent intent = new Intent(getActivity(), MainView.class);
        // Añadir la animación de entrada
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        // Añadir la animación de transición de la actividad
        getActivity().overridePendingTransition(R.anim.anim_rigth_left, android.R.anim.fade_in);
    }
}
