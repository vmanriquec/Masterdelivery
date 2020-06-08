package com.mazinger.masterdelivery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class Splash extends AppCompatActivity {
    String FileName = "myfile";
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        prefs = getApplication().getSharedPreferences(FileName, Context.MODE_PRIVATE);

        String nombre = prefs.getString("nombreusuariof", "");
        String estadopedido = prefs.getString("estadopedido", "");

        if (nombre.equals("")) {
            YoYo.with(Techniques.Landing.getAnimator())
                    .duration(4700)
                    .repeat(2)
                    .playOn(findViewById(R.id.intro));
            Intent i = new Intent(this, Nuevologin.class);
            startActivity(i);

        } else {

                Intent i = new Intent(this, Muestartodaslasempresas.class);
                startActivity(i);
        }


    }

}
