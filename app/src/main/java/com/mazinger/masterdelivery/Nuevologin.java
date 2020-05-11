package com.mazinger.masterdelivery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import java.util.Collections;

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



        if (nombre.equals("")){
            YoYo.with(Techniques.SlideInLeft)
                    .duration(1700)
                    .repeat(2)
                    .playOn(findViewById(R.id.imageView7));


        }
        else{


            Intent i= new Intent(this,Listaparaseleccionar.class);
            startActivity(i);
        }






        Button logincelu=(Button) findViewById(R.id.logincelu);

logincelu.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
     iraregistrarusuarionuevo();
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
        guardarsolotelefonoyiddefirebase(user.getPhoneNumber(),user.getUid());
        registrodenuevousuario();


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
}

