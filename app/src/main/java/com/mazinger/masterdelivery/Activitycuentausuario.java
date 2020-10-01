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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;
import com.mazinger.masterdelivery.adapter.Adaptadorcuensausuario;
import com.mazinger.masterdelivery.modelo.Usuariocompleto;
import com.mazinger.masterdelivery.modelo.Usuariodirecciones;

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

public class Activitycuentausuario extends AppCompatActivity {
    String FileName = "myfile";
    SharedPreferences prefs;
    TextView nombrecompletoa,apellidosa,correoelectronicoa,telefonoa,contraa,recontraa;
    Button adidionardireccion,registrusuarioa;
    private GoogleMap mMap;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cuentausuario);
        prefs = getApplication().getSharedPreferences(FileName, Context.MODE_PRIVATE);
        String idfirebase=prefs.getString("idfirebase","");
      nombrecompletoa=(TextView) findViewById(R.id.nombrecompletoa);
        adidionardireccion=(Button) findViewById(R.id.adidionardireccion);
        registrusuarioa=(Button) findViewById(R.id.registrusuarioa);
      apellidosa=(TextView) findViewById(R.id.apellidosa);
      correoelectronicoa=(TextView) findViewById(R.id.correoelectronicoa);
      telefonoa=(TextView) findViewById(R.id.telefonoa);
      contraa=(TextView) findViewById(R.id.contraa);
      recontraa=(TextView) findViewById(R.id.recontraa);

        new traerdatosdeusuario().execute(idfirebase);
        new trertodaslasdirecciones().execute(idfirebase);


        registrusuarioa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idfirebaseoi=prefs.getString("idfirebase","");

                Usuariocompleto nuevousuario = new Usuariocompleto(nombrecompletoa.getText().toString() ,
                        apellidosa.getText().toString(),contraa.getText().toString(),
                        "sin imagen","sin idfacebook","sin nombre ",
                        prefs.getString("idfirebase",""),idfirebaseoi,contraa.getText().toString(), correoelectronicoa.getText().toString(),"",1,1,"latitud","longitud","referencias");


                new actualizarusuario().execute(nuevousuario);



            }
        });
        adidionardireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Mapacitonulo.class);

                startActivity(intent);



            }
        });
    }



    public class traerdatosdeusuario extends AsyncTask<String, String, String> {

        HttpURLConnection conne;
        URL url = null;
        ProgressDialog pdLoading = new ProgressDialog(Activitycuentausuario.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tTrayendo todos tus datos");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                //  url = new URL("https://sodapop.pe/sugest/apitraertodoslostiposdepago.php");
                url = new URL("https://sodapop.pe/sugest/apitraerusuarioporidfirebase.php");
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
            ArrayList<Usuariocompleto> todaslasempresas = new ArrayList<>();
            todaslasempresas.clear();
            pdLoading.dismiss();
            try {



                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);

                    Usuariocompleto pedidofirebase = new Usuariocompleto
                            (

                            json_data.getString("nombreusuario"),
                            json_data.getString("apellidos"),
                            json_data.getString("claveusuario"),
                            json_data.getString("imagen"),
                            json_data.getString("idfacebook"),
                            json_data.getString("nombrefacebook"),
                            json_data.getString("idfirebase"),
                            json_data.getString("telefono"),
                            json_data.getString("contrasena"),
                            json_data.getString("correo"),
                            json_data.getString("direccion"),
                            json_data.getInt("idusuario"),
                                    json_data.getInt("idalmacen"),
                            json_data.getString("longitud"),
                            json_data.getString("latitud"),
                            json_data.getString("referencia")

                    );
                    todaslasempresas.add(pedidofirebase);


                }

                nombrecompletoa.setText(todaslasempresas.get(0).getNombreusuario().toString());
                apellidosa.setText(todaslasempresas.get(0).getApellidos().toString());
                correoelectronicoa.setText(todaslasempresas.get(0).getCorreo().toString());
                telefonoa.setText(todaslasempresas.get(0).getTelefono().toString());
                contraa.setText(todaslasempresas.get(0).getContrasena().toString());
                recontraa.setText(todaslasempresas.get(0).getContrasena().toString());

               // final RecyclerView pedidose = findViewById(R.id.recydeempresasl);

                //Adaptadorempresa adaptadore = new Adaptadorempresa(todaslasempresas, Muestartodaslasempresas.this);
                //pedidose.setLayoutManager(new GridLayoutManager(Muestartodaslasempresas.this.getApplicationContext(), 1));

                //pedidose.setAdapter(adaptadore);

                //adaptadore.notifyDataSetChanged();

            } catch (JSONException e) {
                Log.d("erororor", e.toString());
            }
        }

    }




    public class trertodaslasdirecciones extends AsyncTask<String, String, String> {

        HttpURLConnection conne;
        URL url = null;
        ProgressDialog pdLoading = new ProgressDialog(Activitycuentausuario.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tTrayendo tus direcciones registradas");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                //  url = new URL("https://sodapop.pe/sugest/apitraertodoslostiposdepago.php");
                url = new URL("https://sodapop.pe/sugest/apitraertodaslasdirecconesporidfirebase.php");
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
            ArrayList<Usuariodirecciones> todaslasempresas = new ArrayList<>();
            todaslasempresas.clear();
            pdLoading.dismiss();
            try {



                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);

                    Usuariodirecciones pedidofirebase = new Usuariodirecciones(

                                    json_data.getInt("idusuariodireccione"),
                                    json_data.getString("idfirebase"),
                                    json_data.getString("direccion"),
                                    json_data.getString("longitud"),
                                    json_data.getString("latitud"),
                                    json_data.getString("referencia")

                            );
                    todaslasempresas.add(pedidofirebase);


                }


                 final RecyclerView recydirecto = findViewById(R.id.recydirecto);

                Adaptadorcuensausuario adaptadore = new Adaptadorcuensausuario(todaslasempresas, Activitycuentausuario.this);
                recydirecto.setLayoutManager(new GridLayoutManager(Activitycuentausuario.this.getApplicationContext(), 1));
                recydirecto.setAdapter(adaptadore);
                adaptadore.notifyDataSetChanged();

            } catch (JSONException e) {
                Log.d("erororor", e.toString());
            }
        }

    }

    private class actualizarusuario extends AsyncTask<Usuariocompleto, Void, String> {
        String resultado;
        HttpURLConnection conne;
        URL url = null;
        Usuariocompleto ped;
        ProgressDialog pdLoading = new ProgressDialog(Activitycuentausuario.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tActualizando datos");
            pdLoading.setCancelable(false);
            pdLoading.show();


        }

        @Override
        protected String doInBackground(Usuariocompleto... params) {
            ped=params[0];
            try {
                url = new URL("https://sodapop.pe/sugest/apiactualizarusuario.php");
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


                Log.d("valor",String.valueOf(ped.getLongitud()));
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("nombreusuario",String.valueOf(ped.getNombreusuario()))
                        .appendQueryParameter("apellidos",String.valueOf(ped.getApellidos()))
                        .appendQueryParameter("claveusuario",String.valueOf(ped.getContrasena()))
                        .appendQueryParameter("imagen", String.valueOf(ped.getIdalmacen()))
                        .appendQueryParameter("idfirebase", String.valueOf(ped.getIdfirebase()))
                        .appendQueryParameter("idalmacen", String.valueOf(ped.getIdalmacen()))
                        .appendQueryParameter("contrasena", String.valueOf(ped.getContrasena()))
                        .appendQueryParameter("correo", String.valueOf(ped.getCorreo()))
                        .appendQueryParameter("direccion", String.valueOf(ped.getDireccion()))
                        .appendQueryParameter("telefono", String.valueOf(ped.getTelefono()))
                        .appendQueryParameter("latitud", String.valueOf(ped.getLatitud()))
                        .appendQueryParameter("longitud", String.valueOf(ped.getLongitud()))
                        .appendQueryParameter("referencia", String.valueOf(ped.getReferencia()))

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
            pdLoading.dismiss();
            Intent intent = new Intent(getApplicationContext(), Muestartodaslasempresas.class);

            startActivity(intent);





        }
    }

}
