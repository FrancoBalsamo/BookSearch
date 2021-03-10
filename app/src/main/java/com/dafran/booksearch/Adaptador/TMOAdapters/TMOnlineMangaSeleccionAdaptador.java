package com.dafran.booksearch.Adaptador.TMOAdapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dafran.booksearch.Activities.TMO.TMOnlineLector;
import com.dafran.booksearch.Clases.TMOClases.TMOnlineCapitulosLeidos;
import com.dafran.booksearch.Clases.TMOClases.TMODatosSeleccion;
import com.dafran.booksearch.R;
import com.dafran.booksearch.SQLite.TMOSQL.TMOnlineMetodosSQL;

import java.util.ArrayList;

public class TMOnlineMangaSeleccionAdaptador extends RecyclerView.Adapter<TMOnlineMangaSeleccionAdaptador.ViewHolder> {
    private ArrayList<TMODatosSeleccion> tmoDatosSeleccionArrayList;
    private Context actividad;
    private String manga;
    private String numero_capitulo;

    public TMOnlineMangaSeleccionAdaptador(ArrayList<TMODatosSeleccion> tmoDatosSeleccionArrayList, Context actividad, String manga, String numero_capitulo) {
        this.tmoDatosSeleccionArrayList = tmoDatosSeleccionArrayList;
        this.actividad = actividad;
        this.manga = manga;
        this.numero_capitulo = numero_capitulo;
    }

    @NonNull
    @Override
    public TMOnlineMangaSeleccionAdaptador.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tmonline_manga_vista_capitulos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TMOnlineMangaSeleccionAdaptador.ViewHolder holder, int position) {
        TMODatosSeleccion tmoDatosSeleccion = this.tmoDatosSeleccionArrayList.get(position);
        holder.capitulo.setText(tmoDatosSeleccion.getNumeroCapitulo());

        final TMOnlineMetodosSQL TMOnlineMetodosSQL = new TMOnlineMetodosSQL(actividad);
        String nom = tmoDatosSeleccion.getNombreManga();
        String caps = tmoDatosSeleccion.getNumeroCapitulo();

        if(TMOnlineMetodosSQL.textoLeido(actividad, nom, caps) == false){
            holder.check_uncheck.setBackgroundResource(R.drawable.checked);
        }else{
            holder.check_uncheck.setBackgroundResource(R.drawable.unchecked);
        }
    }

    @Override
    public int getItemCount() {
        return tmoDatosSeleccionArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView capitulo;
        Button leer;
        ImageView check_uncheck;

        public ViewHolder(@NonNull View view) {
            super(view);
            capitulo = view.findViewById(R.id.tvCapituloNumeroLista);
            leer = view.findViewById(R.id.btnLeerTmo);
            check_uncheck = view.findViewById(R.id.leido);

            view.setOnClickListener(this);
            final TMOnlineMetodosSQL TMOnlineMetodosSQL = new TMOnlineMetodosSQL(actividad);

            leer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //La posición del item en el adaptador
                    int itemPosition = getAdapterPosition();

                    //Para pasar los datos
                    String url = String.valueOf(Uri.parse(tmoDatosSeleccionArrayList.get(itemPosition).getUrlCapitulo()));
                    Intent lectorTmo = new Intent(actividad, TMOnlineLector.class);
                    lectorTmo.putExtra("url", url);

                    //Para controlar los leídos - no leídos
                    String nombre_manga = String.valueOf(Uri.parse(tmoDatosSeleccionArrayList.get(itemPosition).getNombreManga()));
                    String nombre_capitulo = String.valueOf(Uri.parse(tmoDatosSeleccionArrayList.get(itemPosition).getNumeroCapitulo()));
                    int valor_leido = 1;

                    TMOnlineCapitulosLeidos TMOnlineCapitulosLeidos = new TMOnlineCapitulosLeidos();
                    TMOnlineCapitulosLeidos.setNombre_manga(nombre_manga);
                    TMOnlineCapitulosLeidos.setLeido(valor_leido);
                    TMOnlineCapitulosLeidos.setNombre_capitulo_leido(nombre_capitulo);
                    TMOnlineMetodosSQL.consultarSiguiendoYGuardarLeido(actividad, nombre_manga, nombre_capitulo, TMOnlineCapitulosLeidos);

                    if(TMOnlineMetodosSQL.textoLeido(actividad, nombre_manga, nombre_capitulo) == false){
                        check_uncheck.setBackgroundResource(R.drawable.checked);
                    }

                    actividad.startActivity(lectorTmo);
                }
            });
        }

        @Override
        public void onClick(View view) {        }
    }

    public void setFilter(ArrayList<TMODatosSeleccion> newList) {
        tmoDatosSeleccionArrayList = new ArrayList<>();
        tmoDatosSeleccionArrayList.addAll(newList);
        notifyDataSetChanged();
    }

    public void updateData(ArrayList<TMODatosSeleccion> items) {
        this.tmoDatosSeleccionArrayList = items;
    }
}