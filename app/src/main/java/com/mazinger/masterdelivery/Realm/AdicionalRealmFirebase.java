package com.mazinger.masterdelivery.Realm;

public class AdicionalRealmFirebase {

    int idadicional;

    String nombreadicional;

    public int getIdadicional() {
        return idadicional;
    }

    public void setIdadicional(int idadicional) {
        this.idadicional = idadicional;
    }

    public String getNombreadicional() {
        return nombreadicional;
    }

    public void setNombreadicional(String nombreadicional) {
        this.nombreadicional = nombreadicional;
    }

    public Double getPrecioadicional() {
        return precioadicional;
    }

    public void setPrecioadicional(Double precioadicional) {
        this.precioadicional = precioadicional;
    }

    public String getEstadoadicional() {
        return estadoadicional;
    }

    public void setEstadoadicional(String estadoadicional) {
        this.estadoadicional = estadoadicional;
    }

    public int getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(int idproducto) {
        this.idproducto = idproducto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AdicionalRealmFirebase(int idadicional, String nombreadicional, Double precioadicional, String estadoadicional, int idproducto, int id) {
        this.idadicional = idadicional;
        this.nombreadicional = nombreadicional;
        this.precioadicional = precioadicional;
        this.estadoadicional = estadoadicional;
        this.idproducto = idproducto;
        this.id = id;
    }

    Double precioadicional;
    String    estadoadicional;

    int idproducto;
    int id;
    public AdicionalRealmFirebase(){}



}
