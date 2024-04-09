package com.example.practicaltest01var03;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PracticalTest01Var03SecondaryActivity extends AppCompatActivity {

    private TextView textViewSecondActivity;
    private Button buttonCorrect;
    private Button buttonIncorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_var03_secondary);

        textViewSecondActivity = findViewById(R.id.textViewSecondActivity);
        buttonCorrect = findViewById(R.id.buttonCorrect);
        buttonIncorrect = findViewById(R.id.buttonIncorrect);

        Intent intent = getIntent();
        int sum = intent.getIntExtra(Constants.SUM, 0);

        textViewSecondActivity.setText(String.valueOf(sum));

        buttonCorrect.setOnClickListener(v -> {
            Intent result = new Intent();
            setResult(RESULT_OK, result);
            finish();
        });

        buttonIncorrect.setOnClickListener(v -> {
            Intent result = new Intent();
            setResult(RESULT_CANCELED, result);
            finish();
        });
    }
}