package com.example.inzynierkav3.aktywnosci;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.inzynierkav3.R;

public class AktywnoscFizycznaActivity extends AppCompatActivity {

    private String aktywnosc = null;  // Zmienna inicjalizowana jako null
    private final String PREFS_NAME = "UserPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aktywnosc_fizyczna);
        ImageButton imgbtnWstecz = findViewById(R.id.btn_back);
        Button zapiszAktywnosc = findViewById(R.id.btn_zapisz_aktywnosc);

        RadioGroup rgAktywnosc = findViewById(R.id.rg_aktywnosc);

        imgbtnWstecz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        rgAktywnosc.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rbAktywnosc = findViewById(i);
                if (rbAktywnosc != null) {
                    aktywnosc = rbAktywnosc.getText().toString();
                }
            }

        });

        zapiszAktywnosc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sprawdzenie, czy aktywnosc nie jest null i czy nie jest pusta

                SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if (aktywnosc != null && !aktywnosc.isEmpty()) {
                    editor.putString("aktywnosc", aktywnosc);
                    editor.apply();
                    Intent intent = new Intent(AktywnoscFizycznaActivity.this, CeleActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Proszę wybrać aktywność!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        super.onBackPressed(); // Wywołanie domyślnej obsługi
    }

}
