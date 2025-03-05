package com.sbjs.truek;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Notifications extends AppCompatActivity {

    private ImageView backMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        backMain = findViewById(R.id.arrow_back);
        backMain.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Notifications.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
