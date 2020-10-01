package com.mazinger.masterdelivery.modelo;

public class Usuariodirecciones {
    public Integer getIdusuariodireccione() {
        return idusuariodireccione;
    }

    public void setIdusuariodireccione(Integer idusuariodireccione) {
        this.idusuariodireccione = idusuariodireccione;
    }

    public String getIdfirebase() {
        return idfirebase;
    }

    public void setIdfirebase(String idfirebase) {
        this.idfirebase = idfirebase;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    private Integer idusuariodireccione;

    public Usuariodirecciones(Integer idusuariodireccione, String idfirebase, String direccion, String longitud, String latitud, String referencia) {
        this.idusuariodireccione = idusuariodireccione;
        this.idfirebase = idfirebase;
        this.direccion = direccion;
        this.longitud = longitud;
        this.latitud = latitud;
        this.referencia = referencia;
    }

    private String idfirebase,direccion,longitud,latitud,referencia;
    @Override
    public String toString(){return String.valueOf(idusuariodireccione)+" - "+ String.valueOf(direccion);
    }
}
