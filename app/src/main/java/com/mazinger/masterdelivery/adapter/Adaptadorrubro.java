package com.mazinger.masterdelivery.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mazinger.masterdelivery.R;
import com.mazinger.masterdelivery.Realm.Crudiniciodeldia;
import com.mazinger.masterdelivery.Realm.Crudpedido;
import com.mazinger.masterdelivery.Realm.IniciodeldiaRealm;
import com.mazinger.masterdelivery.Realm.PedidoRealm;
import com.mazinger.masterdelivery.modelo.Rubroempresa;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class Adaptadorrubro extends RecyclerView.Adapter<Adaptadorrubro.AdaptadorViewHolder> {
    private Context mainContext;
    public String valorporcentaje, valorcantidad;
    String foto;
    SharedPreferences prefs;
    String FileName = "myfile", productocabecera;

    private List<Rubroempresa> items;
    ArrayList<Rubroempresa> Rubroempresa = new ArrayList<>();
    Rubroempresa objRubroempresa;
    ArrayList<Rubroempresa> peopleadicional = new ArrayList<>();
    String[] stradicional = {"No Suggestions"};

    public Adaptadorrubro(List<Rubroempresa> items, Context contexto) {
        this.mainContext = contexto;
        this.items = items;
    }

    static class AdaptadorViewHolder extends RecyclerView.ViewHolder {

        protected TextView idRubroempresal, textoscrool;

          protected ImageView imagendescrool;
        protected CardView tarjetascrool;


        public AdaptadorViewHolder(View v) {
            super(v);

            this.idRubroempresal = (TextView) v.findViewById(R.id.idrobruscrool);
            this.textoscrool = (TextView) v.findViewById(R.id.textoscrool);
             this.imagendescrool = (ImageView) v.findViewById(R.id.imagendescrool);
            this.tarjetascrool = (CardView) v.findViewById(R.id.tajetascrool);

        }
    }

    @Override
    public Adaptadorrubro.AdaptadorViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tarjetitamenuscrool, viewGroup, false);
        prefs = mainContext.getSharedPreferences(FileName, Context.MODE_PRIVATE);

        return new Adaptadorrubro.AdaptadorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final Adaptadorrubro.AdaptadorViewHolder viewHolder, final int position) {
        final Rubroempresa item = items.get(position);


        viewHolder.itemView.setTag(item);


       // new traerporcentajes().execute(String.valueOf(item.getIdRubroempresa()));



        viewHolder.idRubroempresal.setText(String.valueOf(item.getIdrubroempresa()));




        viewHolder.textoscrool.setText(item.getNombrerubro());


        foto = item.getImagenrubro().toString();
        Picasso.get().load(foto).transform(new CropCircleTransformation()).resize(60, 60)
                .into(viewHolder.imagendescrool);







        viewHolder.tarjetascrool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }





    // try {
    //  String idi = myIntent.getStringExtra("idRubroempresa"); // will return "FirstKeyValue"

    //String idalmacen = prefs.getString("idalmacenactivosf", "");

    //} catch (NumberFormatException e) {
    //  Log.i("tallo", e.toString());
    // }








    public final static void crearpedidoinicial(int idusuario, Double totalpedido, String fechapedido, int idalmacen) {
        Realm pedido = Realm.getDefaultInstance();
        pedido.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm pedido) {
                int gy= Crudpedido.calculateIndex();
                PedidoRealm creapedido = pedido.createObject(PedidoRealm.class, gy);
                creapedido.setIdusuario(idusuario);
                creapedido.setTotalpedido(totalpedido);
                creapedido.setFechapedido(fechapedido);
                creapedido.setIdalmacen(idalmacen);
                creapedido.setIdcliente(1);
                creapedido.setIdmesa(1);
                creapedido.setEstadopedido("1");
                creapedido.setDescripcionpedido("ssss");
                creapedido.setIdfacebook("www");
                creapedido.setTotal(0.0);
            }
        });
    }

    public final static void crearpedidoinicialdeldiarealm(int idusuario, Double totalpedido, String fechapedido, int idalmacen) {
        Realm pedido = Realm.getDefaultInstance();
        pedido.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm pedido) {
                int gy= Crudiniciodeldia.calculateIndex();
                IniciodeldiaRealm creapedido = pedido.createObject(IniciodeldiaRealm.class, gy);
                creapedido.setIdusuario(idusuario);

                creapedido.setIdalmacen(idalmacen);
                creapedido.setTotal(0.0);
            }
        });
    }

    public   void guardarRubroempresa(String idRubroempresa,String total){
        ;
        prefs = mainContext.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences =mainContext.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("idRubroempresa",idRubroempresa);
        editor.putString("tota",total);
        editor.commit();
    }
}

