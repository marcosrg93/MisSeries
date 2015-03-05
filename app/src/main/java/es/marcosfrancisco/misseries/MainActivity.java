package es.marcosfrancisco.misseries;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.MapFragment;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private ArrayList<Series> series;
    private ListView lv;
    private Adaptador ad;
    private Db4O bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        series = new ArrayList<Series>();
        bd = new Db4O(this);
        series = bd.getConsulta();
        lv=(ListView)findViewById(R.id.listView);
        ad = new Adaptador(this, R.layout.activity_main, series);
        lv.setAdapter(ad);



        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {


            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String[] opc = new String[]{"Borrar", "Modificar"};
                final int posicion = position;
                AlertDialog opciones = new AlertDialog.Builder(
                        MainActivity.this)
                        .setTitle("Opciones")
                        .setItems(opc,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int selected) {
                                        if (selected == 0) {
                                            borrar(posicion);
                                        } else if (selected == 1) {
                                            editar(posicion);
                                        }
                                    }
                                }).create();
                opciones.show();
                return true;
            }
        });


        Adaptador ad=new Adaptador(this,R.layout.detalle,series);
        lv.setAdapter(ad);
        registerForContextMenu(lv);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.anadir) {
            agregar();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        bd.close();
        super.onDestroy();
    }

    /*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     ++++++++++++++++++++++++++++++++++++Metodos de Inserción,Modificación,Borrado++++++++++++++++++
     */
    private boolean agregar() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.nueva_serie, null);
        final EditText et1, et2,et3;
        et1 = (EditText) vista.findViewById(R.id.etN);
        et2 = (EditText) vista.findViewById(R.id.etA);
        et3 = (EditText) vista.findViewById(R.id.etTemp);
        et1.setHint("Introduce la serie");
        et2.setHint("Introduce el año de inicio");
        et3.setHint("Introduce las temporadas");

        final AlertDialog d = new AlertDialog.Builder(this)
                .setView(vista)
                .setTitle("Añadir Serie")
                .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, null)
                .create();
        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if(et1.getText().toString().length() > 0
                                && et3.getText().length() > 0&& et2.getText().length()>0) {
                            int annio = 0;
                            int temp = 0;

                            try {
                                 annio = Integer.parseInt(et2.getText().toString());
                                 temp = Integer.parseInt(et3.getText().toString());
                            } catch (Exception e) {
                            }
                            Series i = new Series();

                            i.setNombre(et1.getText().toString());
                            i.setAnnio(annio);
                            i.setTemporadas(temp);

                            bd.insertar(i);
                            Log.v("Insertar Datos","Insertamos los datos de "+i);
                            series.add(i);

                            Adaptador ad = new Adaptador(MainActivity.this, R.layout.detalle, series);
                            ad.notifyDataSetChanged();

                            ListView lv = (ListView) findViewById(R.id.listView);
                            lv.setAdapter(ad);
                            tostada("La serie "+i.getNombre()+" ha sido añadida");
                            d.dismiss();
                        }
                        if(et2.getText().toString().length() == 0 ){
                            tostada("¡Introduzca un nombre!");
                        }
                        if(et3.getText().toString().length() == 0 ){
                            tostada("Introduzca el año que empezo la serie");
                        }
                        if(et1.getText().toString().length() == 0 ){
                            tostada("Introduzca las temporadas que tiene");
                        }
                    }
                });
            }
        });
        d.show();
        lv=(ListView) findViewById(R.id.listView);
        Adaptador ad=new Adaptador(this,R.layout.detalle,series);
        lv.setAdapter(ad);
        return true;

    }

    private boolean editar(final int index) {
        Series i=new Series();
        i=series.get(index);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.nueva_serie, null);
        final EditText et1, et2,et3;
        String nom,an,tem;

        nom=i.getNombre();
        an=i.getAnnio()+"";
        tem=i.getTemporadas()+"";
        et1 = (EditText) vista.findViewById(R.id.etN);
        et2 = (EditText) vista.findViewById(R.id.etA);
        et3 = (EditText) vista.findViewById(R.id.etTemp);
        et1.setText(nom);
        et2.setText(an);
        et3.setText(tem);
        final AlertDialog d = new AlertDialog.Builder(this)
                .setView(vista)
                .setTitle("Modificar serie")
                .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, null)
                .create();
        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        if(et1.getText().toString().length() > 0
                                && et3.getText().length() > 0&& et2.getText().length()>0 ) {
                            int annio = 0;
                            int temp = 0;

                            try {
                                annio = Integer.parseInt(et2.getText().toString());
                                temp = Integer.parseInt(et3.getText().toString());
                            } catch (Exception e) {
                            }
                            Series i = new Series();

                            i.setNombre(et1.getText().toString());
                            i.setAnnio(annio);
                            i.setTemporadas(temp);
                            bd.update(i);
                            series.set(index,i);
                            Adaptador ad = new Adaptador(MainActivity.this, R.layout.detalle, series);
                            ad.notifyDataSetChanged();

                            ListView lv = (ListView) findViewById(R.id.listView);
                            lv.setAdapter(ad);
                            tostada("La serie "+i.getNombre()+" ha sido modificada");
                            d.dismiss();
                        }
                        if(et2.getText().toString().length() == 0 ){
                            tostada("¡Introduzca un nombre!");
                        }
                        if(et3.getText().toString().length() == 0 ){
                            tostada("Introduzca el año que empezo la serie");
                        }
                        if(et1.getText().toString().length() == 0 ){
                            tostada("Introduzca las temporadas que tiene");
                        }
                    }
                });
            }
        });
        d.show();
        lv=(ListView) findViewById(R.id.listView);
        Adaptador ad=new Adaptador(this,R.layout.detalle,series);
        lv.setAdapter(ad);
        return true;
    }

    public boolean borrar(final int pos){
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(MainActivity.this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("¿ Desea borrar esta serie ?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

                bd.delete(series.get(pos));
                series.remove(pos);
                Adaptador ad = new Adaptador(MainActivity.this, R.layout.detalle, series);
                ad.notifyDataSetChanged();
                ListView lv = (ListView) findViewById(R.id.listView);
                lv.setAdapter(ad);
                tostada("Serie borrada");
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                dialogo1.cancel();
            }
        });
        dialogo1.show();
        return true;
    }

    public Toast tostada(String t) {
        Toast toast =
                Toast.makeText(getApplicationContext(),
                        t + "", Toast.LENGTH_SHORT);
        toast.show();
        return toast;
    }
}
