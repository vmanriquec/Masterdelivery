package com.mazinger.masterdelivery;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mazinger.masterdelivery.modelo.Almacen;
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
import java.util.regex.Pattern;

public class Registrodeusuario extends AppCompatActivity {

    private static  final String USUARIOf="datosusuario";
    private String[] strArrDatausuario = {"No Suggestions"};
    private String[] strArrData = {"No Suggestions"};
    SharedPreferences prefs;
    String FileName = "myfile";
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
TextView nombres,apellidos,telefono,direccion,correo,contrasena,recontrasena;
Button registro,mapa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        Spinner spinerio=(Spinner) findViewById(R.id.spinnerio);
        setContentView(R.layout.activity_registrodeusuario);

        prefs = getApplication().getSharedPreferences(FileName, Context.MODE_PRIVATE);
        String telefonoguardado = prefs.getString("telefono", "");
        String idfirebase = prefs.getString("idfirebase", "mi fire");
        String direccione=prefs.getString("direccion", "");
        String referencias=prefs.getString("referencia","");
        String latitud=prefs.getString("latitud","");
        String longitud=prefs.getString("longitud","");

        //Toast.makeText(getApplication(),"direccionooooooo"+longitud+latitud,Toast.LENGTH_LONG).show();
        nombres=(TextView) findViewById(R.id.nombrecompleto);
        apellidos=(TextView) findViewById(R.id.apellidos);
        telefono=(TextView) findViewById(R.id.telefono);
        contrasena=(TextView) findViewById(R.id.contra);
        recontrasena=(TextView) findViewById(R.id.recontra);
        direccion=(TextView) findViewById(R.id.dire);
        correo=(TextView) findViewById(R.id.correoelectronico);
        registro=(Button) findViewById(R.id.registrusuario);
        new cargaralmacen().execute();
telefono.setText(telefonoguardado);
direccion.setText(direccione);



        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validarEmail(correo.getText().toString())){
                    correo.setError("Correo no válido");
                }else{
                    String c=contrasena.getText().toString();
                    String rc=recontrasena.getText().toString();

                if  (c.equals(rc)){


                    guardarsharesinfacebook( nombres.getText().toString(),contrasena.getText().toString(),telefonoguardado,idfirebase);

                    String idalmacen=prefs.getString("idalmacenactivosf","");
   Usuariocompleto nuevousuario = new Usuariocompleto(nombres.getText().toString() ,
                            apellidos.getText().toString(),contrasena.getText().toString(),
                            "sin imagen","sin idfacebook","sin nombre ",
                            idfirebase,telefonoguardado,contrasena.getText().toString(), correo.getText().toString(),direccione,1,Integer.valueOf(idalmacen),latitud,longitud,referencias);
                    FirebaseDatabase database =FirebaseDatabase.getInstance();
                    FirebaseInstanceId.getInstance().getToken();
                    DatabaseReference reference=database.getReference(USUARIOf);
                    reference.child(idfirebase).setValue(nuevousuario);

if(idalmacen.equals("")){

    new cargaralmacen().execute();
}

else{
    new grabausuario().execute(nuevousuario);


}

//createDialog(nombres.getText().toString(),direccione);
                    irapedir();



                }
                else{


Toast.makeText(getApplication(),"Las contraseas no son iguales",Toast.LENGTH_LONG).show();



                }




                }

            }
        });




    }

    private void obtenerdireccion() {
        Intent pi;
        pi = new Intent(this,Mapa.class);
        startActivity(pi);
    }
    private void irapedir() {
        Intent pi;
        pi = new Intent(this,Muestartodaslasempresas.class);
        startActivity(pi);
    }

    private class grabausuario extends AsyncTask<Usuariocompleto, Void, String> {
        String resultado;
        HttpURLConnection conne;
        URL url = null;
        Usuariocompleto ped;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(Usuariocompleto... params) {
            ped=params[0];
            try {
                url = new URL("https://sodapop.pe/sugest/apigrabarusuario.php");
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

            if(resultado.equals("true")){
                Log.d("ii", resultado);


            }else{
                String ii =resultado.toString();
                Log.d("jj", "usuario valido");


                // lanzarsistema();
            }



        }
    }


    private AlertDialog createDialog(String title, String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //AlertDialog dialog = builder.create();

        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_header_layout, null);
        ((TextView)dialogView.findViewById(R.id.nombredialog)).setText(title);
        ((TextView)dialogView.findViewById(R.id.direccionio)).setText(msg);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        ((Button)dialogView.findViewById(R.id.vaallistado)).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                dialog.dismiss();
                irapedir();
            }
        });
        return dialog;
    }

    private class cargaralmacen extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(Registrodeusuario.this);
        HttpURLConnection conn;
        URL url = null;
        ArrayList<Almacen> listaalmacen = new ArrayList<Almacen>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tCargando Locales disponibles");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            Spinner  spin=(Spinner) findViewById(R.id.spinnerio);
            try {
                url = new URL("https://www.sodapop.pe/sugest/fetch-all-fish.php");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");
                conn.setDoOutput(true);
                conn.connect();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }
            try {
                int response_code = conn.getResponseCode();

                if (response_code == HttpURLConnection.HTTP_OK) {

                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return (result.toString());
                } else {
                    return("Connection error");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }
        }
        @Override
        protected void onPostExecute(String result) {
            pdLoading.dismiss();
            Spinner spin=(Spinner) findViewById(R.id.spinnerio2);

            ArrayList<String> dataList = new ArrayList<String>();
            Almacen mes;
            if(result.equals("no rows")) {
                Toast.makeText(Registrodeusuario.this,"no existen datos a mostrar",Toast.LENGTH_LONG).show();
            }else{
                try {
                    JSONArray jArray = new JSONArray(result);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);
                        dataList.add(json_data.getString("nombrealm"));
                        mes = new Almacen(json_data.getInt("idalmacen"), json_data.getString("nombrealm"), json_data.getString("telfonoalm"), json_data.getString("correoalm"));
                        listaalmacen.add(mes);
                    }
                    strArrData = dataList.toArray(new String[dataList.size()]);
                    ArrayAdapter<Almacen> adaptadorl= new ArrayAdapter<Almacen>(Registrodeusuario.this, android.R.layout.simple_spinner_item,listaalmacen );
                    spin.setAdapter(adaptadorl);
                } catch (JSONException e) {
                }

            }

        }

    }
    public   void guardarsharesinfacebook(String nombreusuario,String claveusuario,String telefono,String iddefirebase){
        SharedPreferences sharedPreferences =getSharedPreferences(FileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        Spinner s=(Spinner)findViewById(R.id.spinnerio2);
        String al =s.getItemAtPosition(s.getSelectedItemPosition()).toString();
        String mesei=al;
        int g= mesei.length();
        String mesi = mesei.substring(0,2);

        String  idalmacenactivosf=mesi.trim();

        String mesio = mesei.substring(3,g);
        String almacenactivosf=mesio.trim();
        editor.putString("facebook","no");
        editor.putString("nombreusuariof",nombreusuario);
        editor.putString("claveusuariof",claveusuario);
        editor.putString("almacenactivosf",almacenactivosf);
        editor.putString("idalmacenactivosf",idalmacenactivosf);
        editor.putString("editandopedidof","no");
        editor.putString("telefono",telefono);
        editor.putString("idfirebase",iddefirebase);
           editor.commit();

    }



    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
    @Override
    public void onBackPressed() {

    }
}
