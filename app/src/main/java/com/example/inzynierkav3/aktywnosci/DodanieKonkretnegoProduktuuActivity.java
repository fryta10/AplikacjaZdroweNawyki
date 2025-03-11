package com.example.inzynierkav3.aktywnosci;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inzynierkav3.R;
import com.example.inzynierkav3.database.DatabaseHelper;

import org.w3c.dom.Text;

import java.time.LocalDate;

public class DodanieKonkretnegoProduktuuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodanie_konkretnego_produktuu);

        Button btnDodajProdukt = findViewById(R.id.buttonDodajProdukt);
        TextView nazwaTextView = findViewById(R.id.textViewNazwaProduktu);
        TextView kalorieTextView = findViewById(R.id.textViewKalorie);
        TextView bialkaTextView = findViewById(R.id.textViewBialka);
        TextView tluszczeTextView = findViewById(R.id.textViewTluszcze);
        TextView weglowodanyTextView = findViewById(R.id.textViewWeglowodany);
        EditText editTextWaga = findViewById(R.id.editTextWaga);


        int kalorieProduktu=getIntent().getIntExtra("KalorieProduktu", -1);
        int bialkaProduktu=getIntent().getIntExtra("BialkaProduktu", -1);
        int tluszczeProduktu=getIntent().getIntExtra("TluszczeProduktu", -1);
        int weglowodanyProduktu=getIntent().getIntExtra("WeglowodanyProduktu", -1);
        String nazwaProduktu = getIntent().getStringExtra("NazwaProduktu");
        String wybranaDataString = getIntent().getStringExtra("WybranaData");
        LocalDate wybranaData = LocalDate.parse(wybranaDataString);


        int waga=100;
        editTextWaga.setText(String.valueOf(waga));

        int przeliczoneKalorie = kalorieProduktu * waga / 100;
        int przeliczoneBialka = bialkaProduktu * waga / 100;
        int przeliczoneTluszcze = tluszczeProduktu * waga / 100;
        int przeliczoneWeglowodany = weglowodanyProduktu * waga / 100;

        nazwaTextView.setText(nazwaProduktu);
        kalorieTextView.setText("kcal: " +String.valueOf(przeliczoneKalorie));
        bialkaTextView.setText("białko: "+String.valueOf(przeliczoneBialka));
        tluszczeTextView.setText("tłuscze: "+String.valueOf(przeliczoneTluszcze));
        weglowodanyTextView.setText("węglowodany: "+String.valueOf(przeliczoneWeglowodany));

        editTextWaga.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                int waga;
                try {
                    waga=Integer.parseInt(editTextWaga.getText().toString());
                }catch (NumberFormatException e)
                {
                    waga=100;
                }

                int przeliczoneKalorie = kalorieProduktu * waga / 100;
                int przeliczoneBialka = bialkaProduktu * waga / 100;
                int przeliczoneTluszcze = tluszczeProduktu * waga / 100;
                int przeliczoneWeglowodany = weglowodanyProduktu * waga / 100;

                nazwaTextView.setText(nazwaProduktu);
                kalorieTextView.setText("kcal: " +String.valueOf(przeliczoneKalorie));
                bialkaTextView.setText("białko: "+String.valueOf(przeliczoneBialka));
                tluszczeTextView.setText("tłuscze: "+String.valueOf(przeliczoneTluszcze));
                weglowodanyTextView.setText("węglowodany: "+String.valueOf(przeliczoneWeglowodany));
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        btnDodajProdukt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int produktId = getIntent().getIntExtra("ProduktId", -1);
                int typPosilku=getIntent().getIntExtra("Typ_posilku", -1);

                int waga = Integer.parseInt(editTextWaga.getText().toString());
                int przeliczoneKalorie = kalorieProduktu * waga / 100;
                int przeliczoneBialka = bialkaProduktu * waga / 100;
                int przeliczoneTluszcze = tluszczeProduktu * waga / 100;
                int przeliczoneWeglowodany = weglowodanyProduktu * waga / 100;

                Log.d("DodanieProduktu", "Waga: " + waga);
                Log.d("DodanieProduktu", "Przeliczone Kalorie: " + przeliczoneKalorie);



                DatabaseHelper dbHelper = new DatabaseHelper(DodanieKonkretnegoProduktuuActivity.this);
                dbHelper.dodajPosilek(produktId, typPosilku, waga, przeliczoneKalorie, przeliczoneBialka, przeliczoneTluszcze, przeliczoneWeglowodany, wybranaData);
                Toast.makeText(DodanieKonkretnegoProduktuuActivity.this, "dodano produkt", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DodanieKonkretnegoProduktuuActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

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