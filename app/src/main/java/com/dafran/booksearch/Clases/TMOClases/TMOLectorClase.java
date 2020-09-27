package com.dafran.booksearch.Clases.TMOClases;

import com.dafran.booksearch.Activities.TMO.TMOLector;

public class TMOLectorClase {
    private String img;
    private String urlRedireccionada;

    public String getUrlRedireccionada() {
        return urlRedireccionada;
    }

    public void setUrlRedireccionada(String urlRedireccionada) {
        this.urlRedireccionada = urlRedireccionada;
    }

    public TMOLectorClase(String img) {
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
