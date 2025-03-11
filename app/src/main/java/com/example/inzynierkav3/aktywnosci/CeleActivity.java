package com.example.inzynierkav3.aktywnosci;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inzynierkav3.R;

public class CeleActivity extends AppCompatActivity {

    private int waga;
    private int wagaDocelowa;
    private int wzrost;
    private int wiek;
    public String plec;
    private String aktywnosc_fizyczna;
    private float zmianaWagi;
    private float wspolczynnik_aktywnosci=1.55f;

    // Klucz dla SharedPreferences
    private final String PREFS_NAME = "UserPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cele);

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        wiek = sharedPreferences.getInt("wiek",25);
        wzrost = sharedPreferences.getInt("wzrost", 180);
        waga = sharedPreferences.getInt("waga", 70);
        wagaDocelowa = sharedPreferences.getInt("waga_docelowa", 75);
        zmianaWagi = sharedPreferences.getFloat("zmiana_wagi", 0.5f);

        plec = sharedPreferences.getString("plec", "Kobieta");
        aktywnosc_fizyczna = sharedPreferences.getString("aktywnosc", "Średnia");

        ImageButton imgbtnInformacje=findViewById(R.id.btn_informacje);
        ImageButton imgbtnMasa = findViewById(R.id.btn_masa);
        ImageButton imgbtnMasaTempo = findViewById(R.id.btn_masa_Tempo);
        ImageButton imgbtnAktywnoscFizyczna = findViewById(R.id.btn_aktywnosc_fizyczna);
        ImageButton imgbtnBack = findViewById(R.id.btn_back);
        Button btn_Zapisz=findViewById(R.id.btn_zapisz);

        TextView textViewwzrost = findViewById(R.id.tv_wzrostinfo);
        TextView textViewwiek= findViewById(R.id.tv_wiekinfo);
        TextView textViewplec=findViewById(R.id.tv_plecinfo);
        TextView textViewMasa = findViewById(R.id.tv_masa);
        TextView textViewZmianaWagi= findViewById(R.id.tv_zmiana_wagi);
        TextView textViewAktywnosc=findViewById(R.id.tv_aktywnosc);
        TextView textViewZeroKcal=findViewById(R.id.tv_zero_kcal);
        TextView textViewNadwyzkaKcal=findViewById(R.id.tv_nazdwyzka_kcal);
        textViewwzrost.setText("wzrost: "+ Integer.toString(wzrost)+" cm");
        textViewwiek.setText("wiek: "+Integer.toString(wiek));
        textViewplec.setText("płeć: "+plec);
        textViewMasa.setText(Integer.toString(waga) + " kg → " + Integer.toString(wagaDocelowa) + " kg");
        textViewZmianaWagi.setText(Float.toString(zmianaWagi) + " kg/tyg ");
        textViewAktywnosc.setText(aktywnosc_fizyczna);
        int cpm = CalkowitaPrzemianaMaterii(plec, wiek, wzrost, waga, wspolczynnik_aktywnosci);
        int nadwyzka = (int) (zmianaWagi * 1000);
        int calkowitezapotrzebowanie=cpm+nadwyzka;

        //  procentowe zapotrzebowanie na makroskładniki (w kcal)
        int zapotrzebowanieBialkoKcal = (int) (calkowitezapotrzebowanie * 0.2);
        int zapotrzebowanieTluszczeKcal = (int) (calkowitezapotrzebowanie * 0.3);
        int zapotrzebowanieWeglowodanyKcal =(int) (calkowitezapotrzebowanie*0.5);
        //  kcal na gramy dla każdego makroskładnika
        int zapotrzebowanieBialkoGramy = zapotrzebowanieBialkoKcal / 4;
        int zapotrzebowanieTluszczeGramy = zapotrzebowanieTluszczeKcal / 9;
        int zapotrzebowanieWeglowodanyGramy = zapotrzebowanieWeglowodanyKcal / 4;
        // Zapisz zapotrzebowanie makroskładników w SharedPreferences
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt("bialkoGramy", zapotrzebowanieBialkoGramy);
        editor.putInt("tluszczeGramy", zapotrzebowanieTluszczeGramy);
        editor.putInt("weglowodanyGramy", zapotrzebowanieWeglowodanyGramy);
        editor.putInt("calkowitezapotrzebowaniekcal", calkowitezapotrzebowanie);
        editor.apply(); // Zapisz zmiany

        if (aktywnosc_fizyczna != null) {
            switch (aktywnosc_fizyczna) {
                case "Brak":
                    wspolczynnik_aktywnosci = 1.2f;
                    break;
                case "Niska":
                    wspolczynnik_aktywnosci = 1.375f;
                    break;
                case "Średnia":
                    wspolczynnik_aktywnosci = 1.55f;
                    break;
                case "Wysoka":
                    wspolczynnik_aktywnosci = 1.725f;
                    break;
                case "Bardzo wysoka":
                    wspolczynnik_aktywnosci = 1.9f;
                    break;
                default:
                    wspolczynnik_aktywnosci = 1.55f; // Domyślny współczynnik, jeśli wartość jest inna niż oczekiwano
                    break;
            }
        }

        //oblsuga buttonow
        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CeleActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        imgbtnInformacje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CeleActivity.this, PodstawoweInformacjeActivity.class);
                startActivity(intent);
            }
        });

        imgbtnMasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CeleActivity.this, MasaActivity.class);
                startActivity(intent);
            }
        });
        imgbtnMasaTempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CeleActivity.this, MasaZmianaActivity.class);
                startActivity(intent);

            }
        });

        imgbtnAktywnoscFizyczna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CeleActivity.this, AktywnoscFizycznaActivity.class);
                startActivity(intent);

            }
        });
        btn_Zapisz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CeleActivity.this,"wprowadzono dane",Toast.LENGTH_LONG).show();
                textViewZeroKcal.setText(String.valueOf(cpm)+ " kcal");
                textViewNadwyzkaKcal.setText(String.valueOf(calkowitezapotrzebowanie)+"kcal");
            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        super.onBackPressed();
    }


    public int CalkowitaPrzemianaMaterii(String plec, int wiek, int wzrost, int waga, float aktywnosc) {

        double CPM=0.0;
        if (plec.equals("Mężczyzna")) {
            CPM = (88.362 + (13.397 * waga) + (4.799 * wzrost) - (5.677 * wiek))*aktywnosc;
        } else if (plec.equals("Kobieta")) {
            CPM = (447.593 + (9.247 * waga) + (3.098 * wzrost) - (4.330 * wiek))*aktywnosc;
        }
        return (int)CPM;
    }

}
