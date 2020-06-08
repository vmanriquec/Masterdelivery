package com.mazinger.masterdelivery;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.mazinger.masterdelivery.Realm.*;
import com.mazinger.masterdelivery.Realm.Crudetallepedido;
import com.mazinger.masterdelivery.modelo.Adicional;
import com.mazinger.masterdelivery.modelo.Crema;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import static com.mazinger.masterdelivery.Login.CONNECTION_TIMEOUT;
import static com.mazinger.masterdelivery.Login.READ_TIMEOUT;

public class G extends AppCompatActivity {
    int numerodeadiciones;
    String idproductoseleccionado;
    TextView totalapagar;
int idsupremodedetalle;
int idusuario;
    String FileName = "myfile";
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Realm.init(getApplication());
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("pedido.realm")
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(realmConfig);
        setContentView(R.layout.activity_g);
        totalapagar=(TextView)findViewById(R.id.totalapagar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();


        //datos desde atras
        prefs = getApplicationContext().getSharedPreferences(FileName, Context.MODE_PRIVATE);
       Intent myIntent = getIntent();
        String nombredeproductoseleccionado = myIntent.getStringExtra("nombredeproductoseleccionado"); // will return "FirstKeyValue"
        String preciodeproductoseleccionado= myIntent.getStringExtra("preciodeproductoseleccionado"); // will return "SecondKeyValue"
 idproductoseleccionado= myIntent.getStringExtra("idproductoseleccionado"); // will return "SecondKeyValue"


        realmgrbarenbasedatosdetallepedido(Integer.parseInt(idproductoseleccionado),1,nombredeproductoseleccionado,Double.parseDouble(preciodeproductoseleccionado),1,preciodeproductoseleccionado,"no hay comentarios");
      idsupremodedetalle=Crudetallepedido.calculateIndex();
        Animation a = AnimationUtils.loadAnimation(getApplication(), R.anim.dechicoagrande);
        a.reset();


        TextView cabecera=(TextView) findViewById(R.id.cabeceralayoutadicional);
        TextView cantidadapedir=(TextView) findViewById(R.id.cantidadapedir);
        TextView comentarioacocina=(TextView) findViewById(R.id.editText2e);

        Button mas=(Button) findViewById(R.id.botonmas) ;
        Button menos=(Button) findViewById(R.id.botonmenos) ;
        Button acanasta=(Button)findViewById(R.id.acanasta);

        cabecera.setText(nombredeproductoseleccionado);
        cantidadapedir.setText("1");
        totalapagar.setText(String.valueOf(preciodeproductoseleccionado));

        totalapagar.clearAnimation();
        totalapagar.startAnimation(a);





        acanasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String come=comentarioacocina.getText().toString();

                TextView almacenactivo=(TextView) findViewById(R.id.almacenactivo);
                TextView usuario=(TextView)findViewById(R.id.uno);
                if (come==""){
                    come="no hay comentarios";
                    }else {
                    actualizarcomentarioparacadaproducto(idsupremodedetalle, come);
                    }
               actualizartotalenpedido();
                prefs = getApplication().getSharedPreferences(FileName, Context.MODE_PRIVATE);
                String idempresa11=prefs.getString("idempresa","");
                String nombreempresa11=prefs.getString("nombreempresa","");
                String telefonoempresa11=prefs.getString("telefonoempresa","");
                String tiempodemora11=prefs.getString("tiempodemora","");
                String montominimo11=prefs.getString("montominimo","");


                Intent i= new Intent(getApplicationContext(),Muestraproductosporempresa.class);
                i.putExtra("idempresa",idempresa11);
                i.putExtra("nombreempresa",nombreempresa11);
                i.putExtra("telefonoempresa",telefonoempresa11);
                i.putExtra("tiempodemora",tiempodemora11);
                i.putExtra("montominimo",montominimo11);

                startActivity(i);
            }
        });

        mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String cantidad=cantidadapedir.getText().toString();
                int c= Integer.parseInt(cantidad);
                if(c>=1){
                 c=c+1;
                 cantidadapedir.setText( String.valueOf(c));
                 String va=totalapagar.getText().toString();
                    Double totalitoinicial=Double.parseDouble(va);
                    totalitoinicial=totalitoinicial+(totalitoinicial/(c-1));
                    totalapagar.setText(String.valueOf(totalitoinicial));
                    totalapagar.clearAnimation();
                    totalapagar.startAnimation(a);
                    Masactualizarcantidadendetallemas(idsupremodedetalle,c,String.valueOf(totalitoinicial));
                }
            }
        });
        menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cantidad=cantidadapedir.getText().toString();
                int c= Integer.parseInt(cantidad);
                if(c>1) {
                    c = c - 1;
                    cantidadapedir.setText(String.valueOf(c));
                    String va = totalapagar.getText().toString();
                    Double totalitoinicial = Double.parseDouble(va);
                    totalitoinicial=totalitoinicial-(totalitoinicial/(c+1));
                    totalapagar.setText(String.valueOf(totalitoinicial));
                    totalapagar.clearAnimation();
                    totalapagar.startAnimation(a);
                    Masactualizarcantidadendetallemas(idsupremodedetalle,c,String.valueOf(totalitoinicial));

                }
            }
        });

        new traeradicional().execute(idproductoseleccionado);
        new traercremas().execute(idproductoseleccionado);
    }
    private class traeradicional extends AsyncTask<String, String, String> {
        HttpURLConnection conne;
        ProgressDialog pdLoading = new ProgressDialog(G.this);
        URL url = null;
        ArrayList<Adicional> listadeadicionales = new ArrayList<Adicional>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pdLoading.setMessage("\tCargando Adicionales");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL("https://sodapop.pe/sugest/apitraeradicional.php");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {
                conne = (HttpURLConnection) url.openConnection();
                conne.setReadTimeout(READ_TIMEOUT);
                conne.setConnectTimeout(CONNECTION_TIMEOUT);
                conne.setRequestMethod("POST");
                conne.setDoInput(true);
                conne.setDoOutput(true);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("idproducto", params[0]);
                String query = builder.build().getEncodedQuery();
                // Open connection for sending data
                OutputStream os = conne.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conne.connect();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }
            try {
                int response_code = conne.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK) {
                    InputStream input = conne.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return (
                            result.toString()
                    );
                } else {
                    return ("Connection error");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conne.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Animation a = AnimationUtils.loadAnimation(getApplication(), R.anim.dechicoagrande);
            a.reset();
            pdLoading.dismiss();
            ArrayList<Adicional> peopleadicional = new ArrayList<>();
            String[] stradicional = {"No Suggestions"};
            ArrayList<String> datalistadicional = new ArrayList<String>();


            Adicional mesoadiconal;
            peopleadicional.clear();
            RecyclerView.Adapter adapteradicional;

            if (result.equals("no rows")) {
            } else {
                try {
                    JSONArray jArray = new JSONArray(result);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.optJSONObject(i);
                        mesoadiconal = new Adicional(json_data.getInt("idadicional"), json_data.getString("nombreadicional"), json_data.getDouble("precioadicional"), json_data.getString("estadoadicional"));
                        peopleadicional.add(mesoadiconal);
                    }
                    LinearLayout my_layout = (LinearLayout)findViewById(R.id.my_layout);

                    stradicional = datalistadicional.toArray(new String[datalistadicional.size()]);



                    TextView texto = new TextView(getApplication());
                    texto.setText("        Elije tu Adicional        ");
                    texto.setBackgroundDrawable(getApplication().getResources().getDrawable(R.drawable.abc_btn_colored_material));
                    texto.setGravity(Gravity.CENTER);

                    //  texto.setLayoutParams(param);
                    texto.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                    texto.setTypeface(null, Typeface.NORMAL);
                    texto.setShadowLayer(2, 1, 1, R.color.colorPrimary);
                    texto.setTextColor(getApplication().getResources().getColor(R.color.coloruno));
                    TableRow.LayoutParams textoenlayout = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                    my_layout.addView(texto, textoenlayout);
                    for( numerodeadiciones= 0; numerodeadiciones < peopleadicional.size(); numerodeadiciones++) {
                        CheckBox cb = new CheckBox(getApplication());
                        cb.setText("   "+peopleadicional.get(numerodeadiciones).getNombreadicional()+ "               S/. "+String.valueOf(peopleadicional.get(numerodeadiciones).getPrecioadicional()));
                        cb.setTextColor(getApplication().getResources().getColor(R.color.colorPrimary));
                        Double ffff=peopleadicional.get(numerodeadiciones).getPrecioadicional();
                        String q=peopleadicional.get(numerodeadiciones).getNombreadicional();
                        Double l=peopleadicional.get(numerodeadiciones).getPrecioadicional();
                        cb.setId(numerodeadiciones);
                        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView,
                                                         final boolean isChecked) {
                                CharSequence options[];
                                if (isChecked) {
                                    //String preciodeadicional=String.valueOf(peopleadicional.get(numerodeadiciones).getPrecioadicional());
                                    realmgrabaradicional( q,l,Integer.parseInt(idproductoseleccionado));
                                    TextView totalapaga=(TextView)findViewById(R.id.totalapagar);
                                    TextView cantidadapedir=(TextView)findViewById(R.id.cantidadapedir);
                                    String ct=cantidadapedir.getText().toString();


                                    String vad=totalapaga.getText().toString();
                                    Double totalitoinicial=Double.parseDouble(vad);
                                    totalitoinicial=totalitoinicial+(ffff*Double. parseDouble(ct));
                                    totalapagar.setText(String.valueOf(totalitoinicial));
                                    totalapagar.clearAnimation();
                                    totalapagar.startAnimation(a);

                                    Masactualizarsubtotaldetallemas( idsupremodedetalle,String.valueOf(totalitoinicial));

                                } else {
                                    TextView cantidadapedir=(TextView)findViewById(R.id.cantidadapedir);
                                    String ct=cantidadapedir.getText().toString();
                                    TextView totalapaga=(TextView)findViewById(R.id.totalapagar);
                                    String vad=totalapaga.getText().toString();
                                    Double totalitoinicial=Double.parseDouble(vad);
                                    totalitoinicial=totalitoinicial-(ffff*Double. parseDouble(ct));;
                                    totalapagar.setText(String.valueOf(totalitoinicial));
                                    totalapagar.clearAnimation();
                                    totalapagar.startAnimation(a);
                                    Masactualizarsubtotaldetallemas( idsupremodedetalle,String.valueOf(totalitoinicial));
                 eliminaradicional(idsupremodedetalle,Integer.parseInt(idproductoseleccionado),q);
                                }
                            }});



                        my_layout.addView(cb);
                    }








                } catch (JSONException e) {
                    Log.d("erroro",e.toString());
                }
            }
        }

    }
    private class traercremas extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(G.this);
        HttpURLConnection conne;
        URL url = null;
        ArrayList<Adicional> listadeadicionales = new ArrayList<Adicional>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tCargando Cremas disponibles");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {


            try {
                url = new URL("https://sodapop.pe/sugest/apicremas.php");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {
                conne = (HttpURLConnection) url.openConnection();
                conne.setReadTimeout(READ_TIMEOUT);
                conne.setConnectTimeout(CONNECTION_TIMEOUT);
                conne.setRequestMethod("POST");
                conne.setDoInput(true);
                conne.setDoOutput(true);

                // Append parameters to URL


                Uri.Builder builder = new Uri.Builder()

                        .appendQueryParameter("idproducto", params[0]);

                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conne.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conne.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }
            try {
                int response_code = conne.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK) {
                    InputStream input = conne.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);

                    }
                    return (

                            result.toString()


                    );

                } else {
                    return ("Connection error");
                }
            } catch (IOException e) {
                e.printStackTrace();

                return e.toString();
            } finally {
                conne.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            pdLoading.dismiss();
            ArrayList<Crema> peoplecrema = new ArrayList<>();
            String[] strcrema = {"No Suggestions"};
            ArrayList<String> datalistcrema = new ArrayList<String>();
            Crema mesocrema;
            peoplecrema.clear();
            if (result.equals("no rows")) {
            } else {
                try {
                    JSONArray jArray = new JSONArray(result);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.optJSONObject(i);
                        mesocrema = new Crema(json_data.getInt("idcrema"), json_data.getString("nombrecrema"),  json_data.getString("estadocrema"));
                        peoplecrema.add(mesocrema);
                    }


                    LinearLayout my_layout = (LinearLayout)findViewById(R.id.my_layout);
                    strcrema = datalistcrema.toArray(new String[datalistcrema.size()]);
                    TextView texto = new TextView(getApplication());




                    texto.setText("        Elije tus Cremas        ");
                   // texto.setBackgroundDrawable(getApplication().getResources().getDrawable(R.drawable.blue_leftcorner_bkg));
                    texto.setGravity(Gravity.CENTER);

                  //  texto.setLayoutParams(param);
                    texto.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                    texto.setTypeface(null, Typeface.NORMAL);
                    texto.setShadowLayer(2, 1, 1, R.color.colorPrimary);
                    texto.setTextColor(getApplication().getResources().getColor(R.color.coloruno));


                    TableRow.LayoutParams textoenlayout = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                    my_layout.addView(texto, textoenlayout);

                    int numerodecrema;



                    for(numerodecrema= 0; numerodecrema < peoplecrema.size(); numerodecrema++) {
                        CheckBox cbc = new CheckBox(getApplication());
                        cbc.setText("   "+peoplecrema.get(numerodecrema).getNombrecrema());
                        cbc.setId(numerodecrema+1);
                        cbc.setTextColor(getApplication().getResources().getColor(R.color.colorPrimary));


                        int idcremita=peoplecrema.get(numerodecrema).getIdcrema();
                        String nombrecremita=peoplecrema.get(numerodecrema).getNombrecrema();


                        cbc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView,
                                                         final boolean isChecked) {

                                CharSequence options[];

                                if (isChecked) {
realgrabarcrema(nombrecremita,Integer.parseInt(idproductoseleccionado));

                                } else {
                                    eliminaracrema(idsupremodedetalle,Integer.parseInt(idproductoseleccionado),nombrecremita);
                                  }
                            }});



                        my_layout.addView(cbc);
                    }








                } catch (JSONException e) {
                    Log.d("erroro",e.toString());
                }
            }
        }
    }


    public  static void realmgrbarenbasedatosdetallepedido(final int idproducto,int cantidad,String nombre,Double precio,int idpedido,String subtotal,String comentariococina) {
        Realm pedido = Realm.getDefaultInstance();
        pedido.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm pedido) {
                int index = Crudetallepedido.calculateIndex();
                int intindexpedido= (Crudpedido.calculateIndex()-1);
                Detallepedidorealm realmDetallepedidorealm = pedido.createObject(Detallepedidorealm.class, index);
                realmDetallepedidorealm.setIdproductorealm(idproducto);
                realmDetallepedidorealm.setCantidadrealm(cantidad);
                realmDetallepedidorealm.setNombreproductorealm(nombre);
                realmDetallepedidorealm.setPrecventarealm(precio);
                realmDetallepedidorealm.setSubtotal(subtotal);
                realmDetallepedidorealm.setComentarioacocina(comentariococina);
                realmDetallepedidorealm.setIdpedido(intindexpedido);
                //realmDetallepedidorealm.setIdpedido(index);

            }

        });


    }

    public  static void realmgrabaradicional(final String nombreadicional,Double precioadicional,int idproducto) {
        Realm pedido = Realm.getDefaultInstance();
        pedido.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm pedido) {
                int index = CrudadicionalRealm.calculateIndex();
                int iddedetalle=Crudetallepedido.calculateIndex();
                AdicionalRealm AdicionalRealm = pedido.createObject(AdicionalRealm.class, index);
                AdicionalRealm.setId(iddedetalle-1);
                AdicionalRealm.setIdproducto(idproducto);
                AdicionalRealm.setNombreadicional(nombreadicional);
                AdicionalRealm.setPrecioadicional(precioadicional);
                AdicionalRealm.setEstadoadicional("1");
            }
        });


    }
    public  static void realgrabarcrema(final String nombrecrema,int idproducto) {
        Realm pedido = Realm.getDefaultInstance();
        pedido.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm pedido) {
                int index = CrudcremaRealm.calculateIndex();
                int iddedetalle=Crudetallepedido.calculateIndex();

                CremaRealm CremaRealm = pedido.createObject(CremaRealm.class, index);
                CremaRealm.setId(iddedetalle-1);
                CremaRealm.setEstadocrema("1");
                CremaRealm.setIdproducto(idproducto);
                CremaRealm.setNombrecrema(nombrecrema);
            }
        });


    }

    public final static List<AdicionalRealm> eliminaradicional(int ido,int idproducto,String nombreadicional) {
        Realm pedido = Realm.getDefaultInstance();
int y=ido-1;
        RealmResults<AdicionalRealm> results =
                pedido.where(AdicionalRealm.class).
                        equalTo("id", y)
                        .equalTo("idproducto", idproducto)
                        .equalTo("nombreadicional",nombreadicional)
                        .findAll();
        results.toString().trim();
        pedido.beginTransaction();
        results.deleteFirstFromRealm();     // App crash
        pedido.commitTransaction();



        return results;
    }

    public final static List<CremaRealm> eliminaracrema(int ido,int idproducto,String nombrecrema) {
        Realm pedido = Realm.getDefaultInstance();
        int y=ido-1;
        RealmResults<CremaRealm> results =
                pedido.where(CremaRealm.class).
                        equalTo("id", y)
                        .equalTo("idproducto", idproducto)
                        .equalTo("nombrecrema",nombrecrema)
                        .findAll();
        results.toString().trim();
        pedido.beginTransaction();
        results.deleteFirstFromRealm();     // App crash
        pedido.commitTransaction();
        return results;
    }


    public final static List<PedidoRealm> eliminarpedidos() {
        Realm pedido = Realm.getDefaultInstance();

        RealmResults<PedidoRealm> results =
                pedido.where(PedidoRealm.class)

                        .findAll();
        results.toString().trim();
        pedido.beginTransaction();
        results.deleteFirstFromRealm();     // App crash
        pedido.commitTransaction();
        return results;
    }
    public final static void Masactualizarsubtotaldetallemas(int id,String subtotal){
        int tu= id-1;
        Realm pedido = Realm.getDefaultInstance();
        pedido.beginTransaction();


        Detallepedidorealm pedidoRealm = pedido.where(Detallepedidorealm.class)

                .equalTo("id", tu)
                .findFirst();

       String r= pedidoRealm.toString().trim();
                pedidoRealm.setSubtotal(subtotal);
        pedido.insertOrUpdate(pedidoRealm);
        pedido.commitTransaction();



    }

    public final static void actualizartotalenpedido(){
        String pp=sumarsubtotalesparacolocarenpedido();
        Realm pedido = Realm.getDefaultInstance();
        pedido.beginTransaction();
int tu=Crudpedido.calculateIndex()-1;
        PedidoRealm pedidoRealm = pedido.where(PedidoRealm.class)
                .equalTo("idpedido", tu)
                .findFirst();
      pedidoRealm.setTotalpedido(Double.parseDouble(pp));
        pedido.insertOrUpdate(pedidoRealm);
        pedido.commitTransaction();



    }

    public final static void Masactualizarcantidadendetallemas(int id,int cantidad,String  subtotal){
        int tu= id-1;
        Realm pedido = Realm.getDefaultInstance();
        pedido.beginTransaction();


        Detallepedidorealm pedidoRealm = pedido.where(Detallepedidorealm.class)

                .equalTo("id", tu)
                .findFirst();

        String r= pedidoRealm.toString().trim();
        pedidoRealm.setCantidadrealm(cantidad);
        pedidoRealm.setSubtotal(subtotal);

        pedido.insertOrUpdate(pedidoRealm);
        pedido.commitTransaction();    }

    public final static void actualizarcomentarioparacadaproducto(int id,String comentario){
        int tu= id-1;
        Realm pedido = Realm.getDefaultInstance();
        pedido.beginTransaction();


        Detallepedidorealm pedidoRealm = pedido.where(Detallepedidorealm.class)

                .equalTo("id", tu)
                .findFirst();

        String r= pedidoRealm.toString().trim();
        pedidoRealm.setComentarioacocina(comentario);
        pedido.insertOrUpdate(pedidoRealm);
        pedido.commitTransaction();    }


    public final static String sumarsubtotalesparacolocarenpedido(){
        int tu=Crudpedido.calculateIndex()-1;
String sum;
        Realm pedido = Realm.getDefaultInstance();
        pedido.beginTransaction();
        RealmResults<Detallepedidorealm> results = pedido.where(Detallepedidorealm.class).equalTo("idpedido",tu).findAll();
        int w = results.size();
Double zz,ll=0.0;
        for (int i = 0; i < w; i++){
            zz= Double.parseDouble(results.get(i).getSubtotal());
            ll=ll+zz;
        }
        sum = String.valueOf(ll);
        pedido.commitTransaction();
        return sum;
    }


    public final static int capturariddedetalledeprodysubtotal(String producto, String total) {
int ff = 0;
        Realm pedido = Realm.getDefaultInstance();
        pedido.beginTransaction();
        Detallepedidorealm pedidoRealm = pedido.where(Detallepedidorealm.class).
                equalTo("nombreproductorealm",producto).
                equalTo("subtotal",total)
                .findFirst();
        ff=pedidoRealm.getId();

        return ff;


    }
    public final static List<CremaRealm> eliminaraunTotalcrema(int ido) {
        Realm pedido = Realm.getDefaultInstance();

        RealmResults<CremaRealm> results =
                pedido.where(CremaRealm.class).
                        equalTo("id", ido)
                        .findAll();
        results.toString().trim();
        results.deleteAllFromRealm() ;
        pedido.commitTransaction();
        return results;
    }
    public final static List<AdicionalRealm> eliminarunTotaladicional(int ido) {
        Realm pedido = Realm.getDefaultInstance();

        RealmResults<AdicionalRealm> results =
                pedido.where(AdicionalRealm.class).
                        equalTo("id", ido)
                        .findAll();
        results.toString().trim();
        pedido.beginTransaction();
        results.deleteAllFromRealm();     // App crash
        pedido.commitTransaction();
        return results;
    }

    public final static List<Detallepedidorealm> eliminarunTOTALdetallepedido(int ido) {
        Realm pedido = Realm.getDefaultInstance();

        RealmResults<Detallepedidorealm> results =
                pedido.where(Detallepedidorealm.class).
                        equalTo("id", ido)
                        .findAll();
        results.toString().trim();
        pedido.beginTransaction();
        results.deleteAllFromRealm();     // App crash
        pedido.commitTransaction();
        return results;
    }

    public final static List<CremaRealm> Eliminartotalcremas() {
        Realm pedido = Realm.getDefaultInstance();

        RealmResults<CremaRealm> results =
                pedido.where(CremaRealm.class)

                        .findAll();

        results.deleteAllFromRealm() ;
        pedido.commitTransaction();
        return results;
    }
    public final static List<AdicionalRealm> Eliminartotaladicionals() {
        Realm pedido = Realm.getDefaultInstance();

        RealmResults<AdicionalRealm> results =
                pedido.where(AdicionalRealm.class)

                        .findAll();

        pedido.beginTransaction();
        results.deleteAllFromRealm();     // App crash
        pedido.commitTransaction();
        return results;
    }

    public final static List<Detallepedidorealm> Eliminartotaldetalles() {
        Realm pedido = Realm.getDefaultInstance();

        RealmResults<Detallepedidorealm> results =
                pedido.where(Detallepedidorealm.class)
                                               .findAll();

        pedido.beginTransaction();
        results.deleteAllFromRealm();     // App crash
        pedido.commitTransaction();
        return results;
    }
    public final static List<PedidoRealm> Eliminartotalpedidos() {
        Realm pedido = Realm.getDefaultInstance();

        RealmResults<PedidoRealm> results =
                pedido.where(PedidoRealm.class)
                        .findAll();
        pedido.beginTransaction();
        results.deleteAllFromRealm();     // App crash
        return results;
    }




    @Override
    public void onResume(){
        super.onResume();
//        Toast.makeText(getApplication(),"estas onresume",Toast.LENGTH_LONG).show();

    }
    @Override
    public void onStop(){
        super.onStop();
  //      Toast.makeText(getApplication(),"onstop  ",Toast.LENGTH_LONG).show();

    }
    @Override
    public void onPause(){
        super.onPause();
    //    Toast.makeText(getApplication(),"onpause full",Toast.LENGTH_LONG).show();
      //int yi=idsupremodedetalle;
        //eliminaraTotalcrema(yi);
        //eliminarTotaladicional(yi);
        //eliminarTOTALdetallepedido(yi);

    }
    @Override
    public void onDestroy(){
        super.onDestroy();
      // Toast.makeText(getApplication(),"on detroy full",Toast.LENGTH_LONG).show();
  //      int yi=idsupremodedetalle;
    //    eliminaraTotalcrema(yi);
      //  eliminarTotaladicional(yi);
        //eliminarTOTALdetallepedido(yi);


    }




}




