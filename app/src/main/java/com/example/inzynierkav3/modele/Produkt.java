package com.example.inzynierkav3.modele;

public class Produkt {
    private int id;
    private String nazwa;
    private int kalorie;
    private int bialka;
    private int tluszcze;
    private int weglowodany;

    public Produkt(int id, String nazwa, int kalorie, int bialka, int tluszcze, int weglowodany) {
        this.id = id;
        this.nazwa = nazwa;
        this.kalorie = kalorie;
        this.bialka = bialka;
        this.tluszcze = tluszcze;
        this.weglowodany = weglowodany;
    }

    public Produkt(String nazwa, int kalorie, int bialka, int tluszcze, int weglowodany) {
        this.nazwa = nazwa;
        this.kalorie = kalorie;
        this.bialka = bialka;
        this.tluszcze = tluszcze;
        this.weglowodany = weglowodany;
    }

    public int getId() {return id;}
    public String getNazwa() {return nazwa;}
    public int getKalorie() {return kalorie;}
    public int getBialka() {return bialka;}
    public int getTluszcze() {return tluszcze;}
    public int getWeglowodany() {return weglowodany;}
}
