package com.dafran.booksearch.SQLite;

import java.net.URL;

public class TMOnlineTablasSQL {
   // public static final String DB_NAME = "bd_Final.db";
    public static final String DB_NAME = "bd_001.db";
    public static final int DB_VERSION = 1;//version

    //TABLA SIGUIENDO
    public static final String TABLA_SEGUIR = "listaMangas";

    //Columnas
    public static final String ID_ELEMENTO = "idElementoManga";
    public static final String NOMBRE_MANGA = "nombreManga";
    public static final String URL_MANGA = "urlManga";
    public static final String URL_IMAGEN = "urlImagen";
    public static final String CONTADOR_CAPITULOS = "cantidadCapitulos";
    public static final String BIT_SEGUIR_NO = "valorSiguiendo";
    public static final String TIPO_MANGA = "tipoManga";

    public static final String TABLA_PARA_SEGUIR =
            "CREATE TABLE " + TABLA_SEGUIR + "(" +
                    ID_ELEMENTO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NOMBRE_MANGA + " TEXT NOT NULL, " +
                    URL_MANGA + " TEXT NOT NULL, " +
                    URL_IMAGEN + " TEXT NOT NULL, " +
                    CONTADOR_CAPITULOS + " TEXT NOT NULL, " +
                    BIT_SEGUIR_NO + " INTEGER, " +
                    TIPO_MANGA + " TEXT NOT NULL);";

    //TABLA ULTIMO CAPÍTULO LEÍDO
    public static final String TABLA_CAPITULO_LEIDO = "capituloLeído";

    //Columnas
    public static final String ID_ = "id_";
    public static final String NOMBRE = "nombreManga";
    public static final String BIT_LEIDO = "bitLeido";
    public static final String ULTIMO_LEIDO = "ultimoLeido";

    public static final String TABLA_CAPITULO_LEIDO_SQL =
         "CREATE TABLE " + TABLA_CAPITULO_LEIDO + "(" +
                 ID_ + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                 NOMBRE + " TEXT NOT NULL, " +
                 BIT_LEIDO + " INTEGER, " +
                 ULTIMO_LEIDO + " TEXT NOT NULL);";
}
