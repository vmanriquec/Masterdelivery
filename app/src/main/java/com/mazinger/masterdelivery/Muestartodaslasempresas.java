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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mazinger.masterdelivery.Realm.Detallepedidorealm;
import com.mazinger.masterdelivery.adapter.Adaptadorempresa;
import com.mazinger.masterdelivery.modelo.Empresa;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class Muestartodaslasempresas extends AppCompatActivity {
    String FileName = "myfile";
    SharedPreferences prefs;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private ActionBar toolbar;
    FloatingActionButton fab;
    TextView fabi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitymuestratodaslasempresas);
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();
        Realm.init(getApplicationContext());


        Realm pedido = Realm.getDefaultInstance();
        pedido.beginTransaction();
        pedido.delete(Detallepedidorealm.class);
        pedido.deleteAll();
        pedido.commitTransaction();

        RealmResults<Detallepedidorealm> results =
                pedido.where(Detallepedidorealm.class)
                        .findAll();
        int w = results.size();



        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        prefs = getApplication().getSharedPreferences(FileName, Context.MODE_PRIVATE);
        String nombre = prefs.getString("nombreusuariof", "");
        String direccion = prefs.getString("direccion", "");


        TextView fechausu=(TextView) findViewById(R.id.fechausu);

        TextView abuscarbu=(TextView) findViewById(R.id.abuscarbu);
        Button abuscarbuboton=(Button) findViewById(R.id.abuscarbuboton);

abuscarbu.setHint("Hola "+ nombre+ ", busca un producto...");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());
        fechausu.setText(currentDateandTime);

        new mostrartodaslasempresas().execute("w");

        abuscarbuboton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(abuscarbu.getText().toString().equals(""))
                {
                    Toast.makeText(Muestartodaslasempresas.this,"Debes ecribir algo a buscar",Toast.LENGTH_LONG).show();
                }else{

                    new mostratodaslaasempresasporbusqueda().execute(abuscarbu.getText().toString());
                }
            }
        });
        // tot.setText("S/. " + String.valueOf(tt));





    }


    public class mostrartodaslasempresas extends AsyncTask<String, String, String> {

        HttpURLConnection conne;
        URL url = null;
        ProgressDialog pdLoading = new ProgressDialog(Muestartodaslasempresas.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tMostrando todas las empresas");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                //  url = new URL("https://sodapop.pe/sugest/apitraertodoslostiposdepago.php");
                url = new URL("https://sodapop.pe/sugest/apitraertodaslasempresas.php");
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
            ArrayList<Empresa> todaslasempresas = new ArrayList<>();
            todaslasempresas.clear();
            pdLoading.dismiss();
            try {



                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);

                    Empresa pedidofirebase = new Empresa(
                            (
                                    json_data.getInt("idempresa")),
                            json_data.getString("razonsocialempresa"),
                            json_data.getString("direccionempresa"),
                            json_data.getString("telefonoempresa"),
                            json_data.getString("emailempresa"),
                            json_data.getString("paginawebempresa"),
                            json_data.getString("estadoempresa"),
                            json_data.getString("sloganempresa"),
                            json_data.getString("nombreadministrador"),
                            json_data.getString("telefonoadministrador"),
                            json_data.getString("logotipoempresa"),
                            json_data.getString("idrubroempresa"),

                            json_data.getString("montominimodeventa"),
                            json_data.getString("tiempodedemoraempresa"),
                            json_data.getString("nombrerubro"),
                            json_data.getString("costodelivery")

                    );
                    todaslasempresas.add(pedidofirebase);


                }

                final RecyclerView pedidose = findViewById(R.id.recydeempresasl);

                Adaptadorempresa adaptadore = new Adaptadorempresa(todaslasempresas, Muestartodaslasempresas.this);
                pedidose.setLayoutManager(new GridLayoutManager(Muestartodaslasempresas.this.getApplicationContext(), 1));

                pedidose.setAdapter(adaptadore);

                adaptadore.notifyDataSetChanged();

            } catch (JSONException e) {
                Log.d("erororor", e.toString());
            }
        }

    }

    public class mostratodaslaasempresasporbusqueda extends AsyncTask<String, String, String> {

        HttpURLConnection conne;
        URL url = null;
        ProgressDialog pdLoading = new ProgressDialog(Muestartodaslasempresas.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tMostrando todas las empresas que tengan el producto");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                //  url = new URL("https://sodapop.pe/sugest/apitraertodoslostiposdepago.php");
                url = new URL("https://sodapop.pe/sugest/apitraerempresasporproducto.php");
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
                        .appendQueryParameter("nombreproducto", params[0]);
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
            ArrayList<Empresa> todaslasempresas = new ArrayList<>();
            todaslasempresas.clear();
            pdLoading.dismiss();
            try {



                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);

                    Empresa pedidofirebase = new Empresa(
                            (
                                    json_data.getInt("idempresa")),
                            json_data.getString("razonsocialempresa"),
                            json_data.getString("direccionempresa"),
                            json_data.getString("telefonoempresa"),
                            json_data.getString("emailempresa"),
                            json_data.getString("paginawebempresa"),
                            json_data.getString("estadoempresa"),
                            json_data.getString("sloganempresa"),
                            json_data.getString("nombreadministrador"),
                            json_data.getString("telefonoadministrador"),
                            json_data.getString("logotipoempresa"),
                            json_data.getString("idrubroempresa"),
                            json_data.getString("montominimodeventa"),
                            json_data.getString("tiempodedemoraempresa"),
                            json_data.getString("nombreadministrador"),
                            json_data.getString("costodelivery")

                    );
                    todaslasempresas.add(pedidofirebase);


                }

                final RecyclerView pedidose = findViewById(R.id.recydeempresasl);

                Adaptadorempresa adaptadore = new Adaptadorempresa(todaslasempresas, Muestartodaslasempresas.this);
                pedidose.setLayoutManager(new GridLayoutManager(Muestartodaslasempresas.this.getApplicationContext(), 1));

                pedidose.setAdapter(adaptadore);

                adaptadore.notifyDataSetChanged();

            } catch (JSONException e) {
                Log.d("erororor", e.toString());
            }
        }

    }

    private void iraverpedidos() {
        Intent i = new Intent(getApplication(), Verpedidodos.class);
        startActivity(i);
    }
}

