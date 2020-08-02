package com.mazinger.masterdelivery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import javax.annotation.Nullable;

import io.github.dreierf.materialintroscreen.MaterialIntroActivity;
import io.github.dreierf.materialintroscreen.MessageButtonBehaviour;
import io.github.dreierf.materialintroscreen.SlideFragmentBuilder;


public class IntroActivity extends MaterialIntroActivity {
    String FileName = "myfile";
    SharedPreferences prefs;

    ImageView intro, cov;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.introuno)
                        .buttonsColor(R.color.colorAccent)
                        .image(R.drawable.paraplay2)
                        .title("Reactiva tu Negocio")
                        .description("Vende on line YA!  registra tu negocio gratis y empieza  a crear tu calogo de productos y vende")
                        .build(),
                new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMessage("Juntos venceremos esta enfermedad");
                    }
                }, "afiliate gratis al 910260813"));

        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.introdos)
                        .buttonsColor(R.color.colorAccent)
                        .image(R.drawable.dos)
                        .title("Todo los que nesecitas a un toque")
                        .description("Muchos negocios y cientos de productos esperando por ti grandes promociones y descuentos solo buscalos y aprovechalos ")
                        .build(),
                new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMessage("Perseosystem  Huaral-2020");
                    }
                }, "Apoyemos nuestros negocios locales asi nos beneficiamos todos")

        );





    }
    @Override
    public void onFinish() {
        super.onFinish();
        prefs = getApplication().getSharedPreferences(FileName, Context.MODE_PRIVATE);

        String nombre = prefs.getString("nombreusuariof", "");
        String estadopedido = prefs.getString("estadopedido", "");

        if (nombre.equals("")) {
            //YoYo.with(Techniques.Landing.getAnimator())
              //      .duration(4700)
                //    .repeat(2)
                  //  .playOn(findViewById(R.id.intro));
            Intent i = new Intent(this, Nuevologin.class);
            startActivity(i);

        } else {

            Intent i = new Intent(this, Muestartodaslasempresas.class);
            startActivity(i);
        }

    }
}




