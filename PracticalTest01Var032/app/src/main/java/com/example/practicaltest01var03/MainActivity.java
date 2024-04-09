package com.example.practicaltest01var03;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private EditText editTextFirst;
    private EditText editTextBelowButtons;
    private TextView nonEditableTextView;
    private Button buttonPlus;
    private Button buttonMinus;
    private Button buttonNavigateToSecondActivity;

    private ActivityResultLauncher<Intent> activityResultLauncher;
    private final IntentFilter intentFilter = new IntentFilter();

    private final MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();
    private static class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(Constants.BROADCAST_RECEIVER_TAG, Objects.requireNonNull(intent.getStringExtra(Constants.BROADCAST_RECEIVER_EXTRA)));
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Extracting views by their IDs
        editTextFirst = findViewById(R.id.editTextFirst);
        editTextBelowButtons = findViewById(R.id.editTextBelowButtons);
        nonEditableTextView = findViewById(R.id.nonEditableTextView);
        buttonPlus = findViewById(R.id.buttonPlus);
        buttonMinus = findViewById(R.id.buttonMinus);
        buttonNavigateToSecondActivity = findViewById(R.id.buttonNavigateToSecondActivity);

        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the values from the EditTexts
                String value1Str = editTextFirst.getText().toString();
                String value2Str = editTextBelowButtons.getText().toString();

                if (!isNumeric(value1Str) || !isNumeric(value2Str)) {
                    Toast.makeText(MainActivity.this, "Please enter valid integer values.", Toast.LENGTH_SHORT).show();
                    return;
                }

                int value1 = Integer.parseInt(value1Str);
                int value2 = Integer.parseInt(value2Str);

                int result = value1 + value2;

                nonEditableTextView.setText(String.valueOf(result));

                if(!isNumeric(value1Str) && !isNumeric(value2Str)) {
                    Intent intent = new Intent(getApplicationContext(), PracticalTest01Var03Service.class);
                    intent.putExtra(Constants.EDITTEXTFIRST, Integer.parseInt(editTextFirst.getText().toString()));
                    intent.putExtra(Constants.EDITTEXTBELLOWBUTTONS,  Integer.parseInt(editTextBelowButtons.getText().toString()));
                    getApplicationContext().startService(intent);
                }
            }

        });

        buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value1Str = editTextFirst.getText().toString();
                String value2Str = editTextBelowButtons.getText().toString();

                if (!isNumeric(value1Str) || !isNumeric(value2Str)) {
                    // Display toast indicating the error
                    Toast.makeText(MainActivity.this, "Please enter valid integer values.", Toast.LENGTH_SHORT).show();
                    return;
                }

                int value1 = Integer.parseInt(value1Str);
                int value2 = Integer.parseInt(value2Str);

                int result = value1 - value2;

                nonEditableTextView.setText(String.valueOf(result));
            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                if (result.getData() == null) {
                    return;
                }
                Toast.makeText(this, "The activity returned with CORRECT  ", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "The activity returned with INCORRECT " , Toast.LENGTH_LONG).show();
            }
        });

        buttonNavigateToSecondActivity.setOnClickListener(v -> {

            Intent intent = new Intent(this, PracticalTest01Var03SecondaryActivity.class);

            int sum = Integer.parseInt(nonEditableTextView.getText().toString());
            intent.putExtra(Constants.SUM, sum);

            activityResultLauncher.launch(intent);
        });

        for (int index = 0; index < Constants.actionTypes.length; index++) {
            intentFilter.addAction(Constants.actionTypes[index]);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(messageBroadcastReceiver, intentFilter, Context.RECEIVER_EXPORTED);
        } else {
            registerReceiver(messageBroadcastReceiver, intentFilter);
        }
    }

    @Override
    protected void onPause() {
        unregisterReceiver(messageBroadcastReceiver);
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, PracticalTest01Var03Service.class);
        stopService(intent);

        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("editTextFirst", editTextFirst.getText().toString());
        outState.putString("editTextBelowButtons", editTextBelowButtons.getText().toString());
        outState.putString("nonEditableTextView", nonEditableTextView.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.containsKey("editTextFirst")) {
            editTextFirst.setText(savedInstanceState.getString("editTextFirst"));
        } else {
            editTextFirst.setText("0");
        }

        if (savedInstanceState.containsKey("editTextBelowButtons")) {
            editTextBelowButtons.setText(savedInstanceState.getString("editTextBelowButtons"));
        } else {
            editTextBelowButtons.setText("0");
        }

        if (savedInstanceState.containsKey("nonEditableTextView")) {
            nonEditableTextView.setText(savedInstanceState.getString("nonEditableTextView"));
        } else {
            nonEditableTextView.setText("0");
        }
    }

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }
}