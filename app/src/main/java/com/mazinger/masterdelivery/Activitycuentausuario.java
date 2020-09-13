package com.mazinger.masterdelivery;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.mazinger.masterdelivery.modelo.Usuariocompleto;

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
      apellidosa=(TextView) findViewById(R.id.apellidosa);
      correoelectronicoa=(TextView) findViewById(R.id.correoelectronicoa);
      telefonoa=(TextView) findViewById(R.id.telefonoa);
      contraa=(TextView) findViewById(R.id.contraa);
      recontraa=(TextView) findViewById(R.id.recontraa);

        new traerdatosdeusuario().execute(idfirebase);

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

}
