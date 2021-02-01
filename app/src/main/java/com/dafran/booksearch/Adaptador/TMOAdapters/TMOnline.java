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

import com.dafran.booksearch.Activities.TMO.TMOnlineCapitulosSeleccion;
import com.dafran.booksearch.Clases.TMOClases.TMOItems;
import com.dafran.booksearch.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TMOnline extends RecyclerView.Adapter<TMOnline.ViewHolder> {
    private ArrayList<TMOItems> tmoItems;
    private Context context;

    public TMOnline(ArrayList<TMOItems> tmoItems, Context context) {
        this.tmoItems = tmoItems;
        this.context = context;
    }

    @NonNull
    @Override
    public TMOnline.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vista_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TMOnline.ViewHolder holder, int position) {
        TMOItems tmOnline = this.tmoItems.get(position);
        holder.textView.setText(tmOnline.getNombre());
        Picasso.get().load(tmOnline.getImgUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return tmoItems.size();
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

            String url = String.valueOf(Uri.parse(tmoItems.get(itemPosition).getDetalleUrl()));
            String nombreSeleccion = String.valueOf(tmoItems.get(itemPosition).getNombre());
            String tipo = String.valueOf(tmoItems.get(itemPosition).getTipo());
            Intent pasarDatoUrl = new Intent(context, TMOnlineCapitulosSeleccion.class);
            pasarDatoUrl.putExtra("valor", url);
            pasarDatoUrl.putExtra("nombre", nombreSeleccion);
            pasarDatoUrl.putExtra("tipo", tipo);
            context.startActivity(pasarDatoUrl);
        }
    }

    public void setFilter(ArrayList<TMOItems> newList) {
        tmoItems = new ArrayList<>();
        tmoItems.addAll(newList);
        notifyDataSetChanged();
    }

    public void updateData(ArrayList<TMOItems> items) {
        this.tmoItems = items;
    }
}
