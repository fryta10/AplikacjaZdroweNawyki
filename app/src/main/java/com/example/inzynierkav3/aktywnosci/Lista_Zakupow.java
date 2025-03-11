package com.example.inzynierkav3.aktywnosci;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inzynierkav3.R;
import com.example.inzynierkav3.adaptery.ListaZakupowAdapter;
import com.example.inzynierkav3.modele.ProduktZakupy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Lista_Zakupow extends AppCompatActivity implements ListaZakupowAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private ListaZakupowAdapter adapter;
    private List<ProduktZakupy> listaZakupow;
    private SharedPreferences preferences;
    private static final String PREFS_KEY = "shopping_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_zakupow);


        preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        listaZakupow = loadShoppingList();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ListaZakupowAdapter(this, listaZakupow, this);
        recyclerView.setAdapter(adapter);

        ImageButton buttonBack = findViewById(R.id.button_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        EditText editTextProduct = findViewById(R.id.editTextProduct);
        Button buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(v -> {
            String productName = editTextProduct.getText().toString().trim();
            if (!productName.isEmpty()) {
                listaZakupow.add(new ProduktZakupy(productName, false));
                adapter.notifyItemInserted(listaZakupow.size() - 1);
                saveShoppingList();
                editTextProduct.setText("");
            }
        });
    }

    // Zapisuje listę produktów do SharedPreferences
    private void saveShoppingList() {
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> set = new HashSet<>();
        for (ProduktZakupy produkt : listaZakupow) {
            set.add(produkt.getNazwa() + ":" + produkt.isZakupiony());
        }
        editor.putStringSet(PREFS_KEY, set);
        editor.apply();
    }

    // Wczytuje listę produktów z SharedPreferences
    private List<ProduktZakupy> loadShoppingList() {
        Set<String> set = preferences.getStringSet(PREFS_KEY, new HashSet<>());
        List<ProduktZakupy> produkty = new ArrayList<>();
        for (String entry : set) {
            String[] parts = entry.split(":");
            if (parts.length == 2) {
                String nazwa = parts[0];
                boolean zakupiony = Boolean.parseBoolean(parts[1]);
                produkty.add(new ProduktZakupy(nazwa, zakupiony));
            }
        }
        return produkty;
    }

    // Obsługa kliknięcia checkboxa (zmiana statusu zakupionego produktu)
    @Override
    public void onItemCheck(int position) {
        saveShoppingList();
    }

    // Obsługa usunięcia produktu
    @Override
    public void onItemDelete(int position) {
        listaZakupow.remove(position);
        adapter.notifyItemRemoved(position);
        saveShoppingList();
    }
}
