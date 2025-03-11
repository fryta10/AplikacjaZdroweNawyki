package com.example.inzynierkav3.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.inspector.StaticInspectionCompanionProvider;
import android.database.Cursor;

import androidx.annotation.Nullable;

import com.example.inzynierkav3.modele.Produkt;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String nazwaBazy = "baza_produkty.db";
    private static final int bazaWersja = 2;

    public DatabaseHelper(@Nullable Context context) {
        super(context, nazwaBazy, null, bazaWersja);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE produkty (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nazwa TEXT NOT NULL," +
                "kalorie INTEGER NOT NULL," +
                "bialka INTEGER NOT NULL," +
                "tluszcze INTEGER NOT NULL," +
                "weglowodany INTEGER NOT NULL)");

        db.execSQL("CREATE TABLE posilki(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "produkt_id INTEGER," +
                "typ_posilku INTEGER," +
                "waga INTEGER NOT NULL," +
                "kalorie INTEGER NOT NULL," +
                "bialka INTEGER NOT NULL," +
                "tluszcze INTEGER NOT NULL," +
                "weglowodany INTEGER NOT NULL," +
                "data_spozycia DATE DEFAULT(date('now'))," +
                "FOREIGN KEY (produkt_id) REFERENCES produkty(id))");


        // Dodawanie 50 przykładowych rekordów do tabeli
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Jabłko', 52, 0, 0, 14)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Banana', 89, 1, 0, 23)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Kurczak', 165, 31, 4, 0)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Ryż biały', 130, 2, 0, 28)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Jajko', 155, 13, 11, 1)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Mleko 2%', 50, 3, 2, 5)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Ser żółty', 402, 25, 33, 1)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Chleb pełnoziarnisty', 247, 13, 4, 41)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Makaron pełnoziarnisty', 124, 5, 1, 25)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Piwo jasne', 43, 0, 0, 3.6)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Wódka', 231, 0, 0, 0)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Cola', 42, 0, 0, 10)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Chipsy ziemniaczane', 536, 7, 35, 53)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Masło orzechowe', 588, 25, 50, 20)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Pizza margherita', 266, 11, 10, 33)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Hamburger', 295, 17, 14, 29)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Frytki', 312, 4, 15, 41)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Keczup', 112, 1, 0, 25)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Musztarda', 66, 4, 5, 7)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Majonez', 680, 1, 75, 2)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Jogurt naturalny', 59, 10, 3, 4)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Jogurt grecki', 97, 10, 5, 3)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Lody waniliowe', 207, 4, 11, 24)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Baton czekoladowy', 245, 2, 12, 30)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Czekolada mleczna', 535, 7, 30, 58)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Orzechy ziemne', 567, 25, 49, 16)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Migdały', 579, 21, 50, 22)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Awokado', 160, 2, 15, 9)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Masło', 717, 0, 81, 0)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Twaróg półtłusty', 97, 17, 4, 2)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Łosoś', 208, 20, 13, 0)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Tuńczyk w sosie własnym', 132, 29, 1, 0)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Szynka wieprzowa', 145, 21, 6, 1)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Pierś z indyka', 135, 30, 1, 0)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Parówki wieprzowe', 300, 12, 27, 1)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Sardynki w oleju', 208, 24, 11, 0)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Miód', 304, 0, 0, 82)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Kawa czarna', 2, 0, 0, 0)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Herbata zielona', 0, 0, 0, 0)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Koktajl owocowy', 60, 1, 0, 15)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Szarlotka', 237, 2, 11, 34)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Drożdżówka z serem', 344, 8, 14, 45)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Kostka masła', 717, 0, 81, 0)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Chipsy tortilla', 497, 7, 25, 63)");
        db.execSQL("INSERT INTO produkty (nazwa, kalorie, bialka, tluszcze, weglowodany) VALUES ('Kefir', 60, 3, 3, 5)");
    }

    // Metoda onUpgrade jest wywoływana, gdy wersja bazy danych zostanie zmieniona
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Usuwanie starych tabel, jeśli istnieją
        db.execSQL("DROP TABLE IF EXISTS posilki");
        db.execSQL("DROP TABLE IF EXISTS produkty");
        // Tworzenie nowych tabel
        onCreate(db);
    }

    public void dodajPosilek(int produktId, int typPosilku, int waga, int kalorie, int bialka, int tluszcze, int weglowodany, LocalDate data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("produkt_id", produktId);
        values.put("typ_Posilku", typPosilku);
        values.put("waga", waga);
        values.put("kalorie", kalorie);
        values.put("bialka", bialka);
        values.put("tluszcze", tluszcze);
        values.put("weglowodany", weglowodany);
        values.put("data_spozycia", data.toString());
        db.insert("posilki", null, values);
        db.close();
    }

    public void usunPosilek(int produktId, int typPosilku) {
        SQLiteDatabase db = this.getWritableDatabase();

        int deletedRows = db.delete("posilki", "produkt_id=? AND typ_Posilku=?", new String[]{
                String.valueOf(produktId), String.valueOf(typPosilku)});
        db.close();

        if (deletedRows > 0) {
            Log.d("DatabaseHelper", "Pomyślnie usunięto produkt o id: " + produktId);
        } else {
            Log.d("DatabaseHelper", "Nie udało się usunąć produktu o id: " + produktId);
        }

    }



    public List<Produkt> pobierzPosilkiNaDzien(int typPosilku, LocalDate date) {
        List<Produkt> produkty = new ArrayList<>();
        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT posilki.produkt_id, produkty.nazwa, posilki.kalorie, posilki.bialka, posilki.tluszcze, posilki.weglowodany " +
                "FROM posilki " +
                "JOIN produkty ON posilki.produkt_id = produkty.id " +
                "WHERE posilki.typ_posilku = ? " +
                "AND posilki.data_spozycia = ? ";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(typPosilku), formattedDate});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("produkt_id"));
                String nazwa = cursor.getString(cursor.getColumnIndexOrThrow("nazwa"));
                int kalorie = cursor.getInt(cursor.getColumnIndexOrThrow("kalorie"));
                int bialka = cursor.getInt(cursor.getColumnIndexOrThrow("bialka"));
                int tluszcze = cursor.getInt(cursor.getColumnIndexOrThrow("tluszcze"));
                int weglowodany = cursor.getInt(cursor.getColumnIndexOrThrow("weglowodany"));

                Produkt produkt = new Produkt(id, nazwa, kalorie, bialka, tluszcze, weglowodany);
                produkty.add(produkt);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return produkty;
    }

    public Map<String, Integer> podsumowaniePosilku(String data, String typPosilku){
        SQLiteDatabase db=this.getReadableDatabase();
        Map<String, Integer> podsumowanie_Posilku = new HashMap<>();
        String query = "SELECT SUM(kalorie) as sumaKalorii, SUM(bialka) as sumaBialek, SUM(tluszcze) as sumaTluszczy, SUM(weglowodany) as sumaWegli FROM posilki WHERE data_spozycia=? AND typ_posilku=?";
        Cursor cursor =db.rawQuery(query, new String[]{data, typPosilku});
        if(cursor.moveToFirst()){
            podsumowanie_Posilku.put("kalorie",cursor.getInt(cursor.getColumnIndexOrThrow("sumaKalorii")));
            podsumowanie_Posilku.put("bialka",cursor.getInt(cursor.getColumnIndexOrThrow("sumaBialek")));
            podsumowanie_Posilku.put("tluszcze",cursor.getInt(cursor.getColumnIndexOrThrow("sumaTluszczy")));
            podsumowanie_Posilku.put("weglowodany",cursor.getInt(cursor.getColumnIndexOrThrow("sumaWegli")));
        }
        cursor.close();
        db.close();
        return podsumowanie_Posilku;
    }

    public Map<String, Integer> podsumowanieDnia(String data) {
        SQLiteDatabase db = this.getReadableDatabase();
        Map<String, Integer> podsumowanie = new HashMap<>();

        // Start pomiaru czasu
        long startTime = System.nanoTime();

        String query = "SELECT SUM(kalorie) as sumaKalorii, SUM(bialka) as sumaBialek, SUM(tluszcze) as sumaTluszczy, SUM(weglowodany) as sumaWegli FROM posilki WHERE data_spozycia=?";
        Cursor cursor = db.rawQuery(query, new String[]{data});

        if (cursor.moveToFirst()) {
            podsumowanie.put("kalorie",cursor.getInt(cursor.getColumnIndexOrThrow("sumaKalorii")));
            podsumowanie.put("bialka",cursor.getInt(cursor.getColumnIndexOrThrow("sumaBialek")));
            podsumowanie.put("tluszcze",cursor.getInt(cursor.getColumnIndexOrThrow("sumaTluszczy")));
            podsumowanie.put("weglowodany",cursor.getInt(cursor.getColumnIndexOrThrow("sumaWegli")));
        }
        cursor.close();
        db.close();

        // Koniec pomiaru czasu
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000; // Konwersja do ms
        Log.d("TEST_SQLITE", "Czas pobierania podsumowania dnia: " + duration + " ms");
        return podsumowanie;

    }
    public Map<String, Integer> podsumowanieZakres(String dataPoczatek, String dataKoniec){
     SQLiteDatabase db=this.getReadableDatabase();
     Map <String,Integer> podsumowanie=new HashMap<>();

     String query="SELECT SUM(kalorie) as sumaKalorii, SUM(bialka) as sumaBialek, SUM(tluszcze) as sumaTluszczy, SUM(weglowodany) as sumaWegli FROM posilki WHERE data_spozycia BETWEEN ? AND ?";
     Cursor cursor=db.rawQuery(query,new String[]{dataPoczatek, dataKoniec});
        if (cursor.moveToFirst()) {
            podsumowanie.put("kalorie",cursor.getInt(cursor.getColumnIndexOrThrow("sumaKalorii")));
            podsumowanie.put("bialka",cursor.getInt(cursor.getColumnIndexOrThrow("sumaBialek")));
            podsumowanie.put("tluszcze",cursor.getInt(cursor.getColumnIndexOrThrow("sumaTluszczy")));
            podsumowanie.put("weglowodany",cursor.getInt(cursor.getColumnIndexOrThrow("sumaWegli")));
        }
        cursor.close();
        db.close();
        return podsumowanie;
    }
}





