package com.example.inzynierkav3.adaptery;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inzynierkav3.R;
import com.example.inzynierkav3.modele.ProduktZakupy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListaZakupowAdapter extends RecyclerView.Adapter<ListaZakupowAdapter.ViewHolder> {

    private final List<ProduktZakupy> produkty;
    private final OnItemClickListener listener;
    private final SharedPreferences preferences;
    private static final String PREFS_KEY = "zakupione_produkty";

    // Konstruktor adaptera (dodajemy SharedPreferences)
    public ListaZakupowAdapter(Context context, List<ProduktZakupy> produkty, OnItemClickListener listener) {
        this.produkty = produkty;
        this.listener = listener;
        this.preferences = context.getSharedPreferences("ZakupyPrefs", Context.MODE_PRIVATE);
        loadPurchasedItems(); // Wczytanie danych po uruchomieniu
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_zakupow, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProduktZakupy produkt = produkty.get(position);

        // Ustawienie nazwy produktu
        holder.textViewNazwa.setText(produkt.getNazwa());

        // Sprawdzenie, czy produkt był wcześniej oznaczony jako zakupiony
        holder.checkBoxZakupione.setChecked(produkt.isZakupiony());

        // Obsługa kliknięcia w checkbox - zmiana statusu
        holder.checkBoxZakupione.setOnCheckedChangeListener((buttonView, isChecked) -> {
            produkt.setZakupiony(isChecked);
            listener.onItemCheck(position);
            savePurchasedItems();
        });

        holder.buttonUsun.setOnClickListener(v -> {
            produkty.remove(position);
            notifyDataSetChanged();
            savePurchasedItems();
        });
    }

    @Override
    public int getItemCount() {
        return produkty.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNazwa;
        CheckBox checkBoxZakupione;
        ImageButton buttonUsun;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNazwa = itemView.findViewById(R.id.textViewNazwa);
            checkBoxZakupione = itemView.findViewById(R.id.checkBoxZakupione);
            buttonUsun = itemView.findViewById(R.id.buttonUsun);
        }
    }

    // Interfejs do obsługi kliknięć w elementy listy
    public interface OnItemClickListener {
        void onItemCheck(int position);
        void onItemDelete(int position);
    }

    private void savePurchasedItems() {
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> zakupione = new HashSet<>();
        for (ProduktZakupy produkt : produkty) {
            if (produkt.isZakupiony()) {
                zakupione.add(produkt.getNazwa());
            }
        }
        editor.putStringSet(PREFS_KEY, zakupione);
        editor.apply();
    }

    private void loadPurchasedItems() {
        Set<String> zakupione = preferences.getStringSet(PREFS_KEY, new HashSet<>());
        for (ProduktZakupy produkt : produkty) {
            produkt.setZakupiony(zakupione.contains(produkt.getNazwa()));
        }
    }
}
