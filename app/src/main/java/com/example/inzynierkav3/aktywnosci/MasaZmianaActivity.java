package com.example.inzynierkav3.aktywnosci;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.inzynierkav3.R;

public class MasaZmianaActivity extends AppCompatActivity {

    private final String PREFS_NAME = "UserPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masa_zmiana);

        Button btnZapiszZmianeWagi=findViewById(R.id.btn_zapisz_zmiane_wagi);
        ImageButton imgbtnWstecz = findViewById(R.id.btn_back);
        Spinner spinner_zmiana_wagi= findViewById(R.id.spinner_zmiana_wagi);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this, R.array.zmiana_wagi_adapter, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_zmiana_wagi.setAdapter(adapter);

        imgbtnWstecz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnZapiszZmianeWagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String zmianaWagi=spinner_zmiana_wagi.getSelectedItem().toString();
                if
                (!zmianaWagi.isEmpty()){
                    float zmianawagi = Float.parseFloat(zmianaWagi);
                    SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                   editor.putFloat("zmiana_wagi", zmianawagi);
                    editor.apply();
                    Intent intent = new Intent(MasaZmianaActivity.this, CeleActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(),"proszę wprowadzić poprawne dane! ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}