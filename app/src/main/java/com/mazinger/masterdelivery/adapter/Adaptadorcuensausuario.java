package com.mazinger.masterdelivery.adapter;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mazinger.masterdelivery.Activitycuentausuario;
import com.mazinger.masterdelivery.R;
import com.mazinger.masterdelivery.modelo.Usuariodirecciones;

import java.util.ArrayList;
import java.util.List;

public class Adaptadorcuensausuario extends RecyclerView.Adapter<Adaptadorcuensausuario.AdaptadorViewHolder>  {
    private Activitycuentausuario mainContext;
    String foto;
    SharedPreferences prefs;
    String FileName ="myfile";

    private List<Usuariodirecciones> items;
    public Adaptadorcuensausuario(ArrayList<Usuariodirecciones> items, Activitycuentausuario contexto){
        this.mainContext=contexto;
        this.items=items;


    }
    static class AdaptadorViewHolder extends RecyclerView.ViewHolder{
        protected TextView latitudrecy;
        protected TextView longitudrecy;
        protected TextView direccioncuenta;
        protected Button eliminadireccion;



        public AdaptadorViewHolder(View v){
            super(v);
            this.latitudrecy=(TextView) v.findViewById(R.id.latitudrecy);
            this.longitudrecy=(TextView) v.findViewById(R.id.longitudrecy);
            this.direccioncuenta=(TextView) v.findViewById(R.id.direccioncuenta);
            this.eliminadireccion=(Button) v.findViewById(R.id.eliminadireccion);
        }
    }


    @Override
    public Adaptadorcuensausuario.AdaptadorViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tarjetadireccionesdeusuario,viewGroup,false);
        return new Adaptadorcuensausuario.AdaptadorViewHolder(v);
    }
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final Adaptadorcuensausuario.AdaptadorViewHolder viewHolder, final int position) {
        final Usuariodirecciones item = items.get(position);
        viewHolder.itemView.setTag(item);


        viewHolder.latitudrecy.setText(String.valueOf(item.getLatitud()));
        viewHolder.longitudrecy.setText(item.getLongitud());
        viewHolder.direccioncuenta.setText(String.valueOf(item.getDireccion()));

        viewHolder.eliminadireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idfirebase=items.get(position).getIdfirebase();
                int ii=items.get(position).getIdusuariodireccione();


                items.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
                //mainContext.recargartotalesisisomos();
                //mainContext.calculatotal();




            }
        });
    }



    @Override
    public int getItemCount() {
        return items.size();
    }

}

