package com.dafran.booksearch.Clases.TMOClases;

public class TMOItems {
    private String imgUrl;
    private String nombre;
    private String detalleUrl;
    private String tipo;

    public TMOItems(String imgUrl, String nombre, String detalleUrl, String tipo) {
        this.imgUrl = imgUrl;
        this.nombre = nombre;
        this.detalleUrl = detalleUrl;
        this.tipo = tipo;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDetalleUrl() {
        return detalleUrl;
    }

    public void setDetalleUrl(String detalleUrl) {
        this.detalleUrl = detalleUrl;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
