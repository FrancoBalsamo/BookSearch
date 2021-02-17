package com.dafran.booksearch.Adaptador.TMOAdapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dafran.booksearch.Clases.TMOClases.TMOLectorClase;
import com.dafran.booksearch.R;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TMOnlineLectorAdaptador extends RecyclerView.Adapter<TMOnlineLectorAdaptador.ViewHolder>{
    private ArrayList<TMOLectorClase> tmoLectorClaseArrayList;
    private Context actividad;
    PhotoViewAttacher mAttacher;

    public TMOnlineLectorAdaptador(ArrayList<TMOLectorClase> tmoLectorClaseArrayList, Context actividad) {
        this.tmoLectorClaseArrayList = tmoLectorClaseArrayList;
        this.actividad = actividad;
    }

    @NonNull
    @Override
    public TMOnlineLectorAdaptador.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tmonline_lector_adaptador, parent, false);
        return new TMOnlineLectorAdaptador.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TMOnlineLectorAdaptador.ViewHolder holder, int position) {
        final TMOLectorClase tmoLectorClase = this.tmoLectorClaseArrayList.get(position);
        //Carga con Picasso
        Picasso.get().load(tmoLectorClase.getImg()).into(holder.ivPaginas, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(actividad,"Ocurrió un error cargando la imagen : " + tmoLectorClase.getImg(), Toast.LENGTH_LONG).show();
            }

        });
    }

    @Override
    public int getItemCount() {
        return tmoLectorClaseArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivPaginas;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View view) {
            super(view);
            ivPaginas = view.findViewById(R.id.ivPaginas);
            progressBar = view.findViewById(R.id.progressbar);
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
        }
    }

    public void setFilter(ArrayList<TMOLectorClase> newList) {
        tmoLectorClaseArrayList = new ArrayList<>();
        tmoLectorClaseArrayList.addAll(newList);
        notifyDataSetChanged();
    }

    public void updateData(ArrayList<TMOLectorClase> items) {
        this.tmoLectorClaseArrayList = items;
    }
}
