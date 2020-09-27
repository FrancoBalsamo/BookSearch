package com.dafran.booksearch.Clases.Trantor;

public class TrantorItems {
    private String imgUrl;
    private String title;
    private String detailUrl;
    private String urlDescarga;
    private String idioma;

    public TrantorItems(String imgUrl, String title, String detailUrl, String urlDescarga) {
        this.imgUrl = imgUrl;
        this.title = title;
        this.detailUrl = detailUrl;
        this.urlDescarga = urlDescarga;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getUrlDescarga() {
        return urlDescarga;
    }

    public void setUrlDescarga(String urlDescarga) {
        this.urlDescarga = urlDescarga;
    }
    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }
}
