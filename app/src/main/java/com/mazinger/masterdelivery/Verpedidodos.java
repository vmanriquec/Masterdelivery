package com.mazinger.masterdelivery;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.mazinger.masterdelivery.Realm.Detallepedidorealm;
import com.mazinger.masterdelivery.adapter.Adaptadordialogo;
import com.mazinger.masterdelivery.modelo.Datostarjetadialogo;
import com.mazinger.masterdelivery.modelo.Tiposdepago;
import com.mazinger.masterdelivery.modelo.Tiposdeservicio;
import com.mazinger.masterdelivery.modelo.Usuariodirecciones;
import com.mazinger.masterdelivery.modelo.Valedescuento;

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

import io.realm.Realm;
import io.realm.RealmResults;

public class Verpedidodos extends AppCompatActivity  {
    String[] strArrDataventaso = {"No Suggestions"};
    ArrayList<String> dataListventitas = new ArrayList<String>();
    RecyclerView gggg;
    Context context;
    TextView  frito;
    Intent pi;

    TextView tio;
    String FileName = "myfile";
    SharedPreferences prefs;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    Valedescuento vale;;
    TextView descuento;
    TextView deliverycosto;
    TextView latidudd;
    TextView longitudd;
    TextView nombredescuento,totalpedidoapedir,mivion,horaderecojo;
    Button pidelo;
    Spinner tipodepago,tipodeservicio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verpedido);
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();
     frito =(TextView) findViewById(R.id.num);
        tio=(TextView) findViewById(R.id.miavion);
pidelo=(Button) findViewById(R.id.irafirebase);

horaderecojo=(EditText)findViewById(R.id.horaderecojo);
Button cancelartodo=(Button)findViewById(R.id.cancelarpe);
        totalpedidoapedir=(TextView) findViewById(R.id.totalpedidoapedir);
         deliverycosto=(TextView) findViewById(R.id.montodelvery);

         descuento=(TextView) findViewById(R.id.montodescuento);
         nombredescuento=(TextView) findViewById(R.id.nombredescuento);
         mivion=(TextView)findViewById(R.id.miavion);
         tipodepago=(Spinner) findViewById(R.id.spinnertipodepago);
tipodeservicio=(Spinner) findViewById(R.id.spinnertiposdeservicio);

        pi = new Intent(this,Enviarpedido.class);




        Realm.init(getApplication());

        prefs = getApplication().getSharedPreferences(FileName, Context.MODE_PRIVATE);



        String di = prefs.getString("direccion", "");






        String idfirebase = prefs.getString("idfirebase", "");


        Switch pago=(Switch)findViewById(R.id.dinero);

cancelartodo.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        borrartodo();
        Intent i = new Intent(getApplicationContext(), Muestartodaslasempresas.class);
        startActivity(i);
    }
});




     

        new manejardirecciones().execute(idfirebase);

       String  idempresa = prefs.getString("idempresa", "");

        new manejartiposdepago().execute(idempresa);
        new manejartiposdeservicio().execute(idempresa);
        new traerdescuentosycosodedelivery().execute(idfirebase,idempresa);
              recargartotalesisisomos();


              pidelo.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {

                      EditText apagar=(EditText) findViewById(R.id.dacliente);
                      if(pago.isChecked()){
                          guardarcuantopagacliente(totalpedidoapedir.getText().toString(),"0",totalpedidoapedir.getText().toString());
                          apagar.setText("");
                          apagar.setEnabled(false);
                          apagar.setVisibility(View.INVISIBLE);
                          apagar.setActivated(false);
                           enviarfirebase();
                      }else{
                          apagar.setVisibility(View.VISIBLE);
                          apagar.setEnabled(true);
                          apagar.setActivated(true);

                          Double gi=Double.parseDouble(totalpedidoapedir.getText().toString());

                          if(apagar.getText().toString().equals("")){


                              BottomSheetFragment bottomSheetDialog = BottomSheetFragment.newInstance();

                              String nombre = prefs.getString("nombreusuariof", "");

                              Bundle bundle = new Bundle();
                              bundle.putString("test", "Debes pagar con algo");
                              bundle.putString("nombreusuario", nombre);

                              bottomSheetDialog.setArguments(bundle);
                              bottomSheetDialog.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");

                              //Toast.makeText(getApplication(),"Debes pagar con algo",Toast.LENGTH_LONG).show();
                          }else{
                              Double g=Double.parseDouble(apagar.getText().toString());

                              if (g<gi){


                                  BottomSheetFragment bottomSheetDialog = BottomSheetFragment.newInstance();

                                  String nombre = prefs.getString("nombreusuariof", "");

                                  Bundle bundle = new Bundle();
                                  bundle.putString("test", "Estas pagando menos del Monto pedido");
                                  bundle.putString("nombreusuario", nombre);

                                  bottomSheetDialog.setArguments(bundle);
                                  bottomSheetDialog.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");

//                                  Toast.makeText(getApplication(),"Estas pagando menos del pedido",Toast.LENGTH_LONG).show();
                                  apagar.setText("");
                              }
                              else{
                                  guardarcuantopagacliente(apagar.getText().toString(),String.valueOf(Double.parseDouble(apagar.getText().toString())-Double.parseDouble(totalpedidoapedir.getText().toString())),totalpedidoapedir.getText().toString());
                                  enviarfirebase();
                                  apagar.setText("");
                              }
                          }




                      }

                  }
              });

    }

    public  void recargartotalesisisomos() {
        ArrayList<Datostarjetadialogo> peopleventas = new ArrayList<>();
        peopleventas.clear();
        ArrayList<Datostarjetadialogo> datosdetodaslastarjetas = new ArrayList<>();
        ArrayList<String> datalisttarjeta = new ArrayList<String>();
        String[] strtarjeta = {"No Suggestions"};
        datosdetodaslastarjetas.clear();

        Realm pedido = Realm.getDefaultInstance();
        RealmResults<Detallepedidorealm> results =
                pedido.where(Detallepedidorealm.class)
                        .findAll();
        int w = results.size();

        Double tt=0.0;
        for (int i = 0; i < w; i++){
            int gg=results.get(i).getCantidadrealm();
            int  popo=results.get(i).getIdpedido();
            String lll=results.get(i).getNombreproductorealm();
            Double jjj=Double.parseDouble(results.get(i).getSubtotal());
            tt=tt+jjj;
            Datostarjetadialogo datoso =new Datostarjetadialogo(popo,gg,lll,jjj);
            peopleventas.add(datoso);
        }
        frito.setText(String.valueOf(w));
        tio.setText(String.valueOf(tt));
        strArrDataventaso = dataListventitas.toArray(new String[dataListventitas.size()]);
        Adapter  adapterventas = new Adaptadordialogo(peopleventas,this);
        RecyclerView gggg  = (RecyclerView) findViewById(R.id.recyclerdepedidos);
        gggg.setLayoutManager(new LinearLayoutManager(this));
        gggg.setAdapter(adapterventas);





    }
    public void calculatotal() {



        deliverycosto.setText(vale.getMontocosto());
        nombredescuento.setText(vale.getNombredescuento());
        descuento.setText(vale.getMontodescuento());
        Double des,miavi,deli;



        if (descuento.equals("")){
            des=0.0;
        }else{

            des=Double.parseDouble(vale.getMontodescuento().toString());
        }

        miavi=Double.valueOf(mivion.getText().toString());
        if(miavi==0.0){


            totalpedidoapedir.setText(String.valueOf(0.0));

        }else{

            deli=Double.valueOf(vale.getMontocosto());
            Double ll=(miavi-des)+deli;

            totalpedidoapedir.setText(String.valueOf(ll));


        }


    }


    public void onEventName(String k) {
        context = this;

        frito.setText(k);


    }
    private void cargarbarradeabajo() {
        TextView tot=(TextView) findViewById(R.id.seis);
        TextView cantidadfragment=(TextView) findViewById(R.id.cuatro);

        Realm.init(getApplicationContext());
        Realm pedido = Realm.getDefaultInstance();
        RealmResults<Detallepedidorealm> results =
                pedido.where(Detallepedidorealm.class)
                        .findAll();
        int w = results.size();
        cantidadfragment.setText(String.valueOf(w));
        Double tt=0.0;
        for (int i = 0; i < w; i++){
            int gg=results.get(i).getCantidadrealm();
            int  popo=results.get(i).getIdpedido();
            String lll=results.get(i).getNombreproductorealm();
            Double jjj=Double.parseDouble(results.get(i).getSubtotal());
            tt=tt+jjj;

        }
        tot.setText("S/. "+String.valueOf(tt));



    }


    private class traerdescuentosycosodedelivery extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(Verpedidodos.this);
        HttpURLConnection conne;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tCargando costos y descuentos disponibles");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params)

        {

            try {
                url = new URL("https://sodapop.pe/sugest/apitraerdescuentosydelivery.php");
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

                        .appendQueryParameter("idfirebase", params[0])
                        .appendQueryParameter("idempresa", params[1]);

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
                    return (result.toString());

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
            Log.i("iooooo", result);




            pdLoading.dismiss();

if (result.toString().equals("no rows\t\t  \t\t  \t\t ")){
    String  idempresa = prefs.getString("idempresa", "");
   new  solotraecosodedelivery().execute(idempresa);
}
else{

    JSONArray jArray = null;
    try {
        jArray = new JSONArray(result);
    } catch (JSONException e) {
        e.printStackTrace();
    }
    for (int i = 0; i < jArray.length(); i++) {
        JSONObject json_data = jArray.optJSONObject(i);
        try {
            vale = new Valedescuento(json_data.getString("nombredescuento"), json_data.getString("montodescuento"), json_data.getString("montocosto"), "Costo Delivery");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    calculatotal();
    gusrdarcostosdescuentos(vale.getNombredescuento(),vale.getMontodescuento(),vale.getNombrecosto(),vale.getMontocosto());






}
}
        }


    private class solotraecosodedelivery extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(Verpedidodos.this);
        HttpURLConnection conne;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tCargando costos y descuentos disponibles");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params)

        {

            try {
                url = new URL("https://sodapop.pe/sugest/solotraecostodelivery.php");
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


                        .appendQueryParameter("idempresa", params[0]);

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
                    return (result.toString());

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
            Log.i("iooooo", result);




            pdLoading.dismiss();
            if (result.toString().equals("no rows")){


            }
            else{

                JSONArray jArray = null;
                try {
                    jArray = new JSONArray(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.optJSONObject(i);
                    try {
                        vale = new Valedescuento("Descuento ", "0", json_data.getString("montocosto"), "Costo Delivery");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                calculatotal();
                gusrdarcostosdescuentos(vale.getNombredescuento(),vale.getMontodescuento(),vale.getNombrecosto(),vale.getMontocosto());






            }
        }
    }





    public   void gusrdarcostosdescuentos(String nombredescuento,String montodescuento,String nombrecosto,String montocosto){
        SharedPreferences sharedPreferences =getSharedPreferences(FileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.putString("nombredescuento",nombredescuento);
        editor.putString("montodescuento",montodescuento);
        editor.putString("nombrecosto",nombrecosto);
        editor.putString("montocosto",montocosto);
        editor.commit();

    }

    public   void guardarcuantopagacliente(String cuantopagacliente,String vuelto,String totalpedido){
        SharedPreferences sharedPreferences =getSharedPreferences(FileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.putString("cuantopagacliente",cuantopagacliente);
        editor.putString("vuelto",vuelto);
        editor.putString("totalpedido",totalpedido);
        editor.commit();

    }

    private void enviarfirebase() {
        Spinner s=(Spinner)findViewById(R.id.spinnerdirecciones);
        String al =s.getItemAtPosition(s.getSelectedItemPosition()).toString();
        String mesei=al;
        int g= mesei.length();
        String mesi = mesei.substring(0,2);

        String  idalmacenactivosf=mesi.trim();

        String mesio = mesei.substring(3,g);
        String almacenactivosf=mesio.trim();

        new traerlatylong().execute(almacenactivosf);

    }
    private void borrartodo() {


            Realm pedido = Realm.getDefaultInstance();
            pedido.beginTransaction();
            pedido.deleteAll();
            pedido.commitTransaction();




    }


    public class manejartiposdepago extends AsyncTask<String, String, String> {

        HttpURLConnection conne;
        URL url = null;
            ProgressDialog pdLoading = new ProgressDialog(Verpedidodos.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tMostrando tipos de pago de la empresa");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                //  url = new URL("https://sodapop.pe/sugest/apitraertodoslostiposdepago.php");
                url = new URL("https://sodapop.pe/sugest/apitraertiposdepagoporempresa.php");
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
                        .appendQueryParameter("idempresa", params[0]);
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
                Log.d("wiwo", e1.toString());
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
                    Log.d("waaaaaaa", result.toString());
                    return (result.toString());

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
            ArrayList<Tiposdepago> todoslotiposdepago = new ArrayList<>();
            tipodepago=(Spinner) findViewById(R.id.spinnertipodepago);
            todoslotiposdepago.clear();
            pdLoading.dismiss();
            try {



                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    Tiposdepago pedidofirebase = new Tiposdepago(
                            (json_data.getInt("idtiposdepago")), json_data.getString("nombretiposdepago"), json_data.getString("estadotiposdepago"));
                    todoslotiposdepago.add(pedidofirebase);


                }

                 ArrayAdapter<Tiposdepago> adaptadorl= new ArrayAdapter<Tiposdepago>(Verpedidodos.this, android.R.layout.simple_spinner_item,todoslotiposdepago );
                tipodepago.setAdapter(adaptadorl);

            } catch (JSONException e) {
                Log.d("erororor", e.toString());
            }
        }

    }
    public class manejartiposdeservicio extends AsyncTask<String, String, String> {

        HttpURLConnection conne;
        URL url = null;
        ProgressDialog pdLoading = new ProgressDialog(Verpedidodos.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tMostrando tipos de atencion de la empresa");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                //  url = new URL("https://sodapop.pe/sugest/apitraertodoslostiposdepago.php");
                url = new URL("https://sodapop.pe/sugest/apitraertiposdeatencionporempresa.php");
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
                        .appendQueryParameter("idempresa", params[0]);
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
                Log.d("wiwo", e1.toString());
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
                    Log.d("waaaaaaa", result.toString());
                    return (result.toString());

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
            ArrayList<Tiposdeservicio> todoslotiposdeservicio = new ArrayList<>();
            tipodeservicio=(Spinner)findViewById(R.id.spinnertiposdeservicio);
            todoslotiposdeservicio.clear();
            pdLoading.dismiss();
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    Tiposdeservicio pedidofirebase = new Tiposdeservicio(

                            (json_data.getInt("idtipodeatencion")), json_data.getString("nombretipodeatencion"), json_data.getString("estadotipodeatencion"),
                            json_data.getString("imagentipoatencion"));
                    todoslotiposdeservicio.add(pedidofirebase);


                }

                ArrayAdapter<Tiposdeservicio> adaptadorl= new ArrayAdapter<Tiposdeservicio>(Verpedidodos.this, android.R.layout.simple_spinner_item,todoslotiposdeservicio );
                tipodeservicio.setAdapter(adaptadorl);


            } catch (JSONException e) {
                Log.d("erororor", e.toString());
            }
        }

    }








    public class manejardirecciones extends AsyncTask<String, String, String> {

        HttpURLConnection conne;
        URL url = null;
        ProgressDialog pdLoading = new ProgressDialog(Verpedidodos.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tcargando tus direcciones registradas");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                //  url = new URL("https://sodapop.pe/sugest/apitraertodoslostiposdepago.php");
                url = new URL("https://sodapop.pe/sugest/traerdireccionesporusuario.php");
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
                        .appendQueryParameter("idfirebase", params[0]);
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
                Log.d("wiwo", e1.toString());
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
                    Log.d("waaaaaaa", result.toString());
                    return (result.toString());

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
            ArrayList<Usuariodirecciones> DIR = new ArrayList<>();
           Spinner spinnerdirecciones=(Spinner)findViewById(R.id.spinnerdirecciones);
            DIR.clear();
            pdLoading.dismiss();
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    Usuariodirecciones pedidofirebaseO = new Usuariodirecciones(
                            (json_data.getInt("idusuariodireccione")), json_data.getString("idfirebase"),
                            json_data.getString("direccion"),
                            json_data.getString("longitud"),
                            json_data.getString("latitud"),
                            json_data.getString("referencia"));
                    DIR.add(pedidofirebaseO);


                }

                ArrayAdapter<Usuariodirecciones> adaptadorlL= new ArrayAdapter<Usuariodirecciones>(Verpedidodos.this, android.R.layout.simple_spinner_item,DIR );
                spinnerdirecciones.setAdapter(adaptadorlL);


            } catch (JSONException e) {
                Log.d("erororor", e.toString());
            }
        }

    }




    public class traerlatylong extends AsyncTask<String, String, String> {

        HttpURLConnection conne;
        URL url = null;
        ProgressDialog pdLoading = new ProgressDialog(Verpedidodos.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tcargando");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                //  url = new URL("https://sodapop.pe/sugest/apitraertodoslostiposdepago.php");
                url = new URL("https://sodapop.pe/sugest/traerlatlongpordire.php");
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
                        .appendQueryParameter("direccion", params[0]);
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
                Log.d("wiwo", e1.toString());
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
                    Log.d("waaaaaaa", result.toString());
                    return (result.toString());

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
            ArrayList<Usuariodirecciones> DIR = new ArrayList<>();
            DIR.clear();
            pdLoading.dismiss();
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    Usuariodirecciones pedidofirebaseO = new Usuariodirecciones(
                            (json_data.getInt("idusuariodireccione")), json_data.getString("idfirebase"),
                            json_data.getString("direccion"),
                            json_data.getString("longitud"),
                            json_data.getString("latitud"),
                            json_data.getString("referencia"));
                    DIR.add(pedidofirebaseO);


                }

String fff=DIR.get(0).getLatitud().toString();
                String vvv=DIR.get(0).getLongitud().toString();


                Spinner s=(Spinner)findViewById(R.id.spinnerdirecciones);
                String al =s.getItemAtPosition(s.getSelectedItemPosition()).toString();
                String mesei=al;
                int g= mesei.length();
                String mesi = mesei.substring(0,2);

                String  idalmacenactivosf=mesi.trim();

                String mesio = mesei.substring(3,g);
                String almacenactivosf=mesio.trim();



                String typo;

                if (horaderecojo.getText().toString().equals(""))
                {
                    typo="o";
                }else{
                    typo="    ira a recojer a las "+horaderecojo.getText().toString();
                }
                pi.putExtra("horaaentregar",typo);
                pi.putExtra("direccionseleccionada",almacenactivosf);
                 pi.putExtra("latitudd",fff);
                pi.putExtra("longitudd",vvv);

                startActivity(pi);


            } catch (JSONException e) {
                Log.d("erororor", e.toString());
            }
        }

    }

}
