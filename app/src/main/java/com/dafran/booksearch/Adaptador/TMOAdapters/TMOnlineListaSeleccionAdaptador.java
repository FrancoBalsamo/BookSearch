package com.dafran.booksearch.Adaptador.TMOAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dafran.booksearch.Clases.TMOClases.TMOnlineSeguirManga;
import com.dafran.booksearch.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TMOnlineListaSeleccionAdaptador extends BaseAdapter {
    private Context context;
    ArrayList<TMOnlineSeguirManga> TMOnlineSeguirMangaArrayList;

    public TMOnlineListaSeleccionAdaptador(ArrayList<TMOnlineSeguirManga> TMOnlineSeguirMangaArrayList, Context context){
        this.TMOnlineSeguirMangaArrayList = TMOnlineSeguirMangaArrayList;
        this.context = context;
    }

    @Override
    public int getCount(){
        return this.TMOnlineSeguirMangaArrayList.size();
    }

    @Override
    public Object getItem(int position){
        return this.TMOnlineSeguirMangaArrayList.get(position);
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
        TMOnlineSeguirManga sm  = this.TMOnlineSeguirMangaArrayList.get(position);
        Picasso.get().load(sm.getUrlImagen()).into(caratula);
        nombreManga.setText(sm.getNombre());
        return rowView;
    }
}
