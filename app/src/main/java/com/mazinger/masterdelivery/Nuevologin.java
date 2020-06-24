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
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.Nullable;
import com.mazinger.masterdelivery.modelo.Adicional;
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
import java.util.Collections;

import static com.mazinger.masterdelivery.Login.CONNECTION_TIMEOUT;
import static com.mazinger.masterdelivery.Login.READ_TIMEOUT;

public class Nuevologin extends AppCompatActivity {
    String FileName = "myfile";
    SharedPreferences prefs;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        prefs = getApplication().getSharedPreferences(FileName, Context.MODE_PRIVATE);
        String nombre=prefs.getString("idfirebase","");
        Button logincelu=(Button) findViewById(R.id.logincelu);
logincelu.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (nombre.equals("")){
            YoYo.with(Techniques.SlideInLeft)
                    .duration(1700)
                    .repeat(2)
                    .playOn(findViewById(R.id.imageView7));
            iraregistrarusuarionuevo();
        }
        else{
            Intent i= new Intent(Nuevologin.this,Muestartodaslasempresas.class);
            startActivity(i);
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
                Nuevologin.this);
        mAlertDialog.create();
        // Showing Alert Message
        mAlertDialog.show();
      new   verificarsiusuarioexiste().execute(user.getUid());
       guardarsolotelefonoyiddefirebase(user.getPhoneNumber(),user.getUid());
  //      registrodenuevousuario();
    }
    private void veapedir() {
        Intent pi;
        pi = new Intent(this,Muestraproductosporempresa.class);
        startActivity(pi);
    }
    private void registrodenuevousuario() {
        Intent pi;
        pi = new Intent(this,Mapa.class);
        startActivity(pi);
    }
    public   void guardarsolotelefonoyiddefirebase(String telefono,String iddefirebase){
        SharedPreferences sharedPreferences =getSharedPreferences(FileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("telefono",telefono);
        editor.putString("idfirebase",iddefirebase);
        editor.commit();
    }
    public   void guardarensharesiyaestaregistrado(String telefono,String iddefirebase,String nombreusuariof,String almacenactivosf,String idalmacenactivosf,String direccion
    ,String latitud,String longitud,String referencia){
        SharedPreferences sharedPreferences =getSharedPreferences(FileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("telefono",telefono);
        editor.putString("idfirebase",iddefirebase);
        editor.putString("nombreusuariof",nombreusuariof);
        editor.putString("almacenactivosf",almacenactivosf);
        editor.putString("idalmacenactivosf",idalmacenactivosf);
        editor.putString("direccion",direccion);
        editor.putString("referencia",referencia);
        editor.putString("latitud",latitud);
        editor.putString("longitud",longitud);





        editor.commit();

    }
    private class verificarsiusuarioexiste extends AsyncTask<String, String, String> {
        HttpURLConnection conne;
        ProgressDialog pdLoading = new ProgressDialog(Nuevologin.this);
        URL url = null;
        ArrayList<Adicional> listadeadicionales = new ArrayList<Adicional>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pdLoading.setMessage("\tvalidando   usuario...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL("https://sodapop.pe/sugest/traerusuarioportelefono.php");
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
           // Animation a = AnimationUtils.loadAnimation(getApplication(), R.anim.dechicoagrande);
            //a.reset();
            pdLoading.dismiss();
            ArrayList<Usuariocompleto> peopleadicional = new ArrayList<>();
            String[] stradicional = {"No Suggestions"};
            ArrayList<String> datalistadicional = new ArrayList<String>();
            Usuariocompleto mesoadiconal;
            peopleadicional.clear();
            if (result.equals("null")) {
                registrodenuevousuario();


            }


            else {
                try {
                    JSONArray jArray = new JSONArray(result);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.optJSONObject(i);
                        mesoadiconal = new Usuariocompleto(
                                json_data.getString("nombreusuario"),json_data.getString("apellidos"),json_data.getString("claveusuario")
                                ,json_data.getString("imagen"),json_data.getString("idfacebook"),json_data.getString("nombrefacebook")
                                ,json_data.getString("idfirebase"),json_data.getString("telefono"),json_data.getString("contrasena")
                                ,json_data.getString("correo"),json_data.getString("direccion"),json_data.getInt("idusuario")
                                ,json_data.getInt("idalmacen"),json_data.getString("latitud"),json_data.getString("longitud")
                                ,json_data.getString("referencia")
                                );

                        peopleadicional.add(mesoadiconal);
                    }

                    if(peopleadicional.get(0).getTelefono().equals(""))
                    {
                        guardarsolotelefonoyiddefirebase(peopleadicional.get(0).getTelefono(),peopleadicional.get(0).getIdfirebase());
                        registrodenuevousuario();
                    }else{
                        guardarensharesiyaestaregistrado(peopleadicional.get(0).getTelefono(),peopleadicional.get(0).getIdfirebase(),
                                peopleadicional.get(0).getNombreusuario(),String.valueOf(peopleadicional.get(0).getIdalmacen()),String.valueOf(peopleadicional.get(0).getIdalmacen()),
                                String.valueOf(peopleadicional.get(0).getDireccion()),peopleadicional.get(0).getLatitud()
                        ,peopleadicional.get(0).getLongitud(),peopleadicional.get(0).getReferencia());

                        Toast.makeText(getApplicationContext(),"Hola ya estas registrado, sigue efectuando tus compras ",Toast.LENGTH_LONG).show();
                        veapedir();
                    }


                } catch (JSONException e) {
                    Log.d("erroro",e.toString());
                }
            }
        }

    }
    @Override
    public void onBackPressed() {
    }
}

