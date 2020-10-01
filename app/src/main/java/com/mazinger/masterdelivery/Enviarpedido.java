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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mazinger.masterdelivery.Realm.AdicionalRealm;
import com.mazinger.masterdelivery.Realm.AdicionalRealmFirebase;
import com.mazinger.masterdelivery.Realm.CremaRealm;
import com.mazinger.masterdelivery.Realm.CremaRealmFirebase;
import com.mazinger.masterdelivery.Realm.CrudadicionalRealm;
import com.mazinger.masterdelivery.Realm.Crudetallepedido;
import com.mazinger.masterdelivery.Realm.Crudpedido;
import com.mazinger.masterdelivery.Realm.DetallepedidoRealmFirebase;
import com.mazinger.masterdelivery.Realm.Detallepedidorealm;
import com.mazinger.masterdelivery.Realm.PedidoRealm;
import com.mazinger.masterdelivery.Realm.PedidoRealmFirebase;
import com.mazinger.masterdelivery.adapter.Adaptadorparaverenviarpedidos;

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
import io.realm.RealmResults;

public class Enviarpedido extends AppCompatActivity {
    String FileName = "myfile";
    SharedPreferences prefs;
    String almacen, pagocliente, vuelto;
    private static final String FPEDIDOS = "PEDIDOS";
    private static final String FDETALLEPEDIDO = "FDETALLEPEDIDO";
    private static final String FCREMAS = "FCREMAS";
    private static final String FADICIONAL = "FADICIONAL";
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
Button limpiar,wasap;
RecyclerView lista;
TextView nombre,direccion,referencia,total,cunatopaga,vueltoc,idrecojera;
    String idempresa,horaaentregar,direccionseleccionada,longitudd,latitudd;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviarpedido);
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();
        Realm.init(getApplicationContext());

        prefs = getApplication().getSharedPreferences(FileName, Context.MODE_PRIVATE);
       almacen = prefs.getString("idalmacenactivosf", "");
        limpiar=(Button) findViewById(R.id.limpiartodo) ;
        wasap=(Button)findViewById(R.id.wasap);
       lista=(RecyclerView) findViewById(R.id.detalle);
nombre=(TextView) findViewById(R.id.usuario);
        direccion=(TextView) findViewById(R.id.direccion);
        referencia=(TextView) findViewById(R.id.referencia);
        total=(TextView) findViewById(R.id.totalapagar);
        cunatopaga=(TextView) findViewById(R.id.cuantopagacliente);
        vueltoc=(TextView) findViewById(R.id.vuelto);
        idrecojera=(TextView) findViewById(R.id.recojera);
        Intent intent = this.getIntent();
        Bundle extra = intent.getExtras();




        idrecojera.setText(horaaentregar);

limpiar.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        borrartodo();


        Intent i = new Intent(getApplicationContext(), Muestartodaslasempresas.class);
        startActivity(i);

    }
});



        prefs = getApplication().getSharedPreferences(FileName, Context.MODE_PRIVATE);
        String nombreusuarioff = prefs.getString("nombreusuariof", "");
        String estadipedido = prefs.getString("estadopedido", "");
        horaaentregar= extra.getString("horaaentregar");
        direccionseleccionada= extra.getString("direccionseleccionada");
        longitudd= extra.getString("longitudd");
        latitudd= extra.getString("latitudd");

        escribiren(1);
        mostrartodo();
        guardarestadodeunpedido("generadodelivery");


        nombre.setText(nombreusuarioff);

                    wasap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        enviarawasap();
                             }
                            });



    }

    private void enviarawasap() {




                    try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+"51910260813"+"&text="+"Hola, puedes comunicarte con migo por este medio"));
                startActivity(intent);
            }catch (Exception e){
                e.printStackTrace();
            }

    }


    public final static List<CremaRealm> todocrema(int ido) {
        Realm pedido = Realm.getDefaultInstance();

        RealmResults<CremaRealm> results =
                pedido.where(CremaRealm.class).
                        equalTo("id", ido)
                        .findAll();
        results.toString().trim();

        pedido.commitTransaction();
        return results;
    }


    public final static List<AdicionalRealm> todoadicional(int ido) {
        Realm pedido = Realm.getDefaultInstance();

        RealmResults<AdicionalRealm> results =
                pedido.where(AdicionalRealm.class).
                        equalTo("id", ido)
                        .findAll();
        results.toString().trim();

        pedido.commitTransaction();
        return results;
    }




    private void escribiren(int id) {
        prefs = getApplication().getSharedPreferences(FileName, Context.MODE_PRIVATE);
        String telefonoguardado = prefs.getString("telefono", "");
        String idfirebase = prefs.getString("idfirebase", "mi fire");
        String direccione = direccionseleccionada;
        String referencias = prefs.getString("referencia", "");
        String latitudp = latitudd;
        String longitudp = longitudd;
        String nombredescuento = prefs.getString("nombredescuento", "");
        String montodescuento = prefs.getString("montodescuento", "");
        String nombrecosto = prefs.getString("nombrecosto", "");
        String montocosto = prefs.getString("montocosto", "");
        String totalpedido = prefs.getString("totalpedido", "");
        String nombreusuariof = prefs.getString("nombreusuariof", "");
        idempresa = prefs.getString("idempresa", "");
        pagocliente = prefs.getString("cuantopagacliente", "");
        vuelto = prefs.getString("vuelto", "");
        nombre.setText(nombreusuariof);
        Realm pedido = Realm.getDefaultInstance();

        ArrayList<PedidoRealmFirebase> todoslospedidos = new ArrayList<>();
        RealmResults<PedidoRealm> resultsp =
                pedido.where(PedidoRealm.class).
                        equalTo("idpedido", id)
                        .findAll();

        int wp = resultsp.size();
        for (int i = 0; i < wp; i++) {
            int idpedidop = resultsp.get(i).getIdpedido();
            int idalmacenp = resultsp.get(i).getIdalmacen();
            int idclientep = resultsp.get(i).getIdcliente();


            String descripcionpedidop =horaaentregar;

            String estadopedidop = "generadodelivery";
            Double totalp = resultsp.get(i).getTotal();
            int idmesap = resultsp.get(i).getIdmesa();
            String fechapedidop = resultsp.get(i).getFechapedido();
            String idfacebookp = resultsp.get(i).getIdfacebook();
            String descripcionp = resultsp.get(i).getDescripcionpedido();
            int idusuariop = resultsp.get(i).getIdusuario();
            PedidoRealmFirebase ped = new PedidoRealmFirebase();
            todoslospedidos.add(ped);
            PedidoRealmFirebase prf = new PedidoRealmFirebase(idpedidop,
                    idclientep,
                    idmesap,
                    Double.parseDouble(totalpedido),
                    "generadodelivery",
                    fechapedidop,
                    idusuariop,
                    idalmacenp,
                    idfacebookp
                    , descripcionpedidop,
                    "1",
                    direccione,
                    idfirebase,
                    nombredescuento,
                    montodescuento,
                    nombrecosto,
                    montocosto,
                    longitudp,
                    latitudp,
                    pagocliente,
                    vuelto,
                    telefonoguardado,
                    referencias,
                    nombreusuariof,idempresa);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference(FPEDIDOS);
            reference.child(idfirebase).setValue(prf);

            new grabarpedido().execute(prf);

            direccion.setText(direccionseleccionada);
            referencia.setText(prf.getReferencias().toString());
            total.setText(prf.getTotalpedido().toString());
            cunatopaga.setText(prf.getCuantopagaecliente().toString());
            vueltoc.setText(prf.getVuelto().toString());
        }


        String f;

        ArrayList<DetallepedidoRealmFirebase> todoslosdetalles = new ArrayList<>();
        //sacar el todoslosdetalles
        RealmResults<Detallepedidorealm> results =
                pedido.where(Detallepedidorealm.class)
                        .findAll();
        int w = results.size();
        DetallepedidoRealmFirebase det ;
        for (int i = 0; i < w; i++) {
            int cantidad = results.get(i).getCantidadrealm();
            int idd = results.get(i).getId();
            int idpedido = results.get(i).getIdpedido();
            String nombreproducto = results.get(i).getNombreproductorealm();
            String subtotal = results.get(i).getSubtotal();
            Double precvente = results.get(i).getPrecventarealm();
            int idproductorealm = results.get(i).getIdproductorealm();
            String comentariococina = results.get(i).getComentarioacocina();
 det = new DetallepedidoRealmFirebase(idd, 1, subtotal, idfirebase, cantidad, precvente, nombreproducto, "nohay", Integer.parseInt(almacen), idproductorealm, comentariococina);


            todoslosdetalles.add(det);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference(FPEDIDOS);
            String pppp = String.valueOf(det.getIdproductorealm() + i);
            new grabardetalle().execute(det);
////aquigrabacreasporcadadetalle
            RealmResults<CremaRealm> resulcremaa =
                    pedido.where(CremaRealm.class)
                            .equalTo("id", det.getId())
                            .findAll();

            int lsrgaa = resulcremaa.size();

            for (int ic = 0; ic < lsrgaa; ic++) {
                RealmResults<CremaRealm> resultsoa =
                        pedido.where(CremaRealm.class)
                                .equalTo("id", det.getId())
                                .findAll();
                CremaRealmFirebase dett = new CremaRealmFirebase(ic, resultsoa.get(ic).getNombrecrema().toString(), "1", 1, idproductorealm);
                new grabarcrema().execute(dett);

            }
/////////////////////
            RealmResults<AdicionalRealm> resultadicionala =
                    pedido.where(AdicionalRealm.class)
                            .equalTo("id", det.getId())
                            .findAll();
            int cuantosa = resultadicionala.size();
            for (int ia = 0; ia < cuantosa; ia++) {
                RealmResults<AdicionalRealm> unoporunoa =
                        pedido.where(AdicionalRealm.class)
                                .equalTo("id", det.getId())
                                .findAll();
                AdicionalRealmFirebase dettaa = new AdicionalRealmFirebase(
                        unoporunoa.get(ia).getIdadicional(),unoporunoa.get(ia).getNombreadicional(),unoporunoa.get(ia).getPrecioadicional(),
                        unoporunoa.get(ia).getEstadoadicional(),unoporunoa.get(ia).getIdproducto(),unoporunoa.get(ia).getId());
                new grabaradicional().execute(dettaa);
            }









            reference.child(idfirebase).child("PRODUCTOS").child(pppp).setValue(det);

//lleno cremas

            RealmResults<CremaRealm> resulcrema =
                    pedido.where(CremaRealm.class)
                            .equalTo("id", idd)
                            .findAll();

            int lsrga = resulcrema.size();

            for (int ic = 0; ic < lsrga; ic++) {
                RealmResults<CremaRealm> resultso =
                        pedido.where(CremaRealm.class)
                                .equalTo("id", idd)
                                .findAll();





                    CremaRealmFirebase dett = new CremaRealmFirebase(ic, resultso.get(ic).getNombrecrema().toString(), "1", 1, resultso.get(ic).getIdproducto());
                    FirebaseDatabase databaseec = FirebaseDatabase.getInstance();
                    DatabaseReference referenceec = databaseec.getReference(FPEDIDOS);
                    String pppo = String.valueOf(dett.getIdcrema()+"c");

                reference.child(idfirebase).child("PRODUCTOS").child(pppp).child("CREMA").child(pppo).setValue(dett);
                    }

        //lleno adicionales
            RealmResults<AdicionalRealm> resultadicional =
                    pedido.where(AdicionalRealm.class)
                            .equalTo("id", idd)
                            .findAll();
            int cuantos = resultadicional.size();
            for (int ia = 0; ia < cuantos; ia++) {
                RealmResults<AdicionalRealm> unoporuno =
                        pedido.where(AdicionalRealm.class)
                                .equalTo("id", idd)
                                .findAll();
                AdicionalRealmFirebase detta = new AdicionalRealmFirebase(
                        unoporuno.get(ia).getIdadicional(),unoporuno.get(ia).getNombreadicional(),unoporuno.get(ia).getPrecioadicional(),
                        unoporuno.get(ia).getEstadoadicional(),unoporuno.get(ia).getIdproducto(),unoporuno.get(ia).getId());
                FirebaseDatabase databaseeca = FirebaseDatabase.getInstance();
                DatabaseReference referenceeca = databaseeca.getReference(FPEDIDOS);
                String pppoa = String.valueOf(detta.getIdadicional()+"ia");
                String e=detta.getNombreadicional().toString().trim();
                reference.child(idfirebase).child("PRODUCTOS").child(pppp).child("ADICIONAL").child(pppoa).setValue(detta);

            }


        }




    }

    public void guardarestadodeunpedido(String estadopedido) {
         SharedPreferences sharedPreferences = getSharedPreferences(FileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("estadopedido", estadopedido);

        editor.commit();


    }

    private void borrartodo() {
        Realm pedido = Realm.getDefaultInstance();
        pedido.beginTransaction();

        pedido.deleteAll();
        pedido.commitTransaction();
    }
    public final static List<AdicionalRealm> Eliminartotaladicionals() {
        Realm pedido = Realm.getDefaultInstance();

        int ido = CrudadicionalRealm.calculateIndex();
        RealmResults<AdicionalRealm> results =pedido.where(AdicionalRealm.class).
                equalTo("id", ido)
                .findAll();

        pedido.beginTransaction();
        results.deleteAllFromRealm();     // App crash
        pedido.commitTransaction();
        return results;

    }

    public final static List<Detallepedidorealm> Eliminartotaldetalles() {
        Realm pedido = Realm.getDefaultInstance();

        int ido = Crudetallepedido.calculateIndex();
        RealmResults<Detallepedidorealm> results =pedido.where(Detallepedidorealm.class).
                equalTo("id", ido)
                .findAll();

        pedido.beginTransaction();
        results.deleteAllFromRealm();     // App crash
        pedido.commitTransaction();
        return results;

    }
    public final static List<PedidoRealm> Eliminartotalpedidos() {
        Realm pedido = Realm.getDefaultInstance();

int ido =Crudpedido.calculateIndex();
        RealmResults<PedidoRealm> results =pedido.where(PedidoRealm.class).
                        equalTo("id", ido)
                        .findAll();

        pedido.beginTransaction();
        results.deleteAllFromRealm();     // App crash
        pedido.commitTransaction();
        return results;

    }
    private class grabarpedido extends AsyncTask<PedidoRealmFirebase, Void, String> {
        String resultado;
        ProgressDialog pdLoading = new ProgressDialog(Enviarpedido.this);
        HttpURLConnection conne;
        URL url = null;
        PedidoRealmFirebase ped;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tenviando Pedido :)");
            pdLoading.setCancelable(false);
            pdLoading.show();



        }

        @Override
        protected String doInBackground(PedidoRealmFirebase... params) {
            ped=params[0];
            try {
                url = new URL("https://sodapop.pe/sugest/grabarpedidofirebase.php");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
            try {
                conne = (HttpURLConnection) url.openConnection();
                conne.setReadTimeout(READ_TIMEOUT);
                conne.setConnectTimeout(CONNECTION_TIMEOUT);
                conne.setRequestMethod("POST");
                conne.setDoInput(true);
                conne.setDoOutput(true);

                // Append parameters to URL


                Log.d("valor",ped.toString().trim());
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("idcliente",String.valueOf(ped.getIdusuario()))
                        .appendQueryParameter("idmesa",String.valueOf(ped.getIdmesa()))
                        .appendQueryParameter("totalpedido",String.valueOf(ped.getTotalpedido()))
                        .appendQueryParameter("estadopedido", String.valueOf(ped.getEstadopedido()))
                        .appendQueryParameter("fechapedido", String.valueOf(ped.getFechapedido()))
                        .appendQueryParameter("idusuario", String.valueOf(ped.getIdusuario()))
                        .appendQueryParameter("idalmacen", String.valueOf(ped.getIdalmacen()))
                        .appendQueryParameter("idfacebook", String.valueOf(ped.getIdfacebook()))


                        .appendQueryParameter("observaciones", String.valueOf(ped.getDescripcionpedido()))
                        .appendQueryParameter("llevar", String.valueOf(ped.getLlevar()))
                        .appendQueryParameter("direccionllevar", String.valueOf(ped.getDireccionallevar()))
                        .appendQueryParameter("idfirebase", String.valueOf(ped.getIdfirebase()))
                        .appendQueryParameter("latitud", String.valueOf(ped.getLatitud()))
                    .appendQueryParameter("longitud", String.valueOf(ped.getLongitud()))
                    .appendQueryParameter("costodelivery", String.valueOf(ped.getMontocosto()))
                    .appendQueryParameter("telefono", String.valueOf(ped.getTelefono()))
           .appendQueryParameter("nombredescuento", String.valueOf(ped.getNombredescuento()))
                    .appendQueryParameter("montodescuento", String.valueOf(ped.getMontodescuento()))
                    .appendQueryParameter("nombrecosto", String.valueOf(ped.getNombrecosto()))
                    .appendQueryParameter("montocosto", String.valueOf(ped.getMontocosto()))
                    .appendQueryParameter("pagocliente", String.valueOf(ped.getCuantopagaecliente()))
                    .appendQueryParameter("vuelto", String.valueOf(ped.getVuelto()))
                    .appendQueryParameter("telefonoguardado", String.valueOf(ped.getTelefono()))
                        .appendQueryParameter("refrencias", String.valueOf(ped.getReferencias()))
                        .appendQueryParameter("nombreusuariof", String.valueOf(ped.getNombreusuario()))
                        .appendQueryParameter("idempresa", String.valueOf(ped.getIdempresa()))
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
                return null;
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
                    resultado=result.toString();
                    Log.d("paso",resultado.toString());
                    return resultado;

                } else {

                }
            } catch (IOException e) {
                e.printStackTrace()                ;
                Log.d("valorito",e.toString());
                return null;
            } finally {
                conne.disconnect();
            }
            return resultado;
        }
        @Override
        protected void onPostExecute(String resultado) {
            pdLoading.dismiss();


            super.onPostExecute(resultado);

            if(resultado.equals("true")){
                Log.d("ii", resultado);


            }else{
                String ii =resultado.toString();
                Log.d("jj", "usuario valido");


                // lanzarsistema();
            }



        }
    }
    public class grabardetalle extends AsyncTask<DetallepedidoRealmFirebase, Void, String> {
        String resultado;
        HttpURLConnection conne;
        URL url = null;
        DetallepedidoRealmFirebase ped;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(DetallepedidoRealmFirebase... params) {
            ped=params[0];
            try {
                url = new URL("https://sodapop.pe/sugest/grabardetallepedidofirebase.php");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
            try {
                conne = (HttpURLConnection) url.openConnection();
                conne.setReadTimeout(READ_TIMEOUT);
                conne.setConnectTimeout(CONNECTION_TIMEOUT);
                conne.setRequestMethod("POST");
                conne.setDoInput(true);
                conne.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()

                        .appendQueryParameter("idproducto",String.valueOf(ped.getIdproductorealm()))
                        .appendQueryParameter("cantidad",String.valueOf(ped.getCantidadrealm()))
                        .appendQueryParameter("precventa",String.valueOf(ped.getPrecventarealm()))
                        .appendQueryParameter("subtotal", String.valueOf(ped.getSubtotal()))
                        .appendQueryParameter("idcalmacen",String.valueOf(ped.getIdalmacenrealm()))
                        .appendQueryParameter("adicionales",String.valueOf(ped.getImagenrealm()))
                        .appendQueryParameter("cremas",String.valueOf(ped.getImagenrealm()))
                        .appendQueryParameter("comentario", String.valueOf(ped.getComentarioacocina()))
                        .appendQueryParameter("ojo", String.valueOf(ped.getOjo()))
                        .appendQueryParameter("imagenreal", String.valueOf(ped.getImagenrealm()))
                        .appendQueryParameter("comentarioacocina", String.valueOf(ped.getComentarioacocina()))
                        .appendQueryParameter("nombreproductorealm", String.valueOf(ped.getNombreproductorealm()))
                        .appendQueryParameter("almacenreal", String.valueOf(ped.getIdalmacenrealm()))
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
                return null;
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
                    resultado=result.toString();
                    Log.d("paso",resultado.toString());
                    return resultado;

                } else {

                }
            } catch (IOException e) {
                e.printStackTrace()                ;
                Log.d("valorito",e.toString());
                return null;
            } finally {
                conne.disconnect();
            }
            return resultado;
        }
        @Override
        protected void onPostExecute(String resultado) {

            super.onPostExecute(resultado);

            if(resultado.equals("true")){
                Log.d("ii", resultado);


            }else{
                String ii =resultado.toString();
                Log.d("jj", "usuario valido");


                // lanzarsistema();
            }



        }
    }
    public class grabaradicional extends AsyncTask<AdicionalRealmFirebase, Void, String> {
        String resultado;
        HttpURLConnection conne;
        URL url = null;
        AdicionalRealmFirebase ped;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(AdicionalRealmFirebase... params) {
            ped=params[0];
            try {
                url = new URL("https://sodapop.pe/sugest/grabaradicionalfirebase.php");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
            try {
                conne = (HttpURLConnection) url.openConnection();
                conne.setReadTimeout(READ_TIMEOUT);
                conne.setConnectTimeout(CONNECTION_TIMEOUT);
                conne.setRequestMethod("POST");
                conne.setDoInput(true);
                conne.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()

                        .appendQueryParameter("idadicional",String.valueOf(ped.getIdadicional()))
                        .appendQueryParameter("nombreadicional",String.valueOf(ped.getNombreadicional()))
                        .appendQueryParameter("precioadicional",String.valueOf(ped.getPrecioadicional()))
                        .appendQueryParameter("estadoadicional", String.valueOf(ped.getEstadoadicional()))
                        .appendQueryParameter("idproducto",String.valueOf(ped.getIdproducto()))

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
                return null;
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
                    resultado=result.toString();
                    Log.d("paso",resultado.toString());
                    return resultado;

                } else {

                }
            } catch (IOException e) {
                e.printStackTrace()                ;
                Log.d("valorito",e.toString());
                return null;
            } finally {
                conne.disconnect();
            }
            return resultado;
        }
        @Override
        protected void onPostExecute(String resultado) {

            super.onPostExecute(resultado);





        }
    }
    public class grabarcrema extends AsyncTask<CremaRealmFirebase, Void, String> {
        String resultado;
        HttpURLConnection conne;
        URL url = null;
        CremaRealmFirebase ped;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(CremaRealmFirebase... params) {
            ped=params[0];
            try {
                url = new URL("https://sodapop.pe/sugest/grabarcremafirebase.php");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
            try {
                conne = (HttpURLConnection) url.openConnection();
                conne.setReadTimeout(READ_TIMEOUT);
                conne.setConnectTimeout(CONNECTION_TIMEOUT);
                conne.setRequestMethod("POST");
                conne.setDoInput(true);
                conne.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()

                        .appendQueryParameter("idcrema",String.valueOf(ped.getIdcrema()))
                        .appendQueryParameter("nombrecrema",String.valueOf(ped.getNombrecrema()))
                        .appendQueryParameter("estadocrema",String.valueOf(ped.getEstadocrema()))
                        .appendQueryParameter("iddetalle", String.valueOf(ped.getId()))
                        .appendQueryParameter("idproducto",String.valueOf(ped.getIdproducto()))

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
                return null;
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
                    resultado=result.toString();
                    Log.d("paso",resultado.toString());
                    return resultado;

                } else {

                }
            } catch (IOException e) {
                e.printStackTrace()                ;
                Log.d("valorito",e.toString());
                return null;
            } finally {
                conne.disconnect();
            }
            return resultado;
        }
        @Override
        protected void onPostExecute(String resultado) {

            super.onPostExecute(resultado);

            if(resultado.equals("true")){
                Log.d("ii", resultado);


            }else{
                String ii =resultado.toString();
                Log.d("jj", "usuario valido");


                // lanzarsistema();
            }



        }
    }
    public void mostrartodo(){

        Realm pedido = Realm.getDefaultInstance();
        ArrayList<DetallepedidoRealmFirebase> todoslosdetalles = new ArrayList<>();


        RealmResults<Detallepedidorealm> results =
                pedido.where(Detallepedidorealm.class)
                        .findAll();
        int w = results.size();
        DetallepedidoRealmFirebase det ;

        for (int i = 0; i < w; i++) {
            int cantidad = results.get(i).getCantidadrealm();
            int idd = results.get(i).getId();
            int idpedido = results.get(i).getIdpedido();
            String nombreproducto = results.get(i).getNombreproductorealm();
            String subtotal = results.get(i).getSubtotal();
            Double precvente = results.get(i).getPrecventarealm();
            int idproductorealm = results.get(i).getIdproductorealm();
            String comentariococina = results.get(i).getComentarioacocina();
            ArrayList<CremaRealmFirebase> todaslascremas = new ArrayList<>();



            RealmResults<CremaRealm> resulcremaa =
                    pedido.where(CremaRealm.class)
                            .equalTo("id", results.get(i).getId())
                            .findAll();

            int lsrgaa = resulcremaa.size();

            for (int ic = 0; ic < lsrgaa; ic++) {
                RealmResults<CremaRealm> resultsoa =
                        pedido.where(CremaRealm.class)
                                .equalTo("id", results.get(i).getId())
                                .findAll();
                CremaRealmFirebase dett = new CremaRealmFirebase(ic, resultsoa.get(ic).getNombrecrema().toString(), "1", 1, idproductorealm);
              todaslascremas.add(dett);
            }

int g=todaslascremas.size();
            String fodat="";
            String foda="";
            for (int u=0;u<g;u++){
                 foda=todaslascremas.get(u).getNombrecrema().toString();
                 fodat=fodat+foda+"\r\n";



            }
////////////////////////////////////////////adicionales
            ArrayList<AdicionalRealmFirebase> todoslosadicionalesa = new ArrayList<>();
            RealmResults<AdicionalRealm> resulcremaad =
                    pedido.where(AdicionalRealm.class)
                            .equalTo("id", results.get(i).getId())
                            .findAll();
            int lsrgaad = resulcremaad.size();
            for (int ica = 0; ica < lsrgaad; ica++) {
                RealmResults<AdicionalRealm> resultsoad =
                        pedido.where(AdicionalRealm.class)
                                .equalTo("id", results.get(i).getId())
                                .findAll();
                AdicionalRealmFirebase dettd = new AdicionalRealmFirebase(ica,resultsoad.get(ica).getNombreadicional(),Double.parseDouble(resultsoad.get(ica).getPrecioadicional().toString()),resultsoad.get(ica).getEstadoadicional(),resultsoad.get(ica).getIdproducto(),resultsoad.get(ica).getId());
                todoslosadicionalesa.add(dettd);
            }

            int ga=todoslosadicionalesa.size();
            String fodata="";
            String fodaa="";
            for (int ua=0;ua<ga;ua++){
                fodaa=todoslosadicionalesa.get(ua).getNombreadicional().toString();
                fodata=fodata+fodaa+"\r\n";
}

            ///////////////////////////////////////////////////////
            det = new DetallepedidoRealmFirebase(1,1  , subtotal, fodat, cantidad, precvente, nombreproducto, fodata, Integer.parseInt(almacen), idproductorealm, comentariococina);
            todoslosdetalles.add(det);
            RecyclerView.Adapter adapterventas = new Adaptadorparaverenviarpedidos(todoslosdetalles,this);
            lista.setLayoutManager(new LinearLayoutManager(this));
            lista.setAdapter(adapterventas);

        }




    }

    @Override
    public void onBackPressed() {


    }
}


