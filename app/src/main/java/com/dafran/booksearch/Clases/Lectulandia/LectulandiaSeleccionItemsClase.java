package com.dafran.booksearch.Clases.Lectulandia;

public class LectulandiaSeleccionItemsClase {
    private String titulo;
    private String imgUrl;
    private String autor;
    private String generos;
    private String sinopsis;
    private String urlDescarga;

    public LectulandiaSeleccionItemsClase(String titulo, String imgUrl,
                                          String autor, String generos,
                                          String sinopsis,
                                          String urlDescarga) {
        this.titulo = titulo;
        this.imgUrl = imgUrl;
        this.autor = autor;
        this.generos = generos;
        this.sinopsis = sinopsis;
        this.urlDescarga = urlDescarga;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getGeneros() {
        return generos;
    }

    public void setGeneros(String generos) {
        this.generos = generos;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getUrlDescarga() {
        return urlDescarga;
    }

    public void setUrlDescarga(String urlDescarga) {
        this.urlDescarga = urlDescarga;
    }
}
