package com.dafran.booksearch.SQLite;

public class PaginasTabla {
    public static final String DB_NAME = "booksearch.db";//nombre de la bd
    public static final int DB_VERSION = 1;//version


    //TABLA CLIENTES
    public static final String TABLA_PAGINAS = "paginas";

    //Columnas
    public static final String PAGINA_ID = "idPagina";
    public static final String PAGINA_NOMBRE = "nombrePagina";

    //string del create
    public static final String TABLA_PAGINA_SQL =
            "CREATE TABLE  " + TABLA_PAGINAS + "(" +
                    PAGINA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    PAGINA_NOMBRE + " TEXT NOT NULL);" ;
}
