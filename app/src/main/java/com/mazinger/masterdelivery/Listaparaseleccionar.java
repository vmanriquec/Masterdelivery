package com.mazinger.masterdelivery;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mazinger.masterdelivery.Realm.Crudpedido;
import com.mazinger.masterdelivery.Realm.Detallepedidorealm;
import com.mazinger.masterdelivery.Realm.PedidoRealm;
import com.mazinger.masterdelivery.Realm.PedidoRealmFirebase;
import com.mazinger.masterdelivery.adapter.Adaptadorproductos;
import com.mazinger.masterdelivery.adapter.Adaptadorrecibepedidos;
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
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import static com.mazinger.masterdelivery.Login.CONNECTION_TIMEOUT;
import static com.mazinger.masterdelivery.Login.READ_TIMEOUT;

public class Listaparaseleccionar extends AppCompatActivity {
    String[] strArrDataventaso = {"No Suggestions"};

    public ArrayList<PedidoRealmFirebase> todoslospedidos = new ArrayList<>();

    String session, nombreususrio, almacenactivo, idalmacenactivo;
    String FileName = "myfile";
    SharedPreferences prefs;

    private String[] strArrData = {"No Suggestions"};
    private String[] strArrDataventas = {"No Suggestions"};
    private String[] strArrDataproducto = {"No Suggestions"};
    private String[] strArrDataproductopedido = {"No Suggestions"};
    private String[] strArrDatarecibe = {"No Suggestions"};
    private String[] strArrDatamovimientos = {"No Suggestions"};

    private RecyclerView recycler;
    private RecyclerView.Adapter adapterproducto;
    private RecyclerView.LayoutManager lManager;
    ArrayList<String> dataListproducto = new ArrayList<String>();
    Productos mesoproducto;
    private RecyclerView recyclerproducto;
    ArrayList<Productos> peopleproducto = new ArrayList<>();

    ArrayList<String> dataListventitas = new ArrayList<String>();
 String idalmacen;
 Button todos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listaparaseleccionar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();


        cargarbarradeabajo();
        TextView fechadehoy = (TextView) findViewById(R.id.tres);
        TextView usuariotxt = (TextView) findViewById(R.id.uno);
        TextView almacentxt = (TextView) findViewById(R.id.dos);
        TextView botonlisto = (TextView) findViewById(R.id.cinco);
        TextView tot = (TextView) findViewById(R.id.seis);
        TextView cantidadfragment = (TextView) findViewById(R.id.cuatro);

        todos=(Button)findViewById(R.id.todos);

        MultiAutoCompleteTextView myMultiAutoCompleteTextView
                = (MultiAutoCompleteTextView)findViewById(
                R.id.multiAutoCompleteTextView);
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recyclerlistado);

        int numberOfColumns = 6;
        recycler.setHasFixedSize(true);
        lManager = new LinearLayoutManager(Listaparaseleccionar.this);
        recycler.setLayoutManager(lManager);

        Button llamadai=(Button)findViewById(R.id.llamada);

        llamadai.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {



        PopupMenu popup=new PopupMenu(getApplication(),llamadai);

        popup.getMenuInflater().inflate(R.menu.menucito,popup.getMenu());
         popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getTitle().equals("Historial de Pedidos")){

TextView uno=(TextView)findViewById(R.id.uno);
String rer=uno.getText().toString();
new traertodoslospedidosporcliente().execute(rer);


                }
                if(item.getTitle().equals("whatsapp")){
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+"51910260813"+"&text="+"Hola, ..."));
                        startActivity(intent);
                    }catch (Exception e){
                        e.printStackTrace();
                    }



                }

   return true;
            }


        });
popup.show();



    }
});


        myMultiAutoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {

            String selected = (String) parent.getItemAtPosition(position);


            new traerproductospornombre().execute(selected);
        });


        todos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new traerproductosporidalmacenidfamilia().execute("1");
            }
        });

        Realm.init(getApplication());
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("pedido.realm")
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(realmConfig);

        prefs = getApplication().getSharedPreferences(FileName, Context.MODE_PRIVATE);
        String usuarior = prefs.getString("nombreusuariof", "");
        String almacennombre = prefs.getString("almacenactivosf", "");
        idalmacen = prefs.getString("idalmacenactivosf", "");


        usuariotxt.setText(usuarior);
        almacentxt.setText(almacennombre);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());
        fechadehoy.setText(currentDateandTime);


        crearpedidoinicial(1,0.0,currentDateandTime,Integer.parseInt(idalmacen));



        ListView lista = (ListView) findViewById(R.id.listainicio);
        recyclerproducto = (RecyclerView) findViewById(R.id.recyclerlistado);

        recyclerproducto.setHasFixedSize(true);
        recyclerproducto.addItemDecoration(new DividerItemDecoration(getApplication(), LinearLayoutManager.VERTICAL));

    /////////////////////////////////////////////////////////////////    int numberOfColumns = 6;
        //////////////////////////////////////////////////////
        recyclerproducto.setHasFixedSize(true);
        lManager = new LinearLayoutManager(getApplication());
        recyclerproducto.setLayoutManager(lManager);

        startASycnc();
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

        new llenarautocomplete().execute("1");
    }

    private void iraverpedidos() {


        Intent i= new Intent(getApplication(),Verpedidodos.class);
        startActivity(i);
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

    public void startASycnc() {
        new traerproductos().execute(idalmacen);
    }

    private class traerproductos extends AsyncTask<String, String, String> {
        HttpURLConnection conne;
        URL url = null;
        ArrayList<Productos> listaalmaceno = new ArrayList<Productos>();
        ProgressDialog pdLoading = new ProgressDialog(Listaparaseleccionar.this);

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
                url = new URL("https://sodapop.pe/sugest/apitraerproductosmaestra.php");
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

                        .appendQueryParameter("idalmacen", params[0]);

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
            peopleproducto.clear();
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
                    adapterproducto = new Adaptadorproductos(peopleproducto, getApplicationContext());
                    recyclerproducto.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                    recyclerproducto.setAdapter(adapterproducto);
                    adapterproducto.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.d("erroro",e.toString());
                }
            }

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
    public void onPause(){
        super.onPause();
        cargarbarradeabajo();
        //    Toast.makeText(getApplication(),"onpause full",Toast.LENGTH_LONG).show();
        //int yi=idsupremodedetalle;
        //eliminaraTotalcrema(yi);
        //eliminarTotaladicional(yi);
        //eliminarTOTALdetallepedido(yi);

    }

    public final static void crearpedidoinicial(int idusuario,Double totalpedido,String fechapedido,int idalmacen){


        Realm pedido = Realm.getDefaultInstance();
        pedido.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm pedido) {
                int index = Crudpedido.calculateIndex();
                PedidoRealm realmDetallepedidorealm = pedido.createObject(PedidoRealm.class, index);
                realmDetallepedidorealm.setIdusuario(idusuario);
                realmDetallepedidorealm.setTotalpedido(0.0);
                realmDetallepedidorealm.setFechapedido(fechapedido);
                realmDetallepedidorealm.setIdalmacen(idalmacen);
            }
        });
    }
    private class traerproductospornombre extends AsyncTask<String, String, String> {

        HttpURLConnection conne;
        URL url = null;
        ArrayList<Productos> listaalmaceno = new ArrayList<Productos>();
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recyclerlistado);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {

            try {
                url = new URL("https://sodapop.pe/sugest/apitraerproductospornombre.php");
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
                        ;

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
                    return("Connection error");
                }
            } catch (IOException e) {
                e.printStackTrace()                ;

                return e.toString();
            } finally {
                conne.disconnect();
            }
        }


        @Override
        protected void onPostExecute(String result) {
            ArrayList<Productos> people=new ArrayList<>();
            String[] strArrData = {"No Suggestions"};

            people.clear();
             RecyclerView.Adapter adapter;


            ArrayList<String> dataList = new ArrayList<String>();
            Productos meso;
            if(result.equals("no rows")) {
                Toast.makeText(Listaparaseleccionar.this,"no existen datos a mostrar",Toast.LENGTH_LONG).show();

            }else{
                try {
                    JSONArray jArray = new JSONArray(result);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.optJSONObject(i);
                        meso = new Productos(json_data.getInt("idproducto"), json_data.getString("nombreproducto"), json_data.getString("estadoproducto"), json_data.getString("ingredientes"),json_data.getDouble(("precventa")),json_data.getString("descripcion"));
                        people.add(meso);
                    }
                    strArrData = dataList.toArray(new String[dataList.size()]);
                    recycler.removeAllViews();
                    recycler.setAdapter(null);
                    adapter = new Adaptadorproductos(people,Listaparaseleccionar.this.getApplicationContext());
                    recycler.setLayoutManager(new GridLayoutManager(Listaparaseleccionar.this.getApplicationContext(), 1));
                    recycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {

                }
            }
        }
    }
    private class traerproductosporidalmacenidfamilia extends AsyncTask<String, String, String> {
        ArrayList<Productos> people=new ArrayList<>();
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
                url = new URL("https://sodapop.pe/sugest/apitraerproductosmaestra.php");
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

                        .appendQueryParameter("idalmacen", params[0])
                        ;

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
                    return("Connection error");
                }
            } catch (IOException e) {
                e.printStackTrace()                ;

                return e.toString();
            } finally {
                conne.disconnect();
            }
        }


        @Override
        protected void onPostExecute(String result) {
            RecyclerView.Adapter adapter;
            people.clear();
            RecyclerView recycler = (RecyclerView) findViewById(R.id.recyclerlistado);


            ArrayList<String> dataList = new ArrayList<String>();
            Productos meso;
            if(result.equals("no rows")) {
                Toast.makeText(Listaparaseleccionar.this.getApplicationContext(),"no existen datos a mostrar",Toast.LENGTH_LONG).show();

            }else{

                try {


                    JSONArray jArray = new JSONArray(result);


                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.optJSONObject(i);

                        meso = new Productos(json_data.getInt("idproducto"), json_data.getString("nombreproducto"), json_data.getString("estadoproducto"), json_data.getString("ingredientes"),json_data.getDouble(("precventa")),json_data.getString("descripcion"));
                        people.add(meso);

                    }
                    strArrData = dataList.toArray(new String[dataList.size()]);


                    adapter = new Adaptadorproductos(people,Listaparaseleccionar.this.getApplicationContext());
                    recycler.setLayoutManager(new GridLayoutManager(Listaparaseleccionar.this.getApplicationContext(), 3));

                    recycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {

                }

            }

        }

    }
    private class llenarautocomplete extends AsyncTask<String, String, String> {
        ArrayList<Productos> people=new ArrayList<>();
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
                url = new URL("https://sodapop.pe/sugest/apitraertodoslosproductosporalmacen.php");
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

                        .appendQueryParameter("idalmacen", params[0])
                        ;

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
                    return("Connection error");
                }
            } catch (IOException e) {
                e.printStackTrace()                ;

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
            if(result.equals("no rows")) {
                Toast.makeText(Listaparaseleccionar.this.getApplicationContext(),"no existen datos a mostrar",Toast.LENGTH_LONG).show();

            }else{

                try {


                    JSONArray jArray = new JSONArray(result);


                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.optJSONObject(i);

                        meso = new Productos(json_data.getInt("idproducto"), json_data.getString("nombreproducto"), json_data.getString("estadoproducto"), json_data.getString("ingredientes"),json_data.getDouble(("precventa")),json_data.getString("descripcion"));
                        people.add(meso);

                        mylist.add(json_data.getString("nombreproducto"));

                    }
                    strArrData = dataList.toArray(new String[dataList.size()]);


                    MultiAutoCompleteTextView myMultiAutoCompleteTextView
                            = (MultiAutoCompleteTextView)findViewById(
                            R.id.multiAutoCompleteTextView);
                    myMultiAutoCompleteTextView.setAdapter(
                            new ArrayAdapter<String>(Listaparaseleccionar.this.getApplicationContext(),android.R.layout.simple_dropdown_item_1line,mylist));
                    myMultiAutoCompleteTextView.setTokenizer(
                            new MultiAutoCompleteTextView.CommaTokenizer());



                } catch (JSONException e) {

                }

            }

        }

    }




    public class traertodoslospedidosporcliente extends AsyncTask<String, String, String> {

        HttpURLConnection conne;
        URL url = null;
        ProgressDialog pdLoading = new ProgressDialog(Listaparaseleccionar.this);

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
                url = new URL("https://sodapop.pe/sugest/traerpedidosporcliente.php");
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
                        .appendQueryParameter("nombreusuario", params[0]);
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
                Log.d("wiwo",e1.toString());
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
                    Log.d("waaaaaaa",result.toString());
                    return (result.toString());

                } else {

                    return("Connection error");
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
            todoslospedidos.clear();
            pdLoading.dismiss();
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    PedidoRealmFirebase pedidofirebase = new PedidoRealmFirebase
                            (json_data.getInt("idpedido"),json_data.getInt("idcliente"),json_data.getInt("idmesa")
                            ,json_data.getDouble("totalpedido"),json_data.getString("estadopedido"),json_data.getString("fechapedido")
                            ,json_data.getInt("idusuario"),json_data.getInt("idalmacen"),json_data.getString("idfacebook"),
                                    json_data.getString("observaciones"),json_data.getString("llevar"),json_data.getString("direccionllevar")
                            ,json_data.getString("idfirebase"),json_data.getString("monbredescuento"),json_data.getString("montodescuento")
                            ,json_data.getString("nombrecosto"),json_data.getString("montocosto"),json_data.getString("longitud"),
                                    json_data.getString("latitud"),json_data.getString("pagocliente"),json_data.getString("vuelto"),
                                    json_data.getString("telefono"),json_data.getString("refrencias"),json_data.getString("nombreusuariof"));
                    todoslospedidos.add(pedidofirebase);


                }


                final Dialog dialog = new Dialog(Listaparaseleccionar.this);
                dialog.setContentView(R.layout.dialogopedidos);
                DisplayMetrics metrics = getResources().getDisplayMetrics();
                int width = metrics.widthPixels;
                int height = metrics.heightPixels;

                dialog.getWindow().setLayout((7 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);
                TextView clie=(TextView) dialog.findViewById(R.id.nombredialog);
                clie.setText(todoslospedidos.get(0).getNombreusuario());

                final RecyclerView pedidose= dialog.findViewById(R.id.listadepedidos);

                Adaptadorrecibepedidos adaptadore = new Adaptadorrecibepedidos( todoslospedidos,dialog.getContext());
                pedidose.setLayoutManager(new GridLayoutManager(Listaparaseleccionar.this.getApplicationContext(),1 ));

                pedidose.setAdapter(adaptadore);
                adaptadore.notifyDataSetChanged();
                dialog.show();






            } catch (JSONException e) {


                Log.d("erororor",e.toString());
            }


        }

    }
    @Override
    public void onBackPressed() {

    }
}
