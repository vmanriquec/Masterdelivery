package com.mazinger.masterdelivery.Realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CremaRealm extends RealmObject {
    @PrimaryKey
    int idcrema;
        String nombrecrema;
    String estadocrema;
    int id;

int idproducto;

    public int getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(int idproducto) {
        this.idproducto = idproducto;
    }

    @Override
    public String toString() {
        return "CremaRealm{" +
                "idcrema=" + idcrema +
                ", nombrecrema='" + nombrecrema + '\'' +
                '}';
    }


    public int getIdcrema() {
        return idcrema;
    }

    public void setIdcrema(int idcrema) {
        this.idcrema = idcrema;
    }

    public String getNombrecrema() {
        return nombrecrema;
    }

    public void setNombrecrema(String nombrecrema) {
        this.nombrecrema = nombrecrema;
    }

    public String getEstadocrema() {
        return estadocrema;
    }

    public void setEstadocrema(String estadocrema) {
        this.estadocrema = estadocrema;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
