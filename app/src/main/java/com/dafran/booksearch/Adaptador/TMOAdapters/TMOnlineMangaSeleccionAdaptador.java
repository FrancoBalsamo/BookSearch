package com.dafran.booksearch.Adaptador.TMOAdapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dafran.booksearch.Activities.TMO.TMOnlineLector;
import com.dafran.booksearch.Clases.TMOClases.TMODatosSeleccion;
import com.dafran.booksearch.R;

import java.util.ArrayList;

public class TMOnlineMangaSeleccionAdaptador extends RecyclerView.Adapter<TMOnlineMangaSeleccionAdaptador.ViewHolder> {
    private ArrayList<TMODatosSeleccion> tmoItems;
    private Context context;

    public TMOnlineMangaSeleccionAdaptador(ArrayList<TMODatosSeleccion> tmoItems, Context context) {
        this.tmoItems = tmoItems;
        this.context = context;
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
        holder.textView.setText(tmoDatosSeleccion.getNumeroCapitulo());
    }

    @Override
    public int getItemCount() {
        return tmoItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;
        Button leer;

        public ViewHolder(@NonNull View view) {
            super(view);
            textView = view.findViewById(R.id.tvCap);
            leer = view.findViewById(R.id.btnLeerTmo);

            view.setOnClickListener(this);

            leer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int itemPosition = getAdapterPosition();
                    String url = String.valueOf(Uri.parse(tmoItems.get(itemPosition).getUrlCapitulo()));
                    Intent lectorTmo = new Intent(context, TMOnlineLector.class);
                    lectorTmo.putExtra("url", url);
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