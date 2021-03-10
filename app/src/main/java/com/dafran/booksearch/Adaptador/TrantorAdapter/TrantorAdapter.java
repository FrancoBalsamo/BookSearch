package com.dafran.booksearch.Adaptador.TrantorAdapter;

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

import com.dafran.booksearch.Activities.Trantor.TrantorActivity;
import com.dafran.booksearch.Activities.Trantor.TrantorDetalleLibro;
import com.dafran.booksearch.Clases.Trantor.TrantorItems;
import com.dafran.booksearch.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class TrantorAdapter extends RecyclerView.Adapter<TrantorAdapter.ViewHolder> {
    private ArrayList<TrantorItems> trantorItems;
    private Context context;

    public TrantorAdapter(ArrayList<TrantorItems> trantorItems, Context context) {
        this.trantorItems = trantorItems;
        this.context = context;
    }

    @NonNull
    @Override
    public TrantorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vista_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrantorAdapter.ViewHolder holder, int position) {
        TrantorItems trantorItems = this.trantorItems.get(position);
        holder.textView.setText(trantorItems.getTitle());
        holder.idioma.setText(trantorItems.getIdioma());
        Picasso.get().load(trantorItems.getImgUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return trantorItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView textView, idioma;

        public ViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);
            textView = view.findViewById(R.id.textView);
            idioma = view.findViewById(R.id.tvIdioma);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int itemPosition = getAdapterPosition();
            String url = String.valueOf(Uri.parse(trantorItems.get(itemPosition).getDetailUrl()));
            String descarga = String.valueOf(Uri.parse(trantorItems.get(itemPosition).getUrlDescarga()));
            String titulo = String.valueOf(Uri.parse(trantorItems.get(itemPosition).getTitle()));
            String imagen = String.valueOf(Uri.parse(trantorItems.get(itemPosition).getImgUrl()));
            Intent pasarDato = new Intent(context, TrantorDetalleLibro.class);
            pasarDato.putExtra("urlLibro", url);
            pasarDato.putExtra("descarga", descarga);
            pasarDato.putExtra("titulo", titulo);
            pasarDato.putExtra("urlImagen", imagen);
            context.startActivity(pasarDato);
        }
    }

    public void setFilter(ArrayList<TrantorItems> newList) {
        trantorItems = new ArrayList<>();
        trantorItems.addAll(newList);
        notifyDataSetChanged();
    }

    public void updateData(ArrayList<TrantorItems> items) {
        this.trantorItems = items;
    }
}