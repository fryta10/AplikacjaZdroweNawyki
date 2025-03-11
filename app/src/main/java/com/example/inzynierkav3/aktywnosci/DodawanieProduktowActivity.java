package com.example.inzynierkav3.aktywnosci;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.inzynierkav3.R;
import com.example.inzynierkav3.adaptery.ProduktyAdapter;
import com.example.inzynierkav3.database.DatabaseHelper;
import com.example.inzynierkav3.modele.Produkt;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DodawanieProduktowActivity extends AppCompatActivity implements ProduktyAdapter.OnProduktClickListener {

    private DatabaseHelper databaseHelper;  // Deklaracja zmiennej
    private int typPosilku;
    private  String wybranaData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodawanie_produktow);

        databaseHelper = new DatabaseHelper(this);  // Inicjalizacja zmiennej

        ImageButton imgbtnZamknij = findViewById(R.id.btnZamknij);
        SearchView searchViewSzukanie = findViewById(R.id.searchViewWyszukanie);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewProdukty);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        List<Produkt> produkty = new ArrayList<>();  // Załaduj swoje produkty tutaj
        ProduktyAdapter adapter = new ProduktyAdapter(this, produkty, typPosilku,this);
        recyclerView.setAdapter(adapter);

        typPosilku = getIntent().getIntExtra("Typ_posilku", -1);
        wybranaData = getIntent().getStringExtra("WybranaData");

        searchViewSzukanie.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            // Użytkownik potwierdził wyszukiwanie
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            // Każda zmiana tekstu w polu wyszukiwania uruchamia tę metodę
            @Override
            public boolean onQueryTextChange(String s) {
                wyszukajProdukty(s, adapter);
                return false;
            }
        });

        imgbtnZamknij.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DodawanieProduktowActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void OnProduktClick(Produkt produkt) {
        Intent intent = new Intent(this, DodanieKonkretnegoProduktuuActivity.class);
        intent.putExtra("ProduktId", produkt.getId());
        intent.putExtra("NazwaProduktu", produkt.getNazwa());
        intent.putExtra("KalorieProduktu", produkt.getKalorie());
        intent.putExtra("BialkaProduktu", produkt.getBialka());
        intent.putExtra("TluszczeProduktu", produkt.getTluszcze());
        intent.putExtra("WeglowodanyProduktu", produkt.getWeglowodany());
        intent.putExtra("Typ_posilku", typPosilku);
        intent.putExtra("WybranaData", wybranaData);
        startActivity(intent);

    }
    private void wyszukajProdukty(String s, ProduktyAdapter adapter){
        SQLiteDatabase db= databaseHelper.getReadableDatabase();
        Cursor cursor =db.rawQuery("Select * FROM produkty WHERE nazwa LIKE ?", new String[]{"%"+ s +"%"});

        List<Produkt> produktyList = new ArrayList<>();
        Log.d("SQL_RESULT", "Liczba wyników: " + cursor.getCount());

        if(cursor.moveToFirst()){

            do{
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String nazwa = cursor.getString(cursor.getColumnIndexOrThrow("nazwa"));
                int kalorie = cursor.getInt(cursor.getColumnIndexOrThrow("kalorie"));
                int bialka = cursor.getInt(cursor.getColumnIndexOrThrow("bialka"));
                int tluszcze = cursor.getInt(cursor.getColumnIndexOrThrow("tluszcze"));
                int weglowodany = cursor.getInt(cursor.getColumnIndexOrThrow("weglowodany"));

                Produkt produkt = new Produkt(id, nazwa, kalorie, bialka, tluszcze, weglowodany);
                produktyList.add(produkt);

            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.updateList(produktyList);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        super.onBackPressed(); // Wywołanie domyślnej obsługi
    }

}
