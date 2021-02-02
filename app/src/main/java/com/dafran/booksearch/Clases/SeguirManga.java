package com.dafran.booksearch.Clases;

public class SeguirManga {
    private int id;
    private String nombre;
    private String url;
    private String urlImagen;
    private String contador;
    private int valorSeguir;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContador() {
        return contador;
    }

    public void setContador(String contador) {
        this.contador = contador;
    }

    public int getValorSeguir() {
        return valorSeguir;
    }

    public void setValorSeguir(int valorSeguir) {
        this.valorSeguir = valorSeguir;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public SeguirManga(){}

    public SeguirManga(int id, String nombre, String url, String urlImagen,String contador, int valorSeguir) {
        this.id = id;
        this.nombre = nombre;
        this.url = url;
        this.urlImagen = urlImagen;
        this.contador = contador;
        this.valorSeguir = valorSeguir;
    }
}
