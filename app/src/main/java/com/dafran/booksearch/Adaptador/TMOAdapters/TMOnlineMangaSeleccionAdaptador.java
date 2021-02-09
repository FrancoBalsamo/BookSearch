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
import com.dafran.booksearch.Clases.CapitulosLeidos;
import com.dafran.booksearch.Clases.TMOClases.TMODatosSeleccion;
import com.dafran.booksearch.R;
import com.dafran.booksearch.SQLite.PaginasSQL;

import java.util.ArrayList;

public class TMOnlineMangaSeleccionAdaptador extends RecyclerView.Adapter<TMOnlineMangaSeleccionAdaptador.ViewHolder> {
    private ArrayList<TMODatosSeleccion> tmoItems;
    private Context context;
    private String manga;
    private String numero_capitulo;

    public TMOnlineMangaSeleccionAdaptador(ArrayList<TMODatosSeleccion> tmoItems, Context context, String manga, String numero_capitulo) {
        this.tmoItems = tmoItems;
        this.context = context;
        this.manga = manga;
        this.numero_capitulo = numero_capitulo;
    }

    @NonNull
    @Override
    public TMOnlineMangaSeleccionAdaptador.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adaptador_manga, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TMOnlineMangaSeleccionAdaptador.ViewHolder holder, int position) {
        TMODatosSeleccion tmoDatosSeleccion = this.tmoItems.get(position);
        holder.capitulo.setText(tmoDatosSeleccion.getNumeroCapitulo());

        final PaginasSQL paginasSQL = new PaginasSQL(context);
        String nom = tmoDatosSeleccion.getNombreManga();
        String caps = tmoDatosSeleccion.getNumeroCapitulo();

        if(paginasSQL.textoLeido(context, nom, caps) == false){
            holder.check_uncheck.setBackgroundResource(R.drawable.checked);
        }else{
            holder.check_uncheck.setBackgroundResource(R.drawable.unchecked);
        }
    }

    @Override
    public int getItemCount() {
        return tmoItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView capitulo;
        Button leer;
        ImageView check_uncheck;

        public ViewHolder(@NonNull View view) {
            super(view);
            capitulo = view.findViewById(R.id.tvCap);
            leer = view.findViewById(R.id.btnLeerTmo);
            check_uncheck = view.findViewById(R.id.leido);

            view.setOnClickListener(this);
            final PaginasSQL paginasSQL = new PaginasSQL(context);

            leer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //La posición del item en el adaptador
                    int itemPosition = getAdapterPosition();

                    //Para pasar los datos
                    String url = String.valueOf(Uri.parse(tmoItems.get(itemPosition).getUrlCapitulo()));
                    Intent lectorTmo = new Intent(context, TMOnlineLector.class);
                    lectorTmo.putExtra("url", url);

                    //Para controlar los leídos - no leídos
                    String nombre_manga = String.valueOf(Uri.parse(tmoItems.get(itemPosition).getNombreManga()));
                    String nombre_capitulo = String.valueOf(Uri.parse(tmoItems.get(itemPosition).getNumeroCapitulo()));
                    int valor_leido = 1;

                    CapitulosLeidos capitulosLeidos = new CapitulosLeidos();
                    capitulosLeidos.setNombre_manga(nombre_manga);
                    capitulosLeidos.setLeido(valor_leido);
                    capitulosLeidos.setNombre_capitulo_leido(nombre_capitulo);
                    paginasSQL.consultarSiguiendoYGuardarLeido(context, nombre_manga, nombre_capitulo, capitulosLeidos);

                    if(paginasSQL.textoLeido(context, nombre_manga, nombre_capitulo) == false){
                        check_uncheck.setBackgroundResource(R.drawable.checked);
                    }

                    context.startActivity(lectorTmo);
                }
            });
        }

        @Override
        public void onClick(View view) {        }
    }

    public void setFilter(ArrayList<TMODatosSeleccion> newList) {
        tmoItems = new ArrayList<>();
        tmoItems.addAll(newList);
        notifyDataSetChanged();
    }

    public void updateData(ArrayList<TMODatosSeleccion> items) {
        this.tmoItems = items;
    }
}