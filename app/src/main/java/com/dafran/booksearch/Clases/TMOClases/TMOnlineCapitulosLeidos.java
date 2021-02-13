package com.dafran.booksearch.Clases.TMOClases;

public class TMOnlineCapitulosLeidos {
    private int id;
    private String nombre_manga;
    private int leido;
    private String nombre_capitulo_leido;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre_manga() {
        return nombre_manga;
    }

    public void setNombre_manga(String nombre_manga) {
        this.nombre_manga = nombre_manga;
    }

    public int getLeido() {
        return leido;
    }

    public void setLeido(int leido) {
        this.leido = leido;
    }

    public String getNombre_capitulo_leido() {
        return nombre_capitulo_leido;
    }

    public void setNombre_capitulo_leido(String nombre_capitulo_leido) {
        this.nombre_capitulo_leido = nombre_capitulo_leido;
    }

    public TMOnlineCapitulosLeidos(String nombre_manga, int leido, String nombre_capitulo_leido) {
        this.nombre_manga = nombre_manga;
        this.leido = leido;
        this.nombre_capitulo_leido = nombre_capitulo_leido;
    }

    public TMOnlineCapitulosLeidos(){}
}
