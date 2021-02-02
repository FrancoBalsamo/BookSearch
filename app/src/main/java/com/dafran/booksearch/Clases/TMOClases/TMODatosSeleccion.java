package com.dafran.booksearch.Clases.TMOClases;

public class TMODatosSeleccion {
    private String numeroCapitulo;
    private String urlCapitulo;
    private String urlImagen;

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

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public TMODatosSeleccion(String numeroCapitulo, String urlCapitulo, String urlImagen) {
        this.numeroCapitulo = numeroCapitulo;
        this.urlCapitulo = urlCapitulo;
        this.urlImagen = urlImagen;
    }
}
