package com.dafran.booksearch.Clases.Trantor;

public class TrantorBookDetail {
    private String titulo;
    private String urlImage;
    private String autor;
    private String idioma;
    private String descripcion;
    private String urlDescarga;
    private String urlLector;

    public TrantorBookDetail(String titulo, String urlImage, String autor,
                             String idioma, String descripcion) {
        this.titulo = titulo;
        this.urlImage = urlImage;
        this.autor = autor;
        this.idioma = idioma;
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrlDescarga() {
        return urlDescarga;
    }

    public void setUrlDescarga(String urlDescarga) {
        this.urlDescarga = urlDescarga;
    }

    public String getUrlLector() {
        return urlLector;
    }

    public void setUrlLector(String urlLector) {
        this.urlLector = urlLector;
    }
}
