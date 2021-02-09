package com.dafran.booksearch.Clases.TMOClases;

public class TMODatosSeleccion {
    private String numeroCapitulo;
    private String urlCapitulo;
    private String nombreManga;

    public String getNumeroCapitulo() {
        return numeroCapitulo;
    }

    public void setNumeroCapitulo(String numeroCapitulo) {
        this.numeroCapitulo = numeroCapitulo;
    }

    public String getUrlCapitulo() {
        return urlCapitulo;
    }

    public void setUrlCapitulo(String urlCapitulo) {
        this.urlCapitulo = urlCapitulo;
    }

    public String getNombreManga() {
        return nombreManga;
    }

    public void setNombreManga(String nombreManga) {
        this.nombreManga = nombreManga;
    }

    public TMODatosSeleccion(String numeroCapitulo, String urlCapitulo, String nombreManga) {
        this.numeroCapitulo = numeroCapitulo;
        this.urlCapitulo = urlCapitulo;
        this.nombreManga = nombreManga;
    }
}
