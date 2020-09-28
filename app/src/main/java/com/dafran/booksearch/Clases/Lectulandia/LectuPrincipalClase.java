package com.dafran.booksearch.Clases.Lectulandia;

public class LectuPrincipalClase {
    private String imagenUrl;
    private String titulo;
    private String urlLink;

    public LectuPrincipalClase(String imagenUrl, String titulo, String urlLink) {
        this.imagenUrl = imagenUrl;
        this.titulo = titulo;
        this.urlLink = urlLink;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrlLink() {
        return urlLink;
    }

    public void setUrlLink(String urlLink) {
        this.urlLink = urlLink;
    }
}
