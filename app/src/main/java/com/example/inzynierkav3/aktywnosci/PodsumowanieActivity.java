package com.example.inzynierkav3.aktywnosci;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.inzynierkav3.R;
import com.example.inzynierkav3.database.DatabaseHelper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;


public class PodsumowanieActivity extends AppCompatActivity {

    private PieChart wykresPie;
    private BarChart wykresBar;
    private DatabaseHelper dbhelper;
    private TextView kalorieCel, bialkaCel, tluszczeCel, weglowodanyCel, kalorieSpozyte, bialkaSpozyte, tluszczeSpozyte, weglowodanySpozyte;
    private TextView tv_wybranaData;
    private Button btnPodsumowanieDnia, btnPodsumowanieTygodnia, btnPodsumowanieMiesiaca;
    private LocalDate wybranaData;
    private ImageButton btnDataWstecz, btnDataNext;
    private int aktualnyTrybDaty; //1-dzienny 2=tygodniowt 3-miesięczny

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podsumowanie);

        ImageButton buttonBack = findViewById(R.id.button_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dbhelper=new DatabaseHelper(this);
        wybranaData=LocalDate.now();
        aktualnyTrybDaty=1;

        inicjalizujWidoki();
        wyswietlDate();
        obslugaButtonow();
        ustawSpozycieDzien();

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        super.onBackPressed(); // Wywołanie domyślnej obsługi
    }

    private void inicjalizujWidoki() {
        kalorieSpozyte = findViewById(R.id.kalorie_spozyte);
        bialkaSpozyte = findViewById(R.id.bialka_spozyte);
        tluszczeSpozyte = findViewById(R.id.tluszcze_spozyte);
        weglowodanySpozyte = findViewById(R.id.wegle_spozyte);
        tv_wybranaData=findViewById(R.id.textview_wybrana_data);

        btnPodsumowanieDnia = findViewById(R.id.button_podsumowanie_dnia);
        btnPodsumowanieTygodnia = findViewById(R.id.button_podsumowanie_tygodnia);
        btnPodsumowanieMiesiaca = findViewById(R.id.button_podsumowanie_miesiaca);
        btnDataWstecz=findViewById(R.id.button_data_wstecz);
        btnDataNext=findViewById(R.id.button_data_next);
        wykresPie=findViewById(R.id.wykres_pie);
        wykresBar=findViewById(R.id.wykres_bar);
    }
    private void obslugaButtonow(){

        btnDataNext.setOnClickListener(view -> zmienDate(true) );
        btnDataWstecz.setOnClickListener(view -> zmienDate(false));

        btnPodsumowanieDnia.setOnClickListener(view -> {
            aktualnyTrybDaty=1;
            wybranaData=LocalDate.now();
            wyswietlDate();
            ustawSpozycieDzien();
        });
        btnPodsumowanieTygodnia.setOnClickListener(view -> {
            aktualnyTrybDaty=2;
            wybranaData=LocalDate.now();
            wyswietlDate();
            ustawSpozycieTydzien();
        });
        btnPodsumowanieMiesiaca.setOnClickListener(view -> {
            aktualnyTrybDaty=3;
            wybranaData=LocalDate.now();
            wyswietlDate();
            ustawSpozycieMiesiac();
        });
    }
    private void przelaczWykresy(String typWykresu){
        if (typWykresu.equals("PieChart")){
            wykresBar.setVisibility(View.GONE);
            wykresPie.setVisibility(View.VISIBLE);
        } else if (typWykresu.equals("BarChart")) {
            wykresPie.setVisibility(View.GONE);
            wykresBar.setVisibility(View.VISIBLE);
        }
    }

    private void ustawSpozycieDzien() {

        // Start pomiaru czasu
        long startTime = System.nanoTime();
        przelaczWykresy("PieChart");
        String dzisiejszaData = wybranaData.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Map<String, Integer> spozycie = dbhelper.podsumowanieDnia(dzisiejszaData);

        int bialka = spozycie.getOrDefault("bialka", 0);
        int tluszcze = spozycie.getOrDefault("tluszcze", 0);
        int weglowodany = spozycie.getOrDefault("weglowodany", 0);

        float bialkaKalorie = bialka * 4;
        float tluszczeKalorie = tluszcze * 9;
        float weglowodanyKalorie = weglowodany * 4;
        float sumaKaloriiZMakro = bialkaKalorie + tluszczeKalorie + weglowodanyKalorie;

        int bialkaProcent = sumaKaloriiZMakro > 0 ? (int) ((bialkaKalorie / sumaKaloriiZMakro) * 100) : 0;
        int tluszczeProcent = sumaKaloriiZMakro > 0 ? (int) ((tluszczeKalorie / sumaKaloriiZMakro) * 100) : 0;
        int weglowodanyProcent = sumaKaloriiZMakro > 0 ? (int) ((weglowodanyKalorie / sumaKaloriiZMakro) * 100) : 0;

        // Koniec pomiaru czasu
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;
        Log.d("TEST_SQLITE", "Czas ładowania podsumowania dnia: " + duration + " ms");
        // Wyświetlanie danych
        int kalorie = (int) sumaKaloriiZMakro; // Wyświetlamy tylko kalorie z makroskładników
        kalorieSpozyte.setText(kalorie + " kcal");

        if (kalorie > 0) {
            bialkaSpozyte.setText(bialka + " g (" + bialkaProcent + "%)");
            tluszczeSpozyte.setText(tluszcze + " g (" + tluszczeProcent + "%)");
            weglowodanySpozyte.setText(weglowodany + " g (" + weglowodanyProcent + "%)");
        } else {
            bialkaSpozyte.setText("0 g (0%)");
            tluszczeSpozyte.setText("0 g (0%)");
            weglowodanySpozyte.setText("0 g (0%)");
        }
        pokazWykresKolowy(kalorie, bialka, tluszcze, weglowodany);
    }


    private void ustawSpozycieTydzien() {
        // Start pomiaru czasu
        long startTime = System.nanoTime();
        przelaczWykresy("BarChart");

        LocalDate poczatekTygodnia = wybranaData.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate koniecTygodnia = poczatekTygodnia.plusDays(6);

        // Przygotuj dane dla każdego dnia tygodnia
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>(); // Labels dostępne lokalnie

        LocalDate currentDay = poczatekTygodnia;
        int dayIndex = 0;

        // Koniec pomiaru czasu
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;
        Log.d("TEST_SQLITE", "Czas ładowania podsumowania tygodnia: " + duration + " ms");
        while (!currentDay.isAfter(koniecTygodnia)) {
            String formattedDate = currentDay.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            int dailyCalories = dbhelper.podsumowanieDnia(formattedDate).getOrDefault("kalorie", 0);

            entries.add(new BarEntry(dayIndex, dailyCalories));
            labels.add(currentDay.format(DateTimeFormatter.ofPattern("EEE", Locale.getDefault()))); // Dodaj dzień tygodnia
            currentDay = currentDay.plusDays(1);
            dayIndex++;
        }


        // Konfiguracja wykresu słupkowego
        BarDataSet dataSet = new BarDataSet(entries, "Kalorie");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.8f); // Ustaw szerokość słupków
        wykresBar.setData(barData);
        wykresBar.getDescription().setEnabled(false);
        wykresBar.getXAxis().setGranularity(1f); // Ustaw odstępy osi X
        wykresBar.invalidate();
        wykresBar.getXAxis().setDrawGridLines(false); // Wyłącz linie siatki pionowej
        wykresBar.getAxisLeft().setDrawGridLines(true); // Włącz linie siatki poziomej
        wykresBar.getAxisRight().setEnabled(false); // Wyłącz prawą oś Y
        wykresBar.getDescription().setEnabled(false); // Wyłączenie opisu
        wykresBar.getDescription().setText("Tygodniowe kalorie"); // Dodanie niestandardowego opisu
        wykresBar.getDescription().setTextSize(12f); // Rozmiar tekstu opisu

        // Oś X - Ustawienie dni tygodnia
        XAxis xAxis = wykresBar.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels)); // Przypisz etykiety dni tygodnia
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.DKGRAY);
        xAxis.setDrawGridLines(false);

        // Oś Y
        YAxis leftAxis = wykresBar.getAxisLeft();
        leftAxis.setGranularity(1f);
        leftAxis.setAxisMinimum(0);
        leftAxis.setTextSize(12f);
        leftAxis.setTextColor(Color.DKGRAY);
        wykresBar.getAxisRight().setEnabled(false);

        // Opis wykresu
        wykresBar.getDescription().setEnabled(false);
    }

    private void ustawSpozycieMiesiac() {
        // Start pomiaru czasu
        long startTime = System.nanoTime();
        przelaczWykresy("PieChart");

        LocalDate poczatekMiesiaca = wybranaData.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate koniecMiesiaca = wybranaData.with(TemporalAdjusters.lastDayOfMonth());
        Map<String, Integer> spozycie = dbhelper.podsumowanieZakres(
                poczatekMiesiaca.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                koniecMiesiaca.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );

        // Pobierz spożyte wartości
        int bialka = spozycie.getOrDefault("bialka", 0);
        int tluszcze = spozycie.getOrDefault("tluszcze", 0);
        int weglowodany = spozycie.getOrDefault("weglowodany", 0);

        // Oblicz kalorie z każdego makroskładnika
        float bialkaKalorie = bialka * 4;
        float tluszczeKalorie = tluszcze * 9;
        float weglowodanyKalorie = weglowodany * 4;

        // Oblicz sumę kalorii z makroskładników
        float sumaKaloriiZMakro = bialkaKalorie + tluszczeKalorie + weglowodanyKalorie;

        // Oblicz procenty
        int bialkaProcent = sumaKaloriiZMakro > 0 ? (int) ((bialkaKalorie / sumaKaloriiZMakro) * 100) : 0;
        int tluszczeProcent = sumaKaloriiZMakro > 0 ? (int) ((tluszczeKalorie / sumaKaloriiZMakro) * 100) : 0;
        int weglowodanyProcent = sumaKaloriiZMakro > 0 ? (int) ((weglowodanyKalorie / sumaKaloriiZMakro) * 100) : 0;

        // Koniec pomiaru czasu
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;
        Log.d("TEST_SQLITE", "Czas ładowania podsumowania miesiąca: " + duration + " ms");
        // Wyświetl dane
        int kalorie = (int) sumaKaloriiZMakro; // Wyświetlamy tylko kalorie z makroskładników
        kalorieSpozyte.setText(kalorie + " kcal");

        if (kalorie > 0) {
            bialkaSpozyte.setText(bialka + " g (" + bialkaProcent + "%)");
            tluszczeSpozyte.setText(tluszcze + " g (" + tluszczeProcent + "%)");
            weglowodanySpozyte.setText(weglowodany + " g (" + weglowodanyProcent + "%)");
        } else {
            bialkaSpozyte.setText("0 g (0%)");
            tluszczeSpozyte.setText("0 g (0%)");
            weglowodanySpozyte.setText("0 g (0%)");
        }

        // Zaktualizuj wykres
        pokazWykresKolowy(kalorie, bialka, tluszcze, weglowodany);
    }

    private void pokazWykresKolowy(int kalorie, int bialka, int tluszcze, int weglowodany) {
        // Start pomiaru czasu
        long startTime = System.nanoTime();
        ArrayList<PieEntry> entries = new ArrayList<>();

        if (kalorie > 0) {
            float bialkaKalorie = bialka * 4;
            float tluszczeKalorie = tluszcze * 9;
            float weglowodanyKalorie = weglowodany * 4;

            entries.add(new PieEntry(bialkaKalorie, "Białka"));
            entries.add(new PieEntry(tluszczeKalorie, "Tłuszcze"));
            entries.add(new PieEntry(weglowodanyKalorie, "Węglowodany"));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setSliceSpace(3f); // Odstępy między segmentami
        dataSet.setValueTextSize(14f); // Rozmiar tekstu dla wartości wewnątrz wykresu
        dataSet.setValueTextColor(Color.BLACK);

        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter(wykresPie));

        wykresPie.setData(pieData);
        wykresPie.setUsePercentValues(true); // Wyświetlanie wartości w procentach
        wykresPie.getDescription().setEnabled(false);
        wykresPie.setDrawHoleEnabled(true);
        wykresPie.setHoleRadius(40f);
        wykresPie.setTransparentCircleRadius(0f);
        wykresPie.setCenterText(kalorie + " kcal");
        wykresPie.setCenterTextSize(18f); //  rozmiar tekstu w otworze
        wykresPie.setDrawEntryLabels(false);

        wykresPie.invalidate(); // Odświeżenie wykresu

        // Ustawienia legendy pod wykresem
        Legend legenda = wykresPie.getLegend();
        legenda.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legenda.setWordWrapEnabled(true); // Umożliwia zawijanie legendy
        legenda.setDrawInside(false); // Pozycjonowanie legendy na zewnątrz wykresu
        legenda.setTextSize(14f); // Rozmiar tekstu legendy
        legenda.setXEntrySpace(15f); // Odstęp poziomy między elementami legendy
        legenda.setFormSize(14f); // Rozmiar kształtów w legendzie
        wykresPie.invalidate(); //
        // Koniec pomiaru czasu
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;
        Log.d("TEST_WYKRES", "Czas generowania wykresu: " + duration + " ms");
    }


    private void wyswietlDate(){
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd MM yyyy");
        switch (aktualnyTrybDaty){
            case 1:
                tv_wybranaData.setText(wybranaData.format(formatter));
                break;
            case 2:
                LocalDate poczatekTygodnia=wybranaData.with(WeekFields.of(DayOfWeek.MONDAY,1).dayOfWeek(),1);
                LocalDate koniecTygodnia=poczatekTygodnia.plusDays(6);
                tv_wybranaData.setText(poczatekTygodnia.format(formatter)+ " - " + koniecTygodnia.format(formatter));
                break;
            case 3:
                tv_wybranaData.setText(wybranaData.format(DateTimeFormatter.ofPattern("MM yyyy")));
                break;
        }
    }
    private void zmienDate(boolean next){
        switch (aktualnyTrybDaty){
            case 1:
                wybranaData=next ? wybranaData.plusDays(1) : wybranaData.minusDays(1);
                ustawSpozycieDzien();
                break;
            case 2:
                wybranaData=next ? wybranaData.plusWeeks(1) : wybranaData.minusWeeks(1);
                ustawSpozycieTydzien();
                break;
            case 3:
                wybranaData=next? wybranaData.plusMonths(1) : wybranaData.minusMonths(1);
               ustawSpozycieMiesiac();
                break;
        }
        wyswietlDate();

    }

}