package com.dafran.booksearch.Adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dafran.booksearch.Clases.Paginas;
import com.dafran.booksearch.R;

import java.util.ArrayList;

public class AdaptadorMainListView extends BaseAdapter {
    private Context context;
    ArrayList<Paginas> array;

    public AdaptadorMainListView(Context context, ArrayList<Paginas> array){
        this.context = context;
        this.array = array;
    }

    @Override
    public int getCount() {
        return this.array.size();
    }
    @Override
    public Object getItem(int position) {
        return this.array.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    //desde acá aplicamos como queremos que se vea nuestra lista
    //en nuestro main activity
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //creamos la vista
        View rowView = convertView;
        if (convertView == null) {
            // Nueva vista en la lista
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.adaptador_main, parent, false);//La metemos en el layout
        }
        // mostramos los datos
        TextView marca= rowView.findViewById(R.id.tvArray); //en un textview
        ImageView iv = rowView.findViewById(R.id.bkgd);
        if(array.get(position).getId()==1){
            iv.setBackgroundResource(R.drawable.trantor);
        }
        if(array.get(position).getId()==2){
            iv.setBackgroundResource(R.drawable.lectulandia);
        }
        if(array.get(position).getId()==3){
            iv.setBackgroundResource(R.drawable.tmo);
        }
        marca.setText(array.get(position).getPagina());
        return rowView; //un return para la vista
    }
}
