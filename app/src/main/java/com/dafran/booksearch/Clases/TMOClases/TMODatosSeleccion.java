package com.dafran.booksearch.Clases.TMOClases;

public class TMODatosSeleccion {
    private String numeroCapitulo;
    private String urlCapitulo;
    private String tipo;

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

    public TMODatosSeleccion(String numeroCapitulo, String urlCapitulo) {
        this.numeroCapitulo = numeroCapitulo;
        this.urlCapitulo = urlCapitulo;
    }
}
