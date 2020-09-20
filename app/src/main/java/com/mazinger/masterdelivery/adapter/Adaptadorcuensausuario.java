package com.mazinger.masterdelivery.adapter;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mazinger.masterdelivery.Activitycuentausuario;
import com.mazinger.masterdelivery.R;
import com.mazinger.masterdelivery.modelo.Usuariodirecciones;

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

public class Adaptadorcuensausuario extends RecyclerView.Adapter<Adaptadorcuensausuario.AdaptadorViewHolder>  {
    private Activitycuentausuario mainContext;
    String foto;
    SharedPreferences prefs;
    String FileName ="myfile";
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private List<Usuariodirecciones> items;
    public Adaptadorcuensausuario(ArrayList<Usuariodirecciones> items, Activitycuentausuario contexto){
        this.mainContext=contexto;
        this.items=items;


    }
    static class AdaptadorViewHolder extends RecyclerView.ViewHolder{
        protected TextView latitudrecy;
        protected TextView idususriodireccion;
        protected TextView longitudrecy;
        protected TextView direccioncuenta;
        protected Button eliminadireccion;



        public AdaptadorViewHolder(View v){
            super(v);
            this.latitudrecy=(TextView) v.findViewById(R.id.latitudrecy);
            this.idususriodireccion=(TextView) v.findViewById(R.id.idususriodireccion);
            this.longitudrecy=(TextView) v.findViewById(R.id.longitudrecy);
            this.direccioncuenta=(TextView) v.findViewById(R.id.direccioncuenta);
            this.eliminadireccion=(Button) v.findViewById(R.id.eliminadireccion);
        }
    }


    @Override
    public Adaptadorcuensausuario.AdaptadorViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tarjetadireccionesdeusuario,viewGroup,false);
        return new Adaptadorcuensausuario.AdaptadorViewHolder(v);
    }
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final Adaptadorcuensausuario.AdaptadorViewHolder viewHolder, final int position) {
        final Usuariodirecciones item = items.get(position);
        viewHolder.itemView.setTag(item);

        viewHolder.idususriodireccion.setText(String.valueOf(item.getIdusuariodireccione()));
        viewHolder.latitudrecy.setText(String.valueOf(item.getLatitud()));
        viewHolder.longitudrecy.setText(item.getLongitud());
        viewHolder.direccioncuenta.setText(String.valueOf(item.getDireccion()));

        viewHolder.eliminadireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idfirebase=items.get(position).getIdfirebase();
                int ii=items.get(position).getIdusuariodireccione();

Usuariodirecciones hy=new Usuariodirecciones(Integer.parseInt(viewHolder.idususriodireccion.getText().toString()),
         "","","","","");

                new eliminardireccion().execute(viewHolder.idususriodireccion.getText().toString());


                items.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
                //mainContext.recargartotalesisisomos();
                //mainContext.calculatotal();




            }
        });


    }


    private class eliminardireccion extends AsyncTask<String, String, String> {

        private String[] strArrData = {"No Suggestions"};

        HttpURLConnection conne;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {

            try {
                url = new URL("https://sodapop.pe/sugest/apieliminardireccionporusuario.php");
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

                        .appendQueryParameter("idususriodireccion", params[0])

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
            Log.d("paso",result.toString());

        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}

