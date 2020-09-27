package com.dafran.booksearch.Adaptador.TMOAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dafran.booksearch.Clases.TMOClases.TMOLectorClase;
import com.dafran.booksearch.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TMOLectorAdapter extends RecyclerView.Adapter<TMOLectorAdapter.ViewHolder> {
    private ArrayList<TMOLectorClase> tmoLectorClases;
    private Context context;

    public TMOLectorAdapter(ArrayList<TMOLectorClase> tmoItems, Context context) {
        this.tmoLectorClases = tmoItems;
        this.context = context;
    }

    @NonNull
    @Override
    public TMOLectorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adaptador_lectortmo, parent, false);
        return new TMOLectorAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TMOLectorAdapter.ViewHolder holder, int position) {
        TMOLectorClase tmoLectorClase = this.tmoLectorClases.get(position);
        Picasso.get().load(tmoLectorClase.getImg()).into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return tmoLectorClases.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iv;

        public ViewHolder(@NonNull View view) {
            super(view);
            iv = view.findViewById(R.id.ivPaginas);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(tmoItems.get(itemPosition).getUrlCapitulo())));
        }
    }

    public void setFilter(ArrayList<TMOLectorClase> newList) {
        tmoLectorClases = new ArrayList<>();
        tmoLectorClases.addAll(newList);
        notifyDataSetChanged();
    }

    public void updateData(ArrayList<TMOLectorClase> items) {
        this.tmoLectorClases = items;
    }
}
