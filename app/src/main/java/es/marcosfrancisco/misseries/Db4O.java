package es.marcosfrancisco.misseries;

import android.content.Context;
import android.util.Log;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;

import java.util.ArrayList;

/**
 * Created by MarcosFrancisco on 05/03/2015.
 */
public class Db4O {

    private ObjectContainer bd;

    public Db4O(Context ctx) {
        bd = Db4oEmbedded.openFile(
                Db4oEmbedded.newConfiguration(), ctx.getExternalFilesDir(null) +
                        "/bd.db4o");
    }

    public void insertar(Series ser){
        bd.store(ser);
        bd.rollback();
        Log.v("INSERCION","Despues del commit");

    }

    public void update(Series ser){
        bd.store(ser);
        bd.commit();
        Log.v("ACTUALIZAR","Despues del commit");
    }

    public void delete(Series ser){
        bd.delete(ser);
        bd.commit();
        Log.v("BORRADO","Despues del commit");
    }

    public void close(){
        bd.close();
    }

    public ArrayList<Series> getConsulta(){
        Query consulta = bd.query();
        consulta.constrain(Series.class);
        ObjectSet<Series> ser = consulta.execute();
        ArrayList<Series> lista = new ArrayList<Series>();
        for(Series serie: ser){
                lista.add(serie);
        }
        return lista;
    }

    public ArrayList<String> getConsultaSeries(){
        Query consulta = bd.query();
        consulta.constrain(Series.class);
        ObjectSet<Series> ser = consulta.execute();
        ArrayList<String> lista = new ArrayList<String>();
        for(Series serie: ser){
            String seri = serie.getNombre();
                lista.add(seri);
        }
        return lista;
    }




}
