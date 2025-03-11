package com.example.inzynierkav3.adaptery;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inzynierkav3.R;
import com.example.inzynierkav3.aktywnosci.MainActivity;
import com.example.inzynierkav3.database.DatabaseHelper;
import com.example.inzynierkav3.modele.Produkt;

import java.util.List;

public class ProduktyAdapter extends RecyclerView.Adapter<ProduktyAdapter.ProduktViewHolder> {

    public interface OnProduktClickListener {
        void OnProduktClick(Produkt produkt);
    }

    private List<Produkt> produktyList;
    private OnProduktClickListener listener;
    private int typPosilku;
    private Context context;
    private DatabaseHelper dbHelper;

    // Konstruktor adaptera
    public ProduktyAdapter(Context context, List<Produkt> produktyList, int typPosilku, OnProduktClickListener listener) {
        this.context = context;
        this.produktyList = produktyList;
        this.typPosilku = typPosilku;
        this.listener = listener;
        this.dbHelper = new DatabaseHelper(context); // Przechowujemy instancję DatabaseHelper
    }

    @NonNull
    @Override
    public ProduktViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produkt, parent, false);
        return new ProduktViewHolder(view);
    }

    // Ustawienie zawartości widoków na podstawie pozycji w danych
    @Override
    public void onBindViewHolder(@NonNull ProduktViewHolder holder, int position) {
        Produkt produkt = produktyList.get(position);

        holder.nazwaTextView.setText(produkt.getNazwa());
        holder.kalorieTextView.setText("Kcal: " + produkt.getKalorie());
        holder.bialkaTextView.setText(produkt.getBialka() + " B");
        holder.tlusczeTextView.setText(produkt.getTluszcze() + " T");
        holder.weglowodanyTextView.setText(produkt.getWeglowodany() + " W");
        holder.itemView.setOnClickListener(view -> listener.OnProduktClick(produkt));

        if (context instanceof MainActivity) {
            holder.imgbtnUsunProdukt.setVisibility(View.VISIBLE);
        } else {
            holder.imgbtnUsunProdukt.setVisibility(View.GONE);
        }

        holder.imgbtnUsunProdukt.setOnClickListener(view -> {

            dbHelper.usunPosilek(produkt.getId(), typPosilku);
            produktyList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, produktyList.size());
            Toast.makeText(context, "Produkt usunięty", Toast.LENGTH_SHORT).show();
            ((MainActivity) context).sumowanie();
        });
    }

    // Zwraca liczbę elementów w liście
    @Override
    public int getItemCount() {
        return produktyList.size();
    }

    // Aktualizuje listę produktów i odświeża widok
    public void updateList(List<Produkt> newList) {
        produktyList.clear();
        produktyList.addAll(newList);
        notifyDataSetChanged();
    }

    // Klasa ViewHolder przechowuje odniesienia do widoków elementów listy
    public class ProduktViewHolder extends RecyclerView.ViewHolder {
        TextView nazwaTextView, kalorieTextView, bialkaTextView, tlusczeTextView, weglowodanyTextView;
        ImageButton imgbtnUsunProdukt;

        public ProduktViewHolder(@NonNull View itemView) {
            super(itemView);
            nazwaTextView = itemView.findViewById(R.id.textViewNazwaProduktu);
            kalorieTextView = itemView.findViewById(R.id.textViewKalorie);
            bialkaTextView = itemView.findViewById(R.id.textViewBialka);
            tlusczeTextView = itemView.findViewById(R.id.textViewTluszcze);
            weglowodanyTextView = itemView.findViewById(R.id.textViewWeglowodany);
            imgbtnUsunProdukt = itemView.findViewById(R.id.btn_usun_produkt);
        }
    }
}
