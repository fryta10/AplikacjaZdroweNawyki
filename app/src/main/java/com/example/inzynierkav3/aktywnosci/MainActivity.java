package com.example.inzynierkav3.aktywnosci;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.Map;
import java.time.format.DateTimeFormatter;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.inzynierkav3.R;
import com.example.inzynierkav3.adaptery.ProduktyAdapter;
import com.example.inzynierkav3.database.DatabaseHelper;
import com.example.inzynierkav3.modele.Produkt;
import com.google.android.material.navigation.NavigationView;
import java.time.LocalDate;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private DatabaseHelper dbHelper;
    private LocalDate wybranaData;
    private TextView tvSumaKalorii, tvSumaBialek, tvSumaTlusczy, tvSumaWegli,tvWybranaData;
    private TextView tvsumaKaloriiSniadanie, tvsumaBialekSniadanie, tvsumaTluszczySniadanie, tvsumaWegliSniadanie;
    private TextView tvsumaKaloriiObiad, tvsumaBialekObiad, tvsumaTluszczyObiad, tvsumaWegliObiad;
    private TextView tvsumaKaloriiPrzekaska, tvsumaBialekPrzekaska, tvsumaTluszczyPrzekaska, tvsumaWegliPrzekaska;
    private TextView tvsumaKaloriiKolacja, tvsumaBialekKolacja, tvsumaTluszczyKolacja, tvsumaWegliKolacja;
    private TextView tvKalorieProcent, tvBialkoProcent, tvTluszczeProcent, tvWeglowodanyProcent;

    private ProgressBar progressBarKalorie, progressBarBialko, progressBarTluszcze, progressBarWeglowodany;
    private int kalorieZapotrzebowanie, bialkoZapotrzebowanie, tluszczeZapotrzebowanie, weglowodanyZapotrzebowanie;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Inicjalizacja widoków
        initViews();

        // Pobieramy zapotrzebowanie makroskładników z SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        kalorieZapotrzebowanie= sharedPreferences.getInt("calkowitezapotrzebowaniekcal",2000);
        bialkoZapotrzebowanie = sharedPreferences.getInt("bialkoGramy", 70); //
         tluszczeZapotrzebowanie = sharedPreferences.getInt("tluszczeGramy", 75);
         weglowodanyZapotrzebowanie = sharedPreferences.getInt("weglowodanyGramy", 250);

        setupNavigation(); // Konfiguracja przycisków nawigacyjnych

        // Inicjalizacja bazy danych
        dbHelper = new DatabaseHelper(this);
        wybranaData = LocalDate.now();
        AktualizacjaWidokow();

        setupButtons();
    }


    private void initViews() {
        // Inicjalizacja tekstów i pasków postępu
        tvSumaKalorii = findViewById(R.id.tv_spozyte_kalorie);
        tvSumaBialek = findViewById(R.id.tv_spozyte_bialka);
        tvSumaTlusczy = findViewById(R.id.tv_spozyte_tluszcze);
        tvSumaWegli = findViewById(R.id.tv_spozyte_weglowodany);
        tvWybranaData=findViewById(R.id.textView_wybrana_data2);

        progressBarKalorie = findViewById(R.id.progress_kalorie);
        progressBarBialko = findViewById(R.id.progress_bialka);
        progressBarTluszcze = findViewById(R.id.progress_tluszcze);
        progressBarWeglowodany = findViewById(R.id.progress_weglowodany);

        tvKalorieProcent = findViewById(R.id.tv_kalorie_procent);
        tvBialkoProcent = findViewById(R.id.tv_bialka_procent);
        tvTluszczeProcent = findViewById(R.id.tv_tluszcze_procent);
        tvWeglowodanyProcent = findViewById(R.id.tv_weglowodany_procent);
        tvsumaKaloriiSniadanie = findViewById(R.id.tv_kcal_Sniadanie);
        tvsumaBialekSniadanie = findViewById(R.id.tv_B_Sniadanie);
        tvsumaTluszczySniadanie = findViewById(R.id.tv_T_Sniadanie);
        tvsumaWegliSniadanie = findViewById(R.id.tv_W_Sniadanie);

        tvsumaKaloriiObiad = findViewById(R.id.tv_kcal_Obiad);
        tvsumaBialekObiad = findViewById(R.id.tv_B_Obiad);
        tvsumaTluszczyObiad = findViewById(R.id.tv_T_Obiad);
        tvsumaWegliObiad = findViewById(R.id.tv_W_Obiad);

        tvsumaKaloriiPrzekaska = findViewById(R.id.tv_kcal_Przekaska);
        tvsumaBialekPrzekaska = findViewById(R.id.tv_B_Przekaska);
        tvsumaTluszczyPrzekaska = findViewById(R.id.tv_T_Przekaska);
        tvsumaWegliPrzekaska = findViewById(R.id.tv_W_Przekaska);

        tvsumaKaloriiKolacja = findViewById(R.id.tv_kcal_Kolacja);
        tvsumaBialekKolacja = findViewById(R.id.tv_B_Kolacja);
        tvsumaTluszczyKolacja = findViewById(R.id.tv_T_Kolacja);
        tvsumaWegliKolacja = findViewById(R.id.tv_W_Kolacja);
    }

    private void setupNavigation() {
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }
    private void setupButtons(){
        // Znalezienie przycisków
        setupButton(R.id.btn_dodaj_sniadanie, 1);
        setupButton(R.id.btn_dodaj_obiad, 2);
        setupButton(R.id.btn_dodaj_przekaska, 3);
        setupButton(R.id.btn_dodaj_kolacja, 4);

        Button btnWczoraj = findViewById(R.id.btn_wczoraj);
        Button btnDzisiaj = findViewById(R.id.btn_dzisiaj);
        Button btnJutro = findViewById(R.id.btn_jutro);
        ImageButton imgbtnKalendarz= findViewById(R.id.btn_kalendarz);

        btnWczoraj.setOnClickListener(view -> {
            LocalDate dzis=LocalDate.now();
            wybranaData=dzis.minusDays(1);
            AktualizacjaWidokow();
        });
        btnDzisiaj.setOnClickListener(view -> {
            LocalDate dzis=LocalDate.now();
            wybranaData=dzis;
            AktualizacjaWidokow();
        });
        btnJutro.setOnClickListener(view -> {
            LocalDate dzis=LocalDate.now();
            wybranaData=dzis.plusDays(1);
            AktualizacjaWidokow();
        });
        imgbtnKalendarz.setOnClickListener(view -> {
            int rok = wybranaData.getYear();
            int miesiac = wybranaData.getMonthValue()-1;
            int dzien =wybranaData.getDayOfMonth();

            DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int wybranyrok, int wybranymiesiac, int wybranydzien) {

                    wybranaData = LocalDate.of(wybranyrok, wybranymiesiac + 1, wybranydzien);
                    AktualizacjaWidokow();
                    sumowanie();
                }
            },rok, miesiac,dzien);
            datePickerDialog.show();
        });
    }
    private void ustawTekstMakro(TextView tv, String prefix, int wartosc) {
        tv.setText(prefix + ": " + wartosc);
    }

    public void sumowanie() {

        String todayDate = wybranaData.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Map<String, Integer> podsumowanie = dbHelper.podsumowanieDnia(todayDate);
        int sumaKalorii=podsumowanie.get("kalorie");
        int sumabialek= podsumowanie.get("bialka");
        int sumatluszczy=podsumowanie.get("tluszcze");
        int sumawegli=podsumowanie.get("weglowodany");

        Map<String, Integer> podsumowanieSniadania =dbHelper.podsumowaniePosilku(todayDate, "1");
        ustawTekstMakro(tvsumaKaloriiSniadanie,"kcal",podsumowanieSniadania.get("kalorie"));
        ustawTekstMakro(tvsumaBialekSniadanie,"B",podsumowanieSniadania.get("bialka"));
        ustawTekstMakro(tvsumaTluszczySniadanie,"T",podsumowanieSniadania.get("tluszcze"));
        ustawTekstMakro(tvsumaWegliSniadanie,"W",podsumowanieSniadania.get("weglowodany"));

        Map<String, Integer> podsumowanieObiad = dbHelper.podsumowaniePosilku(todayDate, "2");
        ustawTekstMakro(tvsumaKaloriiObiad, "kcal", podsumowanieObiad.get("kalorie"));
        ustawTekstMakro(tvsumaBialekObiad, "B", podsumowanieObiad.get("bialka"));
        ustawTekstMakro(tvsumaTluszczyObiad, "T", podsumowanieObiad.get("tluszcze"));
        ustawTekstMakro(tvsumaWegliObiad, "W", podsumowanieObiad.get("weglowodany"));

        Map<String, Integer> podsumowaniePrzekaska = dbHelper.podsumowaniePosilku(todayDate, "3");
        ustawTekstMakro(tvsumaKaloriiPrzekaska, "kcal", podsumowaniePrzekaska.get("kalorie"));
        ustawTekstMakro(tvsumaBialekPrzekaska, "B", podsumowaniePrzekaska.get("bialka"));
        ustawTekstMakro(tvsumaTluszczyPrzekaska, "T", podsumowaniePrzekaska.get("tluszcze"));
        ustawTekstMakro(tvsumaWegliPrzekaska, "W", podsumowaniePrzekaska.get("weglowodany"));

        Map<String, Integer> podsumowanieKolacja = dbHelper.podsumowaniePosilku(todayDate, "4");
        ustawTekstMakro(tvsumaKaloriiKolacja, "kcal", podsumowanieKolacja.get("kalorie"));
        ustawTekstMakro(tvsumaBialekKolacja, "B", podsumowanieKolacja.get("bialka"));
        ustawTekstMakro(tvsumaTluszczyKolacja, "T", podsumowanieKolacja.get("tluszcze"));
        ustawTekstMakro(tvsumaWegliKolacja, "W", podsumowanieKolacja.get("weglowodany"));

        // Aktualizacja tekstów i pasków postępu
        ustawProgressBar(progressBarKalorie, tvSumaKalorii, tvKalorieProcent, sumaKalorii, kalorieZapotrzebowanie, "kcal");
        ustawProgressBar(progressBarBialko, tvSumaBialek, tvBialkoProcent, sumabialek, bialkoZapotrzebowanie, "g");
        ustawProgressBar(progressBarTluszcze, tvSumaTlusczy, tvTluszczeProcent, sumatluszczy, tluszczeZapotrzebowanie, "g");
        ustawProgressBar(progressBarWeglowodany, tvSumaWegli, tvWeglowodanyProcent, sumawegli, weglowodanyZapotrzebowanie, "g");
    }

    private void setupRecyclerView(int recyclerViewId, List<Produkt> produkty, int typPosilku) {
        RecyclerView recyclerView = findViewById(recyclerViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ProduktyAdapter adapter = new ProduktyAdapter(this, produkty, typPosilku, new ProduktyAdapter.OnProduktClickListener() {
            @Override
            public void OnProduktClick(Produkt produkt) {
                // Logika kliknięcia produktu, jeśli jest potrzebna
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void setupButton(int buttonId, int typPosilku) {
        ImageButton button = findViewById(buttonId);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, DodawanieProduktowActivity.class);
            intent.putExtra("Typ_posilku", typPosilku);
            intent.putExtra("WybranaData", wybranaData.toString());
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed(); // Normalne działanie - zamknięcie aplikacji
    }



    private void AktualizacjaWidokow() {
        String dataWybrana=wybranaData.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        tvWybranaData.setText(dataWybrana);
        List<Produkt> sniadanie = dbHelper.pobierzPosilkiNaDzien(1, wybranaData);
        List<Produkt> obiad = dbHelper.pobierzPosilkiNaDzien(2, wybranaData);
        List<Produkt> przekaska = dbHelper.pobierzPosilkiNaDzien(3, wybranaData);
        List<Produkt> kolacja = dbHelper.pobierzPosilkiNaDzien(4, wybranaData);
        setupRecyclerView(R.id.recyclerViewSniadanie, sniadanie, 1);
        setupRecyclerView(R.id.recyclerViewObiad, obiad, 2);
        setupRecyclerView(R.id.recyclerViewPrzekaska, przekaska, 3);
        setupRecyclerView(R.id.recyclerViewKolacja, kolacja, 4);
        sumowanie();
    }


    private void ustawProgressBar(ProgressBar progressBar, TextView tvSpozycie, TextView tvProcent, int spozycie, int zapotrzebowanie, String jednostka) {
        int procent = (int) ((float) spozycie / zapotrzebowanie * 100);
        progressBar.setProgress(procent);
        tvSpozycie.setText(spozycie + "/" + zapotrzebowanie + jednostka);
        tvProcent.setText(procent + "%");
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_dziennik) {
        } else if (id == R.id.nav_cele) {
            Intent intent = new Intent(this, CeleActivity.class);
            startActivity(intent);
        }else if (id==R.id.nav_podsumowanie){
            Intent intent=new Intent(this, PodsumowanieActivity.class );
            startActivity(intent);
        } else if (id==R.id.nav_zakupy) {
            Intent intent = new Intent(this, Lista_Zakupow.class);
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null)
                .commit();
    }
}
