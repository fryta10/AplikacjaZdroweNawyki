package com.example.inzynierkav3.aktywnosci;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inzynierkav3.R;

public class MasaActivity extends AppCompatActivity {

    private final String PREFS_NAME = "UserPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masa);

        ImageButton imgbtnWstecz = findViewById(R.id.btn_back);
        EditText editTextWaga = findViewById(R.id.et_waga);
        EditText editTextWagaDocelowa = findViewById( R.id.et_waga_docelowa);
        Button btnZapiszWage = findViewById(R.id.btn_zapisz_wage);
        imgbtnWstecz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


            btnZapiszWage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String wagatext=editTextWaga.getText().toString();
                String wagaDocelowatext =editTextWagaDocelowa.getText().toString();

                if (!wagatext.isEmpty() && !wagaDocelowatext.isEmpty()){

                    int waga = Integer.parseInt(wagatext);
                    int wagaDocelowa = Integer.parseInt(wagaDocelowatext);

                    SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("waga", waga);
                    editor.putInt("waga_docelowa", wagaDocelowa);
                    editor.apply();
                    Intent intent = new Intent(MasaActivity.this, CeleActivity.class);
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