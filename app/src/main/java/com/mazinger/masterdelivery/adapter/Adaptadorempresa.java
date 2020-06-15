package com.mazinger.masterdelivery.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mazinger.masterdelivery.Muestraproductosporempresa;
import com.mazinger.masterdelivery.R;
import com.mazinger.masterdelivery.modelo.Empresa;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropSquareTransformation;

public class Adaptadorempresa extends RecyclerView.Adapter<Adaptadorempresa.AdaptadorViewHolder> {
private Context mainContext;
        String foto;
        SharedPreferences prefs;
        String FileName ="myfile",productocabecera;
private List<Empresa> items;
        ArrayList<Empresa> Empresa=new ArrayList<>();
        Empresa objEmpresa;
        ArrayList<Empresa> peopleadicional = new ArrayList<>();
        String[] stradicional = {"No Suggestions"};

public Adaptadorempresa(List<Empresa> items, Context contexto){
        this.mainContext=contexto;
        this.items=items;
        }
static class AdaptadorViewHolder extends RecyclerView.ViewHolder{
    protected TextView nombreempresa,idempresal;
    protected TextView nombrerubro;
    //protected TextView precio;
    protected TextView montominimodeventa,costodelivery;
    protected ImageView imagenempresa1;
    protected TextView tiempodeesperal;
    protected ImageView imagenefectivol,
            imagenvisal,imastercardl;
    protected CardView tarjetaempresa;

    public AdaptadorViewHolder(View v){
        super(v);
        this.idempresal=(TextView) v.findViewById(R.id.idempresal);
        this.nombreempresa=(TextView) v.findViewById(R.id.nombreempresa);
        this.nombrerubro=(TextView) v.findViewById(R.id.rubroempresa);
        this.montominimodeventa=(TextView) v.findViewById(R.id.montominimodeventa);
        this.imagenempresa1=(ImageView) v.findViewById(R.id.imagenempresa1);
        this.tiempodeesperal=(TextView) v.findViewById(R.id.tiempodeesperal);
        this.costodelivery=(TextView) v.findViewById(R.id.costodeliverycard);
this.tarjetaempresa=(CardView)v.findViewById(R.id.tarjetaempresa);



    }
}
    @Override
    public Adaptadorempresa.AdaptadorViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tarjetitaempresas,viewGroup,false);
        return new Adaptadorempresa.AdaptadorViewHolder(v);
    }
    @Override
    public void onBindViewHolder(final Adaptadorempresa.AdaptadorViewHolder viewHolder, final int position) {
        final Empresa item = items.get(position);
        viewHolder.itemView.setTag(item);
        viewHolder.idempresal.setText(String.valueOf(item.getIdempresa()));

        viewHolder.nombreempresa.setText(item.getRazonsocialempresa());
        viewHolder.nombrerubro.setText(String.valueOf(item.getNombrerubro()));
        viewHolder.montominimodeventa.setText("S/. " + String.valueOf(item.getMontominimodeventa()));
        viewHolder.costodelivery.setText( "S/. " +String.valueOf(item.getCostodelivery()));
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

