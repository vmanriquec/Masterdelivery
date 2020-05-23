package com.mazinger.masterdelivery.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mazinger.masterdelivery.Listaparaseleccionar;
import com.mazinger.masterdelivery.R;
import com.mazinger.masterdelivery.Realm.DetallepedidoRealmFirebase;
import com.mazinger.masterdelivery.Realm.Detallepedidorealm;
import com.mazinger.masterdelivery.Realm.PedidoRealmFirebase;
import com.mazinger.masterdelivery.modelo.Adicional;
import com.mazinger.masterdelivery.modelo.Detallepedido;

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

import static com.mazinger.masterdelivery.Login.CONNECTION_TIMEOUT;
import static com.mazinger.masterdelivery.Login.READ_TIMEOUT;

public class Adaptadorrecibepedidos extends RecyclerView.Adapter<Adaptadorrecibepedidos.AdaptadorViewHolder>  {
    private Context mainContext;
    String foto;
    SharedPreferences prefs;
    String FileName ="myfile";
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private List<PedidoRealmFirebase> items;
    public ArrayList<Adaptadorsolodetalle> todoslospedidos = new ArrayList<>();
    public Adaptadorrecibepedidos(ArrayList<PedidoRealmFirebase> items, Context contexto){
        this.mainContext=contexto;
        this.items=items;


    }
    static class AdaptadorViewHolder extends RecyclerView.ViewHolder{
        protected TextView nombre;
        protected TextView direccion,referencias,cuantopaga,vuelto,totalapagar1,idpedido;

        protected TextView telefono;
        protected Button wasap,rechazarpedido,muestrapedido;

        public AdaptadorViewHolder(View v){
            super(v);
            this.nombre=(TextView) v.findViewById(R.id.nombrecliente);
            this.direccion=(TextView) v.findViewById(R.id.direccion);
            this.totalapagar1=(TextView) v.findViewById(R.id.totalapagar1);
            this.telefono=(TextView) v.findViewById(R.id.telefono);
            this.referencias=(TextView) v.findViewById(R.id.referencias);
            this.cuantopaga=(TextView) v.findViewById(R.id.cuantopaga);
            this.vuelto=(TextView) v.findViewById(R.id.vuelto);
            this.idpedido=(TextView) v.findViewById(R.id.idpedido);

this.muestrapedido=(Button)v.findViewById(R.id.muestrapedido);
        }
    }
    @Override
    public Adaptadorrecibepedidos.AdaptadorViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tarjetapedidos,viewGroup,false);
        return new Adaptadorrecibepedidos.AdaptadorViewHolder(v);
    }
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final Adaptadorrecibepedidos.AdaptadorViewHolder viewHolder, final int position) {
        final PedidoRealmFirebase item = items.get(position);
        viewHolder.itemView.setTag(item);

        viewHolder.vuelto.setText(String.valueOf(item.getVuelto()));
        viewHolder.cuantopaga.setText(String.valueOf(item.getCuantopagaecliente()));
        viewHolder.referencias.setText(String.valueOf(item.getReferencias()));
        viewHolder.idpedido.setText(String.valueOf(item.getIdpedido()));
viewHolder.nombre.setText(String.valueOf(item.getEstadopedido()));
        viewHolder.direccion.setText(item.getDireccionallevar());
        viewHolder.totalapagar1.setText(String.valueOf(item.getTotalpedido()));
        viewHolder.telefono.setText(String.valueOf(item.getFechapedido()));


viewHolder.muestrapedido.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {


        new traertosddetalles().execute(String.valueOf(item.getIdpedido()));









    }
});


    }



    @Override
    public int getItemCount() {
        return items.size();
    }







    public class traertosddetalles extends AsyncTask<String, String, String> {

        HttpURLConnection conne;
        URL url = null;
        ProgressDialog pdLoading = new ProgressDialog(mainContext);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tCargando detalles");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                url = new URL("https://sodapop.pe/sugest/traerdetallesporpedido.php");
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
                        .appendQueryParameter("idpedido", params[0]);
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
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    Adaptadorsolodetalle pedidofirebase = new Adaptadorsolodetalle(json_data.getInt("iddetallepedido")
                            ,json_data.getInt("idproducto"),json_data.getInt("cantidad"),json_data.getDouble("precventa")
                            ,json_data.getDouble("subtotal"),json_data.getInt("idpedido"),json_data.getInt("idalmacen")
                            ,json_data.getString("adicionales"),json_data.getString("cremas"),json_data.getString("comentario")
                            ,json_data.getInt("ojo"),json_data.getString("imagenreal"),json_data.getString("comentarioacocina")
                            ,json_data.getString("nombreproductorealm"),json_data.getInt("idalmacenreal")
                    );
                    todoslospedidos.add(pedidofirebase);
                }
                pdLoading.dismiss();

                AlertDialog.Builder alert = new AlertDialog.Builder(mainContext);
                alert.setTitle("Detalle del pedido");
                final TextView input = new TextView (mainContext);
                alert.setView(input);
                input.setText("");
                int ga =todoslospedidos.size();
                String fodata="";
                String fodaa="";
                for (int ua=0;ua<ga;ua++) {
                    fodaa = "  "+String.valueOf(todoslospedidos.get(ua).getCantidad())+"  "+todoslospedidos.get(ua).getNombreproductorealm().toString()
                    +"  "+todoslospedidos.get(ua).getSubtotal();
                    fodata = fodata + fodaa + "\r\n";

                }
                input.setText("");
                input.setText(fodata);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
dialog.dismiss();
                    }
                });

                alert.show();






            } catch (JSONException e) {


                Log.d("erororor",e.toString());
            }


        }

    }
}

