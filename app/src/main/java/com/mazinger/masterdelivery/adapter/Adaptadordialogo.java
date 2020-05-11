package com.mazinger.masterdelivery.adapter;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mazinger.masterdelivery.G;
import com.mazinger.masterdelivery.R;
import com.mazinger.masterdelivery.Verpedidodos;
import com.mazinger.masterdelivery.modelo.Datostarjetadialogo;

import java.util.ArrayList;
import java.util.List;

public class Adaptadordialogo extends RecyclerView.Adapter<Adaptadordialogo.AdaptadorViewHolder>  {
private Verpedidodos mainContext;
        String foto;
        SharedPreferences prefs;
        String FileName ="myfile";

private List<Datostarjetadialogo> items;
public Adaptadordialogo(ArrayList<Datostarjetadialogo> items, Verpedidodos contexto){
        this.mainContext=contexto;
        this.items=items;


        }
static class AdaptadorViewHolder extends RecyclerView.ViewHolder{
    protected TextView cantidad;
    protected TextView productonombre;
    protected TextView total;
    protected Button eliminar;



    public AdaptadorViewHolder(View v){
        super(v);
        this.cantidad=(TextView) v.findViewById(R.id.cantidadencardview);
        this.productonombre=(TextView) v.findViewById(R.id.productoencardview);
        this.total=(TextView) v.findViewById(R.id.preciototalproductocardview);
        this.eliminar=(Button) v.findViewById(R.id.eliminardedialog);
    }
}


    @Override
    public Adaptadordialogo.AdaptadorViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tarjetamuestraendialogopedidos,viewGroup,false);
        return new Adaptadordialogo.AdaptadorViewHolder(v);
    }
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final Adaptadordialogo.AdaptadorViewHolder viewHolder, final int position) {
        final Datostarjetadialogo item = items.get(position);
        viewHolder.itemView.setTag(item);


        viewHolder.cantidad.setText(String.valueOf(item.getCantidad()));
        viewHolder.productonombre.setText(item.getProducto());
        viewHolder.total.setText(String.valueOf(item.getTotal()));

        viewHolder.eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              String producto=items.get(position).getProducto();
                String subtotal =String.valueOf(items.get(position).getTotal());

                      int numerito= G.capturariddedetalledeprodysubtotal(producto,subtotal);
///Toast.makeText(mainContext,"el id de detalle"+String.valueOf(numerito),Toast.LENGTH_LONG).show();


                //int tr= Crudetallepedido.calculateIndex();
                //int iddedetalles=Crudetallepedido.calculateIndex();



                G.eliminaraunTotalcrema(numerito);
                G.eliminarunTotaladicional(numerito);
                G.eliminarunTOTALdetallepedido(numerito);
                items.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
                mainContext.recargartotalesisisomos();
mainContext.calculatotal();




            }
        });
    }



    @Override
    public int getItemCount() {
        return items.size();
    }

}

