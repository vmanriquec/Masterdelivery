package com.mazinger.masterdelivery.modelo;

public class Valoracionporcentajeempresa {
    String porcentaje;

    public String getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(String porcentaje) {
        this.porcentaje = porcentaje;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public Valoracionporcentajeempresa(String porcentaje, String cantidad) {
        this.porcentaje = porcentaje;
        this.cantidad = cantidad;
    }

    String cantidad;

}
