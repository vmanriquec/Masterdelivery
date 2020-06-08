package com.mazinger.masterdelivery;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.mazinger.masterdelivery.Realm.Crudpedido;
import com.mazinger.masterdelivery.Realm.Detallepedidorealm;
import com.mazinger.masterdelivery.Realm.PedidoRealm;
import com.mazinger.masterdelivery.Realm.PedidoRealmFirebase;
import com.mazinger.masterdelivery.adapter.Adaptadorempresa;
import com.mazinger.masterdelivery.adapter.Adaptadorproductoempresa;

import com.mazinger.masterdelivery.adapter.Adaptadorrecibepedidos;
import com.mazinger.masterdelivery.modelo.Empresa;
import com.mazinger.masterdelivery.modelo.Productos;

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

import static com.mazinger.masterdelivery.Login.CONNECTION_TIMEOUT;
import static com.mazinger.masterdelivery.Login.READ_TIMEOUT;

public class Muestraproductosporempresa extends AppCompatActivity {
    String FileName = "myfile";
    SharedPreferences prefs;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muestraproductosporempresa);
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        toolbar = getSupportActionBar();

        prefs = getApplication().getSharedPreferences(FileName, Context.MODE_PRIVATE);
        cargarbarradeabajo();
        MultiAutoCompleteTextView myMultiAutoCompleteTextView
                = (MultiAutoCompleteTextView) findViewById(
                R.id.multiAutoCompleteTextView1);
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recyprodpe);


        Intent myIntent = getIntent();
        String idempresa = myIntent.getStringExtra("idempresa"); // will return "FirstKeyValue"
        String nombreempresa = myIntent.getStringExtra("nombreempresa"); // will return "FirstKeyValue"
        String telefonoempresa = myIntent.getStringExtra("telefonoempresa"); // will return "FirstKeyValue"
        String tiempodemora = myIntent.getStringExtra("tiempodemora"); // will return "FirstKeyValue"
        String montominimo = myIntent.getStringExtra("montominimo"); // will return "FirstKeyValue"

        guardarenshareempresaseleccionada(idempresa,nombreempresa,telefonoempresa,tiempodemora,montominimo);

        String nombre = prefs.getString("nombreusuariof", "");
        String direccion = prefs.getString("direccion", "");


        TextView nomusu = (TextView) findViewById(R.id.nomusu1);
        TextView direusu = (TextView) findViewById(R.id.direusu1);
        TextView fechausu = (TextView) findViewById(R.id.fechausu1);
        TextView costodelivery = (TextView) findViewById(R.id.costodelivery);
        TextView tiempodemorae = (TextView) findViewById(R.id.tiempodemora);
        TextView nombreep = (TextView) findViewById(R.id.nombreep);
        TextView botonlisto = (TextView) findViewById(R.id.cinco1);


        Button wasap = (Button) findViewById(R.id.wasapep);
        Button todos1 = (Button) findViewById(R.id.todos1);
        Button mispedidos = (Button) findViewById(R.id.mispedidosep);
        TextView tot = (TextView) findViewById(R.id.seis1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());

        fechausu.setText(currentDateandTime);
        nomusu.setText(nombre);
        direusu.setText(direccion);
        tiempodemorae.setText(tiempodemora);
        costodelivery.setText(montominimo);
        nombreep.setText(nombreempresa);
        TextView cantidadfragment = (TextView) findViewById(R.id.cuatro1);


        new traerproductospe().execute(idempresa);
        cargarbarradeabajo();
        wasap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + "51" + telefonoempresa + "&text=" + "Hola, ..."));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        mispedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView uno = (TextView) findViewById(R.id.nomusu1);
                String rer = uno.getText().toString();
                new traertodoslospedidosporclienteporempresa().execute(rer, idempresa);
            }
        });


        myMultiAutoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {

            String selected = (String) parent.getItemAtPosition(position);


            new traerproductospornombreempresa().execute(selected, idempresa);
        });

        todos1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new traerproductosporidalmacenidfamiliaempresa().execute(idempresa);
            }
        });
        try {
          //  String idi = myIntent.getStringExtra("idempresa"); // will return "FirstKeyValue"

            //String idalmacen = prefs.getString("idalmacenactivosf", "");
            String idi = prefs.getString("idempresa", "");

            crearpedidoinicial(1, 0.0, currentDateandTime, Integer.parseInt(idi));

        } catch (NumberFormatException e) {
            Log.i("tallo", e.toString());
        }

        Realm pedido = Realm.getDefaultInstance();
        RealmResults<Detallepedidorealm> results =
                pedido.where(Detallepedidorealm.class)
                        .findAll();
        int w = results.size();
        cantidadfragment.setText(String.valueOf(w));
        Double tt = 0.0;
        for (int i = 0; i < w; i++) {
            int gg = results.get(i).getCantidadrealm();
            int popo = results.get(i).getIdpedido();
            String lll = results.get(i).getNombreproductorealm();
            Double jjj = Double.parseDouble(results.get(i).getSubtotal());
            tt = tt + jjj;

        }
        tot.setText("S/. " + String.valueOf(tt));


        botonlisto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iraverpedidos();


            }
        });

        new llenarautocomplete().execute(idempresa);
    }

    private void iraverpedidos() {


        Intent i = new Intent(getApplication(), Verpedidodos.class);
        startActivity(i);
    }

    private class traerproductospe extends AsyncTask<String, String, String> {
        HttpURLConnection conne;
        URL url = null;
        ArrayList<Productos> listaalmaceno = new ArrayList<Productos>();
        ProgressDialog pdLoading = new ProgressDialog(Muestraproductosporempresa.this);

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            pdLoading.setMessage("\tCargando productos :)");
            pdLoading.setCancelable(false);
            pdLoading.show();


        }

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL("https://sodapop.pe/sugest/apitraerproductosporempresa.php");
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
            Productos mesoproducto;
            ArrayList<Productos> peopleproducto = new ArrayList<>();
            ArrayList<String> dataListproducto = new ArrayList<String>();
            Adaptadorproductoempresa adapterproducto;
            RecyclerView recyclerproducto = (RecyclerView) findViewById(R.id.recyprodpe);
            pdLoading.dismiss();
            peopleproducto.clear();
            String[] strArrDataproducto = {"No Suggestions"};
            if (result.equals("no rows")) {
            } else {
                try {
                    JSONArray jArray = new JSONArray(result);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.optJSONObject(i);
                        mesoproducto = new Productos(json_data.getInt("idproducto"), json_data.getString("nombreproducto"), json_data.getString("estadoproducto"), json_data.getString("ingredientes"), json_data.getDouble("precventa"), json_data.getString("descripcion"));
                        peopleproducto.add(mesoproducto);
                    }
                    strArrDataproducto = dataListproducto.toArray(new String[dataListproducto.size()]);
                    adapterproducto = new Adaptadorproductoempresa(peopleproducto, getApplicationContext());
                    recyclerproducto.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                    recyclerproducto.setAdapter(adapterproducto);
                    adapterproducto.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.d("erroro", e.toString());
                }
            }

        }
    }

    private void cargarbarradeabajo() {
        TextView tot = (TextView) findViewById(R.id.seis1);
        TextView cantidadfragment = (TextView) findViewById(R.id.cuatro1);

        Realm.init(getApplicationContext());
        Realm pedido = Realm.getDefaultInstance();
        RealmResults<Detallepedidorealm> results =
                pedido.where(Detallepedidorealm.class)
                        .findAll();
        int w = results.size();
        cantidadfragment.setText(String.valueOf(w));
        Double tt = 0.0;
        for (int i = 0; i < w; i++) {
            int gg = results.get(i).getCantidadrealm();
            int popo = results.get(i).getIdpedido();
            String lll = results.get(i).getNombreproductorealm();
            Double jjj = Double.parseDouble(results.get(i).getSubtotal());
            tt = tt + jjj;

        }
        tot.setText("S/. " + String.valueOf(tt));


    }

    public class traertodoslospedidosporclienteporempresa extends AsyncTask<String, String, String> {

        HttpURLConnection conne;
        URL url = null;
        ProgressDialog pdLoading = new ProgressDialog(Muestraproductosporempresa.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tCargando pedidos");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                url = new URL("https://sodapop.pe/sugest/traerpedidosporclienteempresa.php");
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
                        .appendQueryParameter("nombreusuario", params[0])
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
            ArrayList<PedidoRealmFirebase> todoslospedidos = new ArrayList<>();

            todoslospedidos.clear();
            pdLoading.dismiss();
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    PedidoRealmFirebase pedidofirebase = new PedidoRealmFirebase
                            (json_data.getInt("idpedido"), json_data.getInt("idcliente"), json_data.getInt("idmesa")
                                    , json_data.getDouble("totalpedido"), json_data.getString("estadopedido"), json_data.getString("fechapedido")
                                    , json_data.getInt("idusuario"), json_data.getInt("idalmacen"), json_data.getString("idfacebook"),
                                    json_data.getString("observaciones"), json_data.getString("llevar"), json_data.getString("direccionllevar")
                                    , json_data.getString("idfirebase"), json_data.getString("monbredescuento"), json_data.getString("montodescuento")
                                    , json_data.getString("nombrecosto"), json_data.getString("montocosto"), json_data.getString("longitud"),
                                    json_data.getString("latitud"), json_data.getString("pagocliente"), json_data.getString("vuelto"),
                                    json_data.getString("telefono"), json_data.getString("refrencias"), json_data.getString("nombreusuariof")
                                    , json_data.getString("idempresa"));
                    todoslospedidos.add(pedidofirebase);


                }


                final Dialog dialog = new Dialog(Muestraproductosporempresa.this);
                dialog.setContentView(R.layout.dialogopedidos);
                DisplayMetrics metrics = getResources().getDisplayMetrics();
                int width = metrics.widthPixels;
                int height = metrics.heightPixels;

                dialog.getWindow().setLayout((7 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);
                TextView clie = (TextView) dialog.findViewById(R.id.nombredialog);
                clie.setText(todoslospedidos.get(0).getNombreusuario());

                final RecyclerView pedidose = dialog.findViewById(R.id.listadepedidos);

                Adaptadorrecibepedidos adaptadore = new Adaptadorrecibepedidos(todoslospedidos, dialog.getContext());
                pedidose.setLayoutManager(new GridLayoutManager(Muestraproductosporempresa.this.getApplicationContext(), 1));

                pedidose.setAdapter(adaptadore);
                adaptadore.notifyDataSetChanged();
                dialog.show();


            } catch (JSONException e) {


                Log.d("erororor", e.toString());
            }


        }

    }

    private class traerproductospornombreempresa extends AsyncTask<String, String, String> {

        HttpURLConnection conne;
        URL url = null;
        ArrayList<Productos> listaalmaceno = new ArrayList<Productos>();
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recyprodpe);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {

            try {
                url = new URL("https://sodapop.pe/sugest/apitraerproductospornombreempresa.php");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {
                conne = (HttpURLConnection) url.openConnection();
                conne.setReadTimeout(READ_TIMEOUT);
                conne.setConnectTimeout(CONNECTION_TIMEOUT);
                conne.setRequestMethod("GET");
                conne.setDoInput(true);
                conne.setDoOutput(true);

                // Append parameters to URL


                Uri.Builder builder = new Uri.Builder()

                        .appendQueryParameter("nombreproducto", params[0])
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
            ArrayList<Productos> people = new ArrayList<>();
            String[] strArrData = {"No Suggestions"};

            people.clear();
            RecyclerView.Adapter adapter;


            ArrayList<String> dataList = new ArrayList<String>();
            Productos meso;
            if (result.equals("no rows")) {
                Toast.makeText(Muestraproductosporempresa.this, "no existen datos a mostrar", Toast.LENGTH_LONG).show();

            } else {
                try {
                    JSONArray jArray = new JSONArray(result);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.optJSONObject(i);
                        meso = new Productos(json_data.getInt("idproducto"), json_data.getString("nombreproducto"), json_data.getString("estadoproducto"), json_data.getString("ingredientes"), json_data.getDouble(("precventa")), json_data.getString("descripcion"));
                        people.add(meso);
                    }
                    strArrData = dataList.toArray(new String[dataList.size()]);
                    recycler.removeAllViews();
                    recycler.setAdapter(null);
                    adapter = new Adaptadorproductoempresa(people, Muestraproductosporempresa.this.getApplicationContext());
                    recycler.setLayoutManager(new GridLayoutManager(Muestraproductosporempresa.this.getApplicationContext(), 1));
                    recycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {

                }
            }
        }
    }

    private class traerproductosporidalmacenidfamiliaempresa extends AsyncTask<String, String, String> {
        ArrayList<Productos> people = new ArrayList<>();
        private String[] strArrData = {"No Suggestions"};

        HttpURLConnection conne;
        URL url = null;
        ArrayList<Productos> listaalmaceno = new ArrayList<Productos>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {

            try {
                url = new URL("https://sodapop.pe/sugest/apitraerproductosmaestraporempresa.php");
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
            RecyclerView.Adapter adapter;
            people.clear();
            RecyclerView recycler = (RecyclerView) findViewById(R.id.recyprodpe);


            ArrayList<String> dataList = new ArrayList<String>();
            Productos meso;
            if (result.equals("no rows")) {
                Toast.makeText(Muestraproductosporempresa.this.getApplicationContext(), "no existen datos a mostrar", Toast.LENGTH_LONG).show();

            } else {

                try {


                    JSONArray jArray = new JSONArray(result);


                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.optJSONObject(i);

                        meso = new Productos(json_data.getInt("idproducto"), json_data.getString("nombreproducto"), json_data.getString("estadoproducto"), json_data.getString("ingredientes"), json_data.getDouble(("precventa")), json_data.getString("descripcion"));
                        people.add(meso);

                    }
                    strArrData = dataList.toArray(new String[dataList.size()]);


                    adapter = new Adaptadorproductoempresa(people, Muestraproductosporempresa.this.getApplicationContext());
                    recycler.setLayoutManager(new GridLayoutManager(Muestraproductosporempresa.this.getApplicationContext(), 3));

                    recycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {

                }

            }

        }

    }

    public final static void crearpedidoinicial(int idusuario, Double totalpedido, String fechapedido, int idalmacen) {
        Realm pedido = Realm.getDefaultInstance();
        pedido.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm pedido) {
                int index = Crudpedido.calculateIndex();
                PedidoRealm realmDetallepedidorealm = pedido.createObject(PedidoRealm.class, index);
                realmDetallepedidorealm.setIdusuario(idusuario);
                realmDetallepedidorealm.setTotalpedido(totalpedido);
                realmDetallepedidorealm.setFechapedido(fechapedido);
                realmDetallepedidorealm.setIdalmacen(idalmacen);
            }
        });
    }

    private class llenarautocomplete extends AsyncTask<String, String, String> {
        ArrayList<Productos> people = new ArrayList<>();
        private String[] strArrData = {"No Suggestions"};

        HttpURLConnection conne;
        URL url = null;
        ArrayList<Productos> listaalmaceno = new ArrayList<Productos>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {

            try {
                url = new URL("https://sodapop.pe/sugest/apitraertodoslosproductosporalmacenempresa.php");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {
                conne = (HttpURLConnection) url.openConnection();
                conne.setReadTimeout(READ_TIMEOUT);
                conne.setConnectTimeout(CONNECTION_TIMEOUT);
                conne.setRequestMethod("GET");
                conne.setDoInput(true);
                conne.setDoOutput(true);
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
            ArrayList<String> mylist = new ArrayList<String>();
            people.clear();


            ArrayList<String> dataList = new ArrayList<String>();
            Productos meso;
            if (result.equals("no rows")) {
                Toast.makeText(Muestraproductosporempresa.this.getApplicationContext(), "no existen datos a mostrar", Toast.LENGTH_LONG).show();

            } else {

                try {


                    JSONArray jArray = new JSONArray(result);


                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.optJSONObject(i);

                        meso = new Productos(json_data.getInt("idproducto"), json_data.getString("nombreproducto"), json_data.getString("estadoproducto"), json_data.getString("ingredientes"), json_data.getDouble(("precventa")), json_data.getString("descripcion"));
                        people.add(meso);

                        mylist.add(json_data.getString("nombreproducto"));

                    }
                    strArrData = dataList.toArray(new String[dataList.size()]);


                    MultiAutoCompleteTextView myMultiAutoCompleteTextView1
                            = (MultiAutoCompleteTextView) findViewById(
                            R.id.multiAutoCompleteTextView1);
                    myMultiAutoCompleteTextView1.setAdapter(
                            new ArrayAdapter<String>(Muestraproductosporempresa.this.getApplicationContext(), android.R.layout.simple_dropdown_item_1line, mylist));
                    myMultiAutoCompleteTextView1.setTokenizer(
                            new MultiAutoCompleteTextView.CommaTokenizer());


                } catch (JSONException e) {

                }

            }

        }

    }
    @Override
    public void onBackPressed() {
        TextView cuatro1=(TextView)findViewById(R.id.cuatro1);
        if (cuatro1.getText().toString().equals("0"))
        {

            Intent pi;
            pi = new Intent(this,Muestartodaslasempresas.class);
            startActivity(pi);
        }else
        {
Toast.makeText(Muestraproductosporempresa.this,"Tienes un producto de esta tienda en canasta",Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public void onResume(){
        super.onResume();
//        Toast.makeText(getApplication(),"estas onresume",Toast.LENGTH_LONG).show();
        cargarbarradeabajo();
    }
    @Override
    public void onStop(){
        super.onStop();
        cargarbarradeabajo();
        //      Toast.makeText(getApplication(),"onstop  ",Toast.LENGTH_LONG).show();

    }
    @Override
    public void onPause() {
        super.onPause();
        cargarbarradeabajo();
        //    Toast.makeText(getApplication(),"onpause full",Toast.LENGTH_LONG).show();
        //int yi=idsupremodedetalle;
        //eliminaraTotalcrema(yi);
        //eliminarTotaladicional(yi);
        //eliminarTOTALdetallepedido(yi);
    }


    public   void guardarenshareempresaseleccionada(String idempresa,String nombreempresa,String telefonoempresa,String tiempodemora,
                                                    String montominimo){
        SharedPreferences sharedPreferences =getSharedPreferences(FileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("idempresa",idempresa);
        editor.putString("nombreempresa",nombreempresa);
        editor.putString("telefonoempresa",telefonoempresa);
        editor.putString("tiempodemora",tiempodemora);
        editor.putString("montominimo",montominimo);

        editor.commit();

    }

}