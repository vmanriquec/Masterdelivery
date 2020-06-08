package com.mazinger.masterdelivery.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mazinger.masterdelivery.G;
import com.mazinger.masterdelivery.R;
import com.mazinger.masterdelivery.modelo.Adicional;
import com.mazinger.masterdelivery.modelo.Detallepedido;
import com.mazinger.masterdelivery.modelo.Productos;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropSquareTransformation;

public class Adaptadorproductoempresa extends RecyclerView.Adapter<Adaptadorproductoempresa.AdaptadorViewHolder> {
private Context mainContext;
        String foto;
        SharedPreferences prefs;
        String FileName ="myfile",productocabecera;
private List<Productos> items;
        ArrayList<Detallepedido> detallepedido=new ArrayList<>();
        Detallepedido objdetallepedido;
        ArrayList<Adicional> peopleadicional = new ArrayList<>();
        String[] stradicional = {"No Suggestions"};
        ArrayList<String> datalistadicional = new ArrayList<String>();
public Adaptadorproductoempresa(List<Productos> items, Context contexto){
        this.mainContext=contexto;
        this.items=items;
        }
static class AdaptadorViewHolder extends RecyclerView.ViewHolder{
    protected TextView productonombre;
    protected TextView idproducto;
    protected TextView precio;
    protected TextView cantidadpedida;
    protected ImageView imagen;
    protected TextView productoingredientes, inventario,preciodescuento;

    protected CardView mas;

    public AdaptadorViewHolder(View v){
        super(v);
        this.idproducto=(TextView) v.findViewById(R.id.idproductope);
        this.productonombre=(TextView) v.findViewById(R.id.nomnrepe);


        this.mas=(CardView) v.findViewById(R.id.cardpe);

        this.precio=(TextView) v.findViewById(R.id.preciope);



        this.productoingredientes=(TextView)v.findViewById(R.id.descripcionpe);
        this.imagen=(ImageView)v.findViewById(R.id.imagenpe);


    }
}
    @Override
    public Adaptadorproductoempresa.AdaptadorViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tarjetitaproductosporempresa,viewGroup,false);
        return new Adaptadorproductoempresa.AdaptadorViewHolder(v);
    }
    @Override
    public void onBindViewHolder(final Adaptadorproductoempresa.AdaptadorViewHolder viewHolder, final int position) {

        final Productos item = items.get(position);
        viewHolder.itemView.setTag(item);

        viewHolder.productonombre.setText(item.getNombreproducto());
        productocabecera=item.getNombreproducto();
        viewHolder.idproducto.setText(String.valueOf(item.getIdproducto()));
        viewHolder.productoingredientes.setText(String.valueOf(item.getIngredientes()));
        viewHolder.precio.setText("S/. " + String.valueOf(item.getPrecventa()));


        foto = item.getDescripcion().toString();
        Picasso.get().load(foto).transform(new CropSquareTransformation()).resize(200, 200)
                .into(viewHolder.imagen);
        // viewHolder.mas.setVisibility(View.GONE);
        //viewHolder.menos.setVisibility(View.GONE);
        viewHolder.mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), G.class);
                intent.putExtra("nombredeproductoseleccionado",item.getNombreproducto());
                intent.putExtra("preciodeproductoseleccionado",String.valueOf(item.getPrecventa()));
                intent.putExtra("idproductoseleccionado",String.valueOf(item.getIdproducto()));
                v.getContext().startActivity(intent);





            }
        });

        viewHolder.imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast ImageToast = new Toast(mainContext.getApplicationContext());
                LinearLayout toastLayout = new LinearLayout(mainContext.getApplicationContext());
                toastLayout.setOrientation(LinearLayout.VERTICAL);
                ImageView image = new ImageView(mainContext.getApplicationContext());
                TextView text = new TextView(mainContext.getApplicationContext());
                foto=item.getDescripcion().toString();
                Picasso.get().load("image_URL").into(viewHolder.imagen);
                Picasso.get().load(foto).transform(new CropSquareTransformation())
                        .resize(550, 550)
                        .into( image);
                text.setText(item.getIngredientes());
                text.setTextColor(Color.RED);
                text.setBackgroundColor(Color.WHITE);
                text.setGravity(12);
                toastLayout.addView(image);
                toastLayout.addView(text);
                ImageToast.setView(toastLayout);
                ImageToast.setGravity (Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 40, 40);
                ImageToast.setDuration(Toast.LENGTH_LONG);
                ImageToast.show();
                ImageToast.getView().setPadding( 20, 20, 20, 20);
                Picasso.get().load(foto).transform(new CropSquareTransformation()).resize(200, 200)
                        .into(viewHolder.imagen);

            }
        });
        /*si esta check activo para aumentar cantidad*/







    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

