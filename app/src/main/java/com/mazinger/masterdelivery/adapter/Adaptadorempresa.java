package com.mazinger.masterdelivery.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mazinger.masterdelivery.CircleTransform;
import com.mazinger.masterdelivery.Muestraproductosporempresa;
import com.mazinger.masterdelivery.R;
import com.mazinger.masterdelivery.Realm.Crudiniciodeldia;
import com.mazinger.masterdelivery.Realm.Crudpedido;
import com.mazinger.masterdelivery.Realm.IniciodeldiaRealm;
import com.mazinger.masterdelivery.Realm.PedidoRealm;
import com.mazinger.masterdelivery.modelo.Empresa;
import com.mazinger.masterdelivery.modelo.Valoracionporcentajeempresa;
import com.squareup.picasso.Picasso;

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
import jp.wasabeef.picasso.transformations.CropSquareTransformation;

import static com.mazinger.masterdelivery.Login.CONNECTION_TIMEOUT;
import static com.mazinger.masterdelivery.Login.READ_TIMEOUT;

public class Adaptadorempresa extends RecyclerView.Adapter<Adaptadorempresa.AdaptadorViewHolder> {
    private Context mainContext;
    public String valorporcentaje, valorcantidad;
    ArrayList<Valoracionporcentajeempresa> people = new ArrayList<>();
    String foto;
    SharedPreferences prefs;
    String FileName = "myfile", productocabecera;

    private List<Empresa> items;
    ArrayList<Empresa> Empresa = new ArrayList<>();
    Empresa objEmpresa;
    ArrayList<Empresa> peopleadicional = new ArrayList<>();
    String[] stradicional = {"No Suggestions"};

    public Adaptadorempresa(List<Empresa> items, Context contexto) {
        this.mainContext = contexto;
        this.items = items;
    }

    static class AdaptadorViewHolder extends RecyclerView.ViewHolder {
        protected TextView nombreempresa, idempresal;
        protected TextView nombrerubro;
        //protected TextView precio;
        protected TextView montominimodeventa, costodelivery;
        protected ImageView imagenempresa1;
        protected TextView tiempodeesperal, txtvaloracion;
        protected ImageView imagenefectivol,
                imagenvisal, imastercardl,logito;
        protected CardView tarjetaempresa;


        public AdaptadorViewHolder(View v) {
            super(v);

            this.idempresal = (TextView) v.findViewById(R.id.idempresal);
            this.nombreempresa = (TextView) v.findViewById(R.id.nombreempresa);
            this.nombrerubro = (TextView) v.findViewById(R.id.rubroempresa);
            this.montominimodeventa = (TextView) v.findViewById(R.id.montominimodeventa);
            this.imagenempresa1 = (ImageView) v.findViewById(R.id.imagenempresa1);
            this.logito = (ImageView) v.findViewById(R.id.logito);
            this.tiempodeesperal = (TextView) v.findViewById(R.id.tiempodeesperal);
            this.costodelivery = (TextView) v.findViewById(R.id.costodeliverycard);
            this.tarjetaempresa = (CardView) v.findViewById(R.id.tarjetaempresa);
            this.txtvaloracion = (TextView) v.findViewById(R.id.txtvaloracion);


        }
    }

    @Override
    public Adaptadorempresa.AdaptadorViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tarjetitaempresas, viewGroup, false);
        prefs = mainContext.getSharedPreferences(FileName, Context.MODE_PRIVATE);

        return new Adaptadorempresa.AdaptadorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final Adaptadorempresa.AdaptadorViewHolder viewHolder, final int position) {
        final Empresa item = items.get(position);


        viewHolder.itemView.setTag(item);


        new traerporcentajes().execute(String.valueOf(item.getIdempresa()));

        viewHolder.txtvaloracion.setText(valorporcentaje);

        viewHolder.idempresal.setText(String.valueOf(item.getIdempresa()));

        foto = item.getLogotipoempresa().toString();

        viewHolder.nombreempresa.setText(item.getRazonsocialempresa());
        viewHolder.nombrerubro.setText(String.valueOf(item.getNombrerubro()));
        viewHolder.montominimodeventa.setText("S/. " + String.valueOf(item.getMontominimodeventa()));
        viewHolder.costodelivery.setText("S/. " + String.valueOf(item.getCostodelivery()));

        foto = item.getLogotipoempresa().toString();
        Picasso.get().load(foto).transform(new CropSquareTransformation()).resize(400, 400)
                .into(viewHolder.imagenempresa1);
        viewHolder.tiempodeesperal.setText(item.getTiempodedemoraempresa()+"min  " );
        //viewHolder.im.setText(String.valueOf(porcenta+item.getPrecventa()));
        //viewHolder.preciodescuento.setPaintFlags(viewHolder.preciodescuento.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        prefs = mainContext.getSharedPreferences(FileName, Context.MODE_PRIVATE);




        String valorporcentaje1=prefs.getString("valorporcentaje","");
        if (item.getIdtipodeatencion().equals("abierto")){

            Picasso.get().load(R.drawable.abierto).transform(new CircleTransform()).resize(100, 100).into(viewHolder.logito);

        }else{

            Picasso.get().load(R.drawable.cerrado).transform(new CircleTransform()).resize(100, 100).into(viewHolder.logito);

        }


      viewHolder.txtvaloracion.setText(valorporcentaje1);




        viewHolder.tarjetaempresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idi=String.valueOf(item.getIdempresa());
                //String idi = prefs.getString("idempresa", "");


crearpedidoinicial(1,0.0,"ww",item.getIdempresa());


                Intent intent = new Intent(v.getContext(), Muestraproductosporempresa.class);
                intent.putExtra("idempresa", String.valueOf(item.getIdempresa()));
                intent.putExtra("nombreempresa", String.valueOf(item.getRazonsocialempresa()));
                intent.putExtra("telefonoempresa", String.valueOf(item.getTelefonoempresa()));
                intent.putExtra("tiempodemora", String.valueOf(item.getTiempodedemoraempresa()));
                intent.putExtra("montominimo", String.valueOf(item.getMontominimodeventa()));
                intent.putExtra("imagen", String.valueOf(item.getLogotipoempresa()));
                //intent.putExtra("idproductoseleccionado",String.valueOf(item.getIdproducto()));
                v.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    private class traerporcentajes extends AsyncTask<String, String, String> {

        HttpURLConnection conne;
        URL url = null;
        ArrayList<Valoracionporcentajeempresa> listaalmaceno = new ArrayList<Valoracionporcentajeempresa>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {

            try {
                url = new URL("https://sodapop.pe/sugest/apiandroidporcentaje.php");
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

            String[] strArrData = {"No Suggestions"};

            people.clear();
            RecyclerView.Adapter adapter;


            ArrayList<String> dataList = new ArrayList<String>();
            Valoracionporcentajeempresa meso;
            if (result.equals("no rows")) {


            } else {
                try {
                    JSONArray jArray = new JSONArray(result);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.optJSONObject(i);
                        meso = new Valoracionporcentajeempresa(json_data.getString("porcentaje"),
                                json_data.getString("cantidad"));
                        people.add(meso);
                    }
                    valorporcentaje = people.get(0).getPorcentaje().toString();
                    valorcantidad = people.get(0).getCantidad().toString();
guardare(valorporcentaje,valorcantidad);



                } catch (JSONException e) {

                }
            }
        }
    }
    public   void guardare(String valorporcentaje,String valorcantidad){
        SharedPreferences sharedPreferences =mainContext.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("valorporcentaje",valorporcentaje);
        editor.putString("valorcantidad",valorcantidad);
        editor.commit();

    }




    // try {
    //  String idi = myIntent.getStringExtra("idempresa"); // will return "FirstKeyValue"

    //String idalmacen = prefs.getString("idalmacenactivosf", "");

    //} catch (NumberFormatException e) {
    //  Log.i("tallo", e.toString());
    // }








    public final static void crearpedidoinicial(int idusuario, Double totalpedido, String fechapedido, int idalmacen) {
        Realm pedido = Realm.getDefaultInstance();
        pedido.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm pedido) {
                int gy= Crudpedido.calculateIndex();
                PedidoRealm creapedido = pedido.createObject(PedidoRealm.class, gy);
                creapedido.setIdusuario(idusuario);
                creapedido.setTotalpedido(totalpedido);
                creapedido.setFechapedido(fechapedido);
                creapedido.setIdalmacen(idalmacen);
                creapedido.setIdcliente(1);
                creapedido.setIdmesa(1);
                creapedido.setEstadopedido("1");
                creapedido.setDescripcionpedido("ssss");
                creapedido.setIdfacebook("www");
                creapedido.setTotal(0.0);
            }
        });
    }

    public final static void crearpedidoinicialdeldiarealm(int idusuario, Double totalpedido, String fechapedido, int idalmacen) {
        Realm pedido = Realm.getDefaultInstance();
        pedido.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm pedido) {
                int gy= Crudiniciodeldia.calculateIndex();
                IniciodeldiaRealm creapedido = pedido.createObject(IniciodeldiaRealm.class, gy);
                creapedido.setIdusuario(idusuario);

                creapedido.setIdalmacen(idalmacen);
                creapedido.setTotal(0.0);
            }
        });
    }

    public   void guardarempresa(String idempresa,String total){
        ;
        prefs = mainContext.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences =mainContext.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("idempresa",idempresa);
        editor.putString("tota",total);
        editor.commit();
    }
}

