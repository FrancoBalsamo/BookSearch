package com.dafran.booksearch.Adaptador.LectulandiaAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dafran.booksearch.Clases.Lectulandia.LectulandiaSeleccionItemsClase;
import com.dafran.booksearch.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LectulandiaSeleccionLibroAdaptador extends RecyclerView.Adapter<LectulandiaSeleccionLibroAdaptador.ViewHolder> {
    private ArrayList<LectulandiaSeleccionItemsClase> lectulandiaSeleccionItemsClases;
    private Context context;

    public LectulandiaSeleccionLibroAdaptador(ArrayList<LectulandiaSeleccionItemsClase> lectulandiaSeleccionItemsClases, Context context) {
        this.lectulandiaSeleccionItemsClases = lectulandiaSeleccionItemsClases;
        this.context = context;
    }

    @NonNull
    @Override
    public LectulandiaSeleccionLibroAdaptador.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vista_seleccion_lectulandia, parent, false);
        return new LectulandiaSeleccionLibroAdaptador.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LectulandiaSeleccionLibroAdaptador.ViewHolder holder, int position) {
        LectulandiaSeleccionItemsClase lectuPrincipalClase = this.lectulandiaSeleccionItemsClases.get(position);
        holder.titulo.setText(lectuPrincipalClase.getTitulo());
        holder.autor.setText(lectuPrincipalClase.getAutor());
        holder.generos.setText(lectuPrincipalClase.getGeneros());
        holder.sinopsis.setText(lectuPrincipalClase.getSinopsis());
        Picasso.get().load(lectuPrincipalClase.getImgUrl()).into(holder.portada);
    }

    @Override
    public int getItemCount() {
        return lectulandiaSeleccionItemsClases.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titulo, autor, generos, sinopsis;
        ImageView portada;

        public ViewHolder(@NonNull View view) {
            super(view);
            portada = view.findViewById(R.id.ivLectuProtadaSeleccion);
            titulo = view.findViewById(R.id.tvtituloLectu);
            autor = view.findViewById(R.id.tvAutorLectu);
            generos = view.findViewById(R.id.tvGenerosLectus);
            sinopsis = view.findViewById(R.id.tvSinopsisLectu);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int itemPosition = getAdapterPosition();
        }
    }

    public void setFilter(ArrayList<LectulandiaSeleccionItemsClase> newList) {
        lectulandiaSeleccionItemsClases = new ArrayList<>();
        lectulandiaSeleccionItemsClases.addAll(newList);
        notifyDataSetChanged();
    }

    public void updateData(ArrayList<LectulandiaSeleccionItemsClase> items) {
        this.lectulandiaSeleccionItemsClases = items;
    }
}
