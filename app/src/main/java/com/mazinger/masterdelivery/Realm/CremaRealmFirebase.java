package com.mazinger.masterdelivery.Realm;

public class CremaRealmFirebase {
    int idcrema;

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

    public int getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(int idproducto) {
        this.idproducto = idproducto;
    }

    public CremaRealmFirebase(
            int idcrema, String nombrecrema, String estadocrema, int id, int idproducto) {
        this.idcrema = idcrema;
        this.nombrecrema = nombrecrema;
        this.estadocrema = estadocrema;
        this.id = id;
        this.idproducto = idproducto;
    }

    String nombrecrema;
    String estadocrema;
    int id;

    int idproducto;

    public CremaRealmFirebase(){}



}
