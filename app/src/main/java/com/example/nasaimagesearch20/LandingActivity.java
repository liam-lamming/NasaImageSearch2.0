package com.example.nasaimagesearch20;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

public class LandingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
    }

    public void onStartButtonClick(View view) {
        // Start the MainActivity when the button is clicked
        Intent intent = new Intent(LandingActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
