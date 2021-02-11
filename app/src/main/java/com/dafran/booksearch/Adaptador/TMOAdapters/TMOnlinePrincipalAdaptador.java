package com.dafran.booksearch.Adaptador.TMOAdapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dafran.booksearch.Activities.TMO.TMOnlineMangaSeleccionado;
import com.dafran.booksearch.Clases.TMOClases.TMOItems;
import com.dafran.booksearch.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TMOnlinePrincipalAdaptador extends RecyclerView.Adapter<TMOnlinePrincipalAdaptador.ViewHolder> {
    private ArrayList<TMOItems> tmoItemsArrayList;
    private Context actividad;

    public TMOnlinePrincipalAdaptador(ArrayList<TMOItems> tmoItemsArrayList, Context actividad) {
        this.tmoItemsArrayList = tmoItemsArrayList;
        this.actividad = actividad;
    }

    @NonNull
    @Override
    public TMOnlinePrincipalAdaptador.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tmoonline_vista_mangas_busqueda, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TMOnlinePrincipalAdaptador.ViewHolder holder, int position) {
        TMOItems tmOnline = this.tmoItemsArrayList.get(position);
        holder.tituloManga.setText(tmOnline.getNombre());
        Picasso.get().load(tmOnline.getImgUrl()).into(holder.portadaManga);
    }

    @Override
    public int getItemCount() {
        return tmoItemsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView portadaManga;
        TextView tituloManga;

        public ViewHolder(@NonNull View view) {
            super(view);
            portadaManga = view.findViewById(R.id.ivPortadaManga);
            tituloManga = view.findViewById(R.id.tvTituloManga);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int itemPosition = getAdapterPosition();

            String url = String.valueOf(Uri.parse(tmoItemsArrayList.get(itemPosition).getDetalleUrl()));
            String nombreSeleccion = String.valueOf(tmoItemsArrayList.get(itemPosition).getNombre());
            String tipo = String.valueOf(tmoItemsArrayList.get(itemPosition).getTipo());
            String urlImagen = String.valueOf(Uri.parse(tmoItemsArrayList.get(itemPosition).getImgUrl()));
            Intent pasarDatoUrl = new Intent(actividad, TMOnlineMangaSeleccionado.class);
            pasarDatoUrl.putExtra("valor", url);
            pasarDatoUrl.putExtra("nombre", nombreSeleccion);
            pasarDatoUrl.putExtra("tipo", tipo);
            pasarDatoUrl.putExtra("urlImagen", urlImagen);
            actividad.startActivity(pasarDatoUrl);
        }
    }

    public void setFilter(ArrayList<TMOItems> newList) {
        tmoItemsArrayList = new ArrayList<>();
        tmoItemsArrayList.addAll(newList);
        notifyDataSetChanged();
    }

    public void updateData(ArrayList<TMOItems> items) {
        this.tmoItemsArrayList = items;
    }
}
