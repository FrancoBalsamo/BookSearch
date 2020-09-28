package com.dafran.booksearch.Adaptador.LectulandiaAdapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dafran.booksearch.Activities.Lectulandia.LectulandiaSeleccion;
import com.dafran.booksearch.Activities.TMO.TMODatosSeleccionActivity;
import com.dafran.booksearch.Adaptador.TMOAdapters.TMOnline;
;
import com.dafran.booksearch.Clases.Lectulandia.LectuPrincipalClase;
import com.dafran.booksearch.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LectulandiaInicioAdaptador extends RecyclerView.Adapter<LectulandiaInicioAdaptador.ViewHolder> {
    private ArrayList<LectuPrincipalClase> lectuPrincipalClases;
    private Context context;

    public LectulandiaInicioAdaptador(ArrayList<LectuPrincipalClase> lectuPrincipalClases, Context context) {
        this.lectuPrincipalClases = lectuPrincipalClases;
        this.context = context;
    }

    @NonNull
    @Override
    public LectulandiaInicioAdaptador.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vista_lectulandia, parent, false);
        return new LectulandiaInicioAdaptador.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LectulandiaInicioAdaptador.ViewHolder holder, int position) {
        LectuPrincipalClase lectuPrincipalClase = this.lectuPrincipalClases.get(position);
        holder.textView.setText(lectuPrincipalClase.getTitulo());
        Picasso.get().load(lectuPrincipalClase.getImagenUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return lectuPrincipalClases.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.ivLectuPortada);
            textView = view.findViewById(R.id.tvLectuTit);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int itemPosition = getAdapterPosition();

            String url = String.valueOf(Uri.parse(lectuPrincipalClases.get(itemPosition).getUrlLink()));
            String imagen = String.valueOf(Uri.parse(lectuPrincipalClases.get(itemPosition).getImagenUrl()));
            String titulo = String.valueOf(Uri.parse(lectuPrincipalClases.get(itemPosition).getTitulo()));
            Intent pasarDatoUrl = new Intent(context, LectulandiaSeleccion.class);
            pasarDatoUrl.putExtra("url", url);
            pasarDatoUrl.putExtra("imagen", imagen);
            pasarDatoUrl.putExtra("titulo", titulo);
            context.startActivity(pasarDatoUrl);
        }
    }

    public void setFilter(ArrayList<LectuPrincipalClase> newList) {
        lectuPrincipalClases = new ArrayList<>();
        lectuPrincipalClases.addAll(newList);
        notifyDataSetChanged();
    }

    public void updateData(ArrayList<LectuPrincipalClase> items) {
        this.lectuPrincipalClases = items;
    }
}
