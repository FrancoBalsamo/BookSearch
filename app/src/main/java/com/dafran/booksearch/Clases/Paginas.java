package com.dafran.booksearch.Clases;

public class Paginas {
    public int id;
    public String pagina;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPagina() {
        return pagina;
    }

    public void setPagina(String pagina) {
        this.pagina = pagina;
    }

    public Paginas(){}

    public Paginas(int id, String pagina) {
        this.id = id;
        this.pagina = pagina;
    }
}
