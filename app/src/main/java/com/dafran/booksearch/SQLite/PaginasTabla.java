package com.dafran.booksearch.SQLite;

import java.net.URL;

public class PaginasTabla {
    public static final String DB_NAME = "booksearch0.db";//nombre de la bd
    public static final int DB_VERSION = 1;//version


    //TABLA
    public static final String TABLA_PAGINAS = "paginas";

    //Columnas
    public static final String PAGINA_ID = "idPagina";
    public static final String PAGINA_NOMBRE = "nombrePagina";

    //TABLA
    public static final String TABLA_SEGUIR = "siguiendo";

    //Columnas
    public static final String ID_ELEMENTO = "idElementoManga";
    public static final String NOMBRE_MANGA = "nombreManga";
    public static final String URL_MANGA = "urlManga";
    public static final String CONTADOR_CAPITULOS = "cantidadCapitulos";
    public static final String BIT_SEGUIR_NO = "valorSiguiendo";



    //string del create
    public static final String TABLA_PAGINA_SQL =
            "CREATE TABLE  " + TABLA_PAGINAS + "(" +
                    PAGINA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    PAGINA_NOMBRE + " TEXT NOT NULL);" ;

    public static final String TABLA_PARA_SEGUIR =
            "CREATE TABLE " + TABLA_SEGUIR + "(" +
                    ID_ELEMENTO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NOMBRE_MANGA + " TEXT NOT NULL, " +
                    URL_MANGA + " TEXT NOT NULL, " +
                    CONTADOR_CAPITULOS + " TEXT NOT NULL, " +
                    BIT_SEGUIR_NO + " INTEGER);";
}
