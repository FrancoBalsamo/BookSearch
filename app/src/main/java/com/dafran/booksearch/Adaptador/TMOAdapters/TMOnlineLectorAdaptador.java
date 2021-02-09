package com.dafran.booksearch.Adaptador.TMOAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ZoomControls;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dafran.booksearch.Clases.TMOClases.TMOLectorClase;
import com.dafran.booksearch.R;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class TMOnlineLectorAdaptador extends RecyclerView.Adapter<TMOnlineLectorAdaptador.ViewHolder>{
    private ArrayList<TMOLectorClase> tmoLectorClases;
    private Context context;
    PhotoViewAttacher mAttacher;

    public TMOnlineLectorAdaptador(ArrayList<TMOLectorClase> tmoItems, Context context) {
        this.tmoLectorClases = tmoItems;
        this.context = context;
    }

    @NonNull
    @Override
    public TMOnlineLectorAdaptador.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adaptador_lectortmo, parent, false);
        return new TMOnlineLectorAdaptador.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TMOnlineLectorAdaptador.ViewHolder holder, int position) {
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
            mAttacher = new PhotoViewAttacher(iv);
            view.setOnClickListener(this);
            mAttacher.update();
        }
        @Override
        public void onClick(View view) {
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
