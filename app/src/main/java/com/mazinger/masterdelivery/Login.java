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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.Nullable;
import com.mazinger.masterdelivery.modelo.Almacen;
import com.mazinger.masterdelivery.modelo.Usuarios;

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
import java.util.Collections;
import java.util.Date;

public class Login extends AppCompatActivity {
String nombreusuario1;
String claveusuario;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 101;

int idusuario;
    String FileName = "myfile";
    SharedPreferences prefs;

    ArrayList<Usuarios> peopleusuario = new ArrayList<>();
    ArrayList<String> dataListusuario = new ArrayList<String>();



    private String[] strArrDatausuario = {"No Suggestions"};
    private String[] strArrData = {"No Suggestions"};

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    TextView nombreuser;
    TextView clave;
Button nuevousuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prueba);


           nombreuser=(TextView) findViewById(R.id.phpnombreusuario);
        clave=(TextView) findViewById(R.id.phpclaveusuario);
        TextView  claveusuario=(TextView) findViewById(R.id.phpclaveusuario);
             Spinner spinerio=(Spinner) findViewById(R.id.spinnerio);
        Button va=(Button) findViewById(R.id.phpbtnloginphp) ;
nuevousuario=(Button) findViewById(R.id.sincuenta);

nuevousuario.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
            iraregistrarusuarionuevo();
    }
});
        new cargaralmacen().execute();
        va.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if( nombreuser.getText().toString().length() == 0 || claveusuario.getText().toString().length() == 0 ){
                    nombreuser.setError( "Debes digitar un nombre y clave de usuario" );
                }
                else{
                    if( nombreuser.getText().toString().length() == 0 ){
                        nombreuser.setError( "Debes digitar un nombre usuario" );
                    }   else{
                        if( claveusuario.getText().toString().length() == 0 ){
                            claveusuario.setError( "Debes digitar su clave" );
                        }else{
                            String al =spinerio.getItemAtPosition(spinerio.getSelectedItemPosition()).toString();
                            String mesei=al;
                            String mesi = mesei.substring(0, 2);
                            String mei=mesi.trim();
                            new Loginsinfacebook().execute(nombreuser.getText().toString(),claveusuario.getText().toString(),mei);
                        }

                    }
                }
            }
        });
    }

    private void iraregistrarusuarionuevo() {



        Intent intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setIsSmartLockEnabled(!com.firebase.ui.auth.BuildConfig.DEBUG)
                .setAvailableProviders(Collections.singletonList(
                        new AuthUI.IdpConfig.PhoneBuilder().build()))
                .setLogo(R.mipmap.ic_launcher)
                .build();

        startActivityForResult(intent, RC_SIGN_IN);


    }

    private class Loginsinfacebook extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(Login.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tCargando...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {
              url = new URL("https://www.sodapop.pe/sugest/apiloginnuevo.php");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", params[0])
                        .appendQueryParameter("password", params[1])
                        .appendQueryParameter("idalmacen", params[2]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return(result.toString());

                }else{

                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {



            peopleusuario.clear();
            Usuarios mesousuarios;
            if (result.equals("no rows")) {


            }

            else {
                try {
                    JSONArray jArray = new JSONArray(result);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.optJSONObject(i);
 mesousuarios = new Usuarios(json_data.getInt("idusuario"), json_data.getString("nombreusuario"), json_data.getString("claveusuario"), json_data.getInt("idalmacen"), json_data.getString("imagen"), json_data.getString("idfacebook"),json_data.getString("nombrefacebook"));
                        peopleusuario.add(mesousuarios);
                        idusuario=mesousuarios.getIdusuario();
                    }
                    strArrDatausuario = dataListusuario.toArray(new String[dataListusuario.size()]);
                    guardarsharesinfacebook(nombreuser.getText().toString(), clave.getText().toString(),idusuario,"","");

                    ir();
                    pdLoading.dismiss();
                }


                catch (JSONException e)
                {
                    Log.d("erroro",e.toString());
                    Toast.makeText(Login.this, "Upss! no estas registrado.", Toast.LENGTH_LONG).show();
                    pdLoading.dismiss();
                }

            }
        }

    }
    private class cargaralmacen extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(Login.this);
        HttpURLConnection conn;
        URL url = null;
        ArrayList<Almacen> listaalmacen = new ArrayList<Almacen>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tCargando Almacenes");
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
            ImageView im=(ImageView) findViewById(R.id.ima);
           Spinner spin=(Spinner) findViewById(R.id.spinnerio);
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.dechicoagrande);
            im.startAnimation(animation);
            ArrayList<String> dataList = new ArrayList<String>();
            Almacen mes;
            if(result.equals("no rows")) {
                Toast.makeText(Login.this,"no existen datos a mostrar",Toast.LENGTH_LONG).show();

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

                    ArrayAdapter<Almacen> adaptadorl= new ArrayAdapter<Almacen>(Login.this, android.R.layout.simple_spinner_item,listaalmacen );
                    spin.setAdapter(adaptadorl);


                } catch (JSONException e) {
                }

            }
            pdLoading.dismiss();


        }

    }
    public   void guardarsharesinfacebook(String nombreusuario,String claveusuario,int idusuario,String telefono,String iddefirebase){
        SharedPreferences sharedPreferences =getSharedPreferences(FileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        Spinner s=(Spinner)findViewById(R.id.spinnerio);
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
        editor.putString("idusuario",String.valueOf(idusuario));




        editor.commit();

    }
    private void ir(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());
        prefs = getApplicationContext().getSharedPreferences(FileName, Context.MODE_PRIVATE);
        String usuarior=prefs.getString("nombreusuariof","");
        String almacennombre=prefs.getString("almacenactivosf","");
       String idalmacen=prefs.getString("idalmacenactivosf","");
        String usuariostring   =prefs.getString("idusuario","");
        Intent i= new Intent(this,Listaparaseleccionar.class);
        startActivity(i);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse idpResponse = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                showAlertDialog(user);
            } else {
                /**
                 *   Sign in failed. If response is null the user canceled the
                 *   sign-in flow using the back button. Otherwise check
                 *   response.getError().getErrorCode() and handle the error.
                 */
                Toast.makeText(getBaseContext(), "Algo Fallo", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void showAlertDialog(FirebaseUser user) {
        AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(
                Login.this);

        // Set Title
        mAlertDialog.setTitle("Verificacion Satisfactoria");

        // Set Message
       // mAlertDialog.setMessage("Tu numero es  " + user.getPhoneNumber()+user.getUid());
        guardarsharesinfacebook("", "",0,user.getPhoneNumber(),user.getUid());
        registrodenuevousuario();

        mAlertDialog.create();

        // Showing Alert Message
        mAlertDialog.show();
    }



    private void registrodenuevousuario() {
        Intent pi;
        pi = new Intent(this,Registrodeusuario.class);
        startActivity(pi);
    }
}
