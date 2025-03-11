package com.example.inzynierkav3.modele;

public class ProduktZakupy {
    private String nazwa;
    private boolean zakupiony;

    public ProduktZakupy(String nazwa, boolean zakupiony) {
        this.nazwa = nazwa;
        this.zakupiony = zakupiony;
    }

    public String getNazwa() {return nazwa;}

    public void setNazwa(String nazwa) {this.nazwa = nazwa;}

    public boolean isZakupiony() {return zakupiony;}

    public void setZakupiony(boolean zakupiony) {this.zakupiony = zakupiony;}
}

