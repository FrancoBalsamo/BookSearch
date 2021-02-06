package com.dafran.booksearch.Adaptador.TMOAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dafran.booksearch.Clases.SeguirManga;
import com.dafran.booksearch.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TMOnlineListaSeleccionAdaptador extends BaseAdapter {
    private Context context;
    ArrayList<SeguirManga> seguirMangaArrayList;

    public TMOnlineListaSeleccionAdaptador(ArrayList<SeguirManga>seguirMangaArrayList, Context context){
        this.seguirMangaArrayList = seguirMangaArrayList;
        this.context = context;
    }

    @Override
    public int getCount(){
        return this.seguirMangaArrayList.size();
    }

    @Override
    public Object getItem(int position){
        return this.seguirMangaArrayList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup){
        View rowView = convertView;
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = layoutInflater.inflate(R.layout.tmonline_lista_text_view, viewGroup, false);
        }
        TextView nombreManga = rowView.findViewById(R.id.tvMangaLista);
        ImageView caratula = rowView.findViewById(R.id.ivListaMangas);
        SeguirManga sm  = this.seguirMangaArrayList.get(position);
        Picasso.get().load(sm.getUrlImagen()).into(caratula);
        nombreManga.setText(sm.getNombre());
        return rowView;
    }
}
