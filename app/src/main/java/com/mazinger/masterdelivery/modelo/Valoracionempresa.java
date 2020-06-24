package com.mazinger.masterdelivery.modelo;

public class Valoracionempresa {

    int idvaloracionclienteproducto;
    int idempresa;

    public int getIdvaloracionclienteproducto() {
        return idvaloracionclienteproducto;
    }

    public void setIdvaloracionclienteproducto(int idvaloracionclienteproducto) {
        this.idvaloracionclienteproducto = idvaloracionclienteproducto;
    }

    public int getIdempresa() {
        return idempresa;
    }

    public void setIdempresa(int idempresa) {
        this.idempresa = idempresa;
    }

    public int getNivelvaloracion() {
        return nivelvaloracion;
    }

    public void setNivelvaloracion(int nivelvaloracion) {
        this.nivelvaloracion = nivelvaloracion;
    }

    public String getIdfirebaseusuario() {
        return idfirebaseusuario;
    }

    public void setIdfirebaseusuario(String idfirebaseusuario) {
        this.idfirebaseusuario = idfirebaseusuario;
    }

    int nivelvaloracion;

    public Valoracionempresa(int idvaloracionclienteproducto, int idempresa, int nivelvaloracion, String idfirebaseusuario) {
        this.idvaloracionclienteproducto = idvaloracionclienteproducto;
        this.idempresa = idempresa;
        this.nivelvaloracion = nivelvaloracion;
        this.idfirebaseusuario = idfirebaseusuario;
    }

    String idfirebaseusuario;
}
