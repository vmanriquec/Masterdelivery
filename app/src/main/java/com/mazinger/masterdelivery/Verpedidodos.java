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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.mazinger.masterdelivery.Realm.Detallepedidorealm;
import com.mazinger.masterdelivery.adapter.Adaptadordialogo;
import com.mazinger.masterdelivery.modelo.Datostarjetadialogo;
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
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class Verpedidodos extends AppCompatActivity  {
    String[] strArrDataventaso = {"No Suggestions"};
    ArrayList<String> dataListventitas = new ArrayList<String>();
    RecyclerView gggg;
    Context context;
    TextView  frito;
    TextView tio;
    String FileName = "myfile";
    SharedPreferences prefs;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    Valedescuento vale;;
    TextView descuento;
    TextView deliverycosto;
    TextView nombredescuento,totalpedidoapedir,mivion;
    Button pidelo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verpedido);
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();
     frito =(TextView) findViewById(R.id.num);
        tio=(TextView) findViewById(R.id.miavion);
pidelo=(Button) findViewById(R.id.irafirebase);
        totalpedidoapedir=(TextView) findViewById(R.id.totalpedidoapedir);
         deliverycosto=(TextView) findViewById(R.id.montodelvery);
         descuento=(TextView) findViewById(R.id.montodescuento);
         nombredescuento=(TextView) findViewById(R.id.nombredescuento);
         mivion=(TextView)findViewById(R.id.miavion);

        Realm.init(getApplication());
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("pedido.realm")
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(realmConfig);

        prefs = getApplication().getSharedPreferences(FileName, Context.MODE_PRIVATE);
        String di = prefs.getString("direccion", "");
        String idfirebase = prefs.getString("idfirebase", "");

        TextView yy=(TextView) findViewById(R.id.txtdireccion);
        Switch pago=(Switch)findViewById(R.id.dinero);


        yy.setText(di);
        new traerdescuentosycosodedelivery().execute(idfirebase);
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

                              Toast.makeText(getApplication(),"Debes pagar con algo",Toast.LENGTH_LONG).show();
                          }else{
                              Double g=Double.parseDouble(apagar.getText().toString());

                              if (g<gi){

                                  Toast.makeText(getApplication(),"Estas pagando menos del pedido",Toast.LENGTH_LONG).show();
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
            Log.d("iooooo", result);
            pdLoading.dismiss();

            JSONArray jArray = null;
            try {
                jArray = new JSONArray(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.optJSONObject(i);
                    try {
                        vale = new Valedescuento(json_data.getString("nombredescuento"), json_data.getString("montodescuento"), json_data.getString("montocosto"), json_data.getString("nombrecosto"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                calculatotal();
            gusrdarcostosdescuentos(vale.getNombredescuento(),vale.getMontodescuento(),vale.getNombrecosto(),vale.getMontocosto());






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
        Intent pi;
        pi = new Intent(this,Enviarpedido.class);
        startActivity(pi);
    }
}
