package com.example.inzynierkav3.aktywnosci;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.inzynierkav3.R;

import java.util.function.ToIntFunction;

public class PodstawoweInformacjeActivity extends AppCompatActivity {

    private String plec;
    // Nazwa preferencji
    private final String PREFS_NAME = "UserPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podstawowe_informacje);


        ImageButton imgbtnWstecz = findViewById(R.id.btn_back);
        EditText editTextWiek = findViewById(R.id.et_wiek);
        EditText editTextWzrost = findViewById(R.id.et_wzrost);
        Button zapiszInformacje= findViewById(R.id.btn_zapisz_informacje);
        RadioGroup rgPlec=findViewById(R.id.rg_plec);

        imgbtnWstecz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        rgPlec.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton_plec = findViewById(i);
                plec = radioButton_plec.getText().toString();

            }
        });
        zapiszInformacje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String wiekText = editTextWiek.getText().toString();
                String wzrostText= editTextWzrost.getText().toString();

                if (!wiekText.isEmpty() && !wzrostText.isEmpty()){

                    int wiek = Integer.parseInt(wiekText);
                    int wzrost = Integer.parseInt(wzrostText);

                    SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("wiek", wiek);
                    editor.putInt("wzrost", wzrost);
                    editor.putString("plec", plec);
                    editor.apply(); // Zapisz zmiany
                    Intent intent = new Intent(PodstawoweInformacjeActivity.this, CeleActivity.class);
                    startActivity(intent);

                }
                else {
                    Toast.makeText(getApplicationContext(),"proszę wprowadzić poprawne dane! ", Toast.LENGTH_SHORT).show();
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