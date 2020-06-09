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
import com.mazinger.masterdelivery.Listaparaseleccionar;
import com.mazinger.masterdelivery.Muestraproductosporempresa;
import com.mazinger.masterdelivery.R;
import com.mazinger.masterdelivery.modelo.Empresa;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropSquareTransformation;


public class Adaptadorbuscarprodenempresas extends RecyclerView.Adapter<Adaptadorbuscarprodenempresas.AdaptadorViewHolder> {
private Context mainContext;
        String foto;
        SharedPreferences prefs;
        String FileName ="myfile",productocabecera;
private List<Empresa> items;
        ArrayList<Empresa> Empresa=new ArrayList<>();
        Empresa objEmpresa;
        ArrayList<Empresa> peopleadicional = new ArrayList<>();
        String[] stradicional = {"No Suggestions"};

public Adaptadorbuscarprodenempresas(List<Empresa> items, Context contexto){
        this.mainContext=contexto;
        this.items=items;
        }
static class AdaptadorViewHolder extends RecyclerView.ViewHolder{
    protected TextView nombreempresa,idempresal;
    protected TextView sloganempresa;
    protected ImageView imagenempresa1;
    protected ImageView imagenproductobu;
    protected TextView tiempodeesperal;
    protected CardView tarjetaempresa;
    protected TextView nombreproductobu;
    public AdaptadorViewHolder(View v){
        super(v);
        this.idempresal=(TextView) v.findViewById(R.id.idempresabu);
        this.nombreempresa=(TextView) v.findViewById(R.id.nombrempresabu);

        this.imagenempresa1=(ImageView) v.findViewById(R.id.imagendeempresabu);
        this.tiempodeesperal=(TextView) v.findViewById(R.id.tiempodemorabu);

        this.tarjetaempresa=(CardView)v.findViewById(R.id.tarjetaempresabu);


    }
}
    @Override
    public Adaptadorbuscarprodenempresas.AdaptadorViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tarjetitaproductosporempresa,viewGroup,false);
        return new Adaptadorbuscarprodenempresas.AdaptadorViewHolder(v);
    }
    @Override
    public void onBindViewHolder(final Adaptadorbuscarprodenempresas.AdaptadorViewHolder viewHolder, final int position) {
        final Empresa item = items.get(position);
        viewHolder.itemView.setTag(item);
        viewHolder.idempresal.setText(String.valueOf(item.getIdempresa()));

        viewHolder.nombreempresa.setText(item.getRazonsocialempresa());
         foto = item.getLogotipoempresa().toString();
        Picasso.get().load(foto).transform(new CropSquareTransformation()).resize(200, 200)
                .into(viewHolder.imagenempresa1);


        viewHolder.tiempodeesperal.setText(item.getTiempodedemoraempresa());
        //viewHolder.im.setText(String.valueOf(porcenta+item.getPrecventa()));
        //viewHolder.preciodescuento.setPaintFlags(viewHolder.preciodescuento.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        viewHolder.tarjetaempresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Muestraproductosporempresa.class);
                intent.putExtra("idempresa",String.valueOf(item.getIdempresa()));
                intent.putExtra("nombreempresa",String.valueOf(item.getRazonsocialempresa()));
                intent.putExtra("telefonoempresa",String.valueOf(item.getTelefonoempresa()));
                intent.putExtra("tiempodemora",String.valueOf(item.getTiempodedemoraempresa()));
                intent.putExtra("montominimo",String.valueOf(item.getMontominimodeventa()));

                //intent.putExtra("idproductoseleccionado",String.valueOf(item.getIdproducto()));
                v.getContext().startActivity(intent);
            }
        });




    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}


