package es.marcosfrancisco.misseries;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;


public class Adaptador extends ArrayAdapter<Series> {
    public static LayoutInflater i;
    public int resource;
    public List<Series> lista;
    public Adaptador(Context context, int resource, List<Series> objects){
        super(context,resource,objects);
        this.resource=resource;
        lista=objects;
        this.i=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public View getView(int position, View v,ViewGroup vg){
        if(v==null){
            v=i.inflate(resource,null);
        }
        TextView tvN=(TextView) v.findViewById(R.id.tvNombre);
        TextView tvA=(TextView) v.findViewById(R.id.tvAnnio);
        TextView tvT=(TextView) v.findViewById(R.id.tvTemp);
        tvN.setText(lista.get(position).getNombre());
        tvA.setText(String.valueOf(lista.get(position).getAnnio()));
        tvT.setText(String.valueOf(lista.get(position).getTemporadas()));

        return v;
    }
}
