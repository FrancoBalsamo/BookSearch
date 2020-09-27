package com.dafran.booksearch.Adaptador.TrantorAdapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dafran.booksearch.Activities.Trantor.TrantorActivity;
import com.dafran.booksearch.Activities.Trantor.TrantorDetalleLibro;
import com.dafran.booksearch.Clases.Trantor.TrantorBookDetail;
import com.dafran.booksearch.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TrantorBookAdapter extends RecyclerView.Adapter<TrantorBookAdapter.ViewHolder> {
    private ArrayList<TrantorBookDetail> trantorBookDetails;
    private Context context;

    public TrantorBookAdapter(ArrayList<TrantorBookDetail> trantorItems, Context context) {
        this.trantorBookDetails = trantorItems;
        this.context = context;
    }

    @NonNull
    @Override
    public TrantorBookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adaptador_detalle_libro_trantor, parent, false);
        return new TrantorBookAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrantorBookAdapter.ViewHolder holder, int position) {
        TrantorBookDetail trantorBookDetail = this.trantorBookDetails.get(position);
        holder.titulo.setText(trantorBookDetail.getTitulo());
        holder.autor.setText(trantorBookDetail.getAutor());
        holder.lenguaje.setText(trantorBookDetail.getIdioma());
        holder.descripcion.setText(trantorBookDetail.getDescripcion());
        Picasso.get().load(trantorBookDetail.getUrlImage()).into(holder.ivPortada);
    }

    @Override
    public int getItemCount() {
        return trantorBookDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivPortada;
        TextView titulo, autor, lenguaje, descripcion;

        public ViewHolder(@NonNull View view) {
            super(view);
            //final int itemPosition = getAdapterPosition();

            ivPortada = view.findViewById(R.id.portadaLibro);
            titulo = view.findViewById(R.id.tituloLibro);
            autor = view.findViewById(R.id.tvAutor);
            lenguaje = view.findViewById(R.id.tvIdioma);
            descripcion = view.findViewById(R.id.tvDescripcionTrantor);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {}
    }

    public void setFilter(ArrayList<TrantorBookDetail> newList) {
        trantorBookDetails = new ArrayList<>();
        trantorBookDetails.addAll(newList);
        notifyDataSetChanged();
    }

    public void updateData(ArrayList<TrantorBookDetail> items) {
        this.trantorBookDetails = items;
    }
}