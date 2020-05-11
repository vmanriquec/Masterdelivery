package com.mazinger.masterdelivery.modelo;

public class Detalleiniciodeldia {
    private int iddetalleiniciodeldia;
    private int idiniciodeldia;
    private int idproducto;
    private int cantidad;
    private Double precventa;
    private String nombreproducto;
    private int idalmacen;

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    private Double subtotal;

    public int getIddetalleiniciodeldia() {
        return iddetalleiniciodeldia;
    }

    public void setIddetalleiniciodeldia(int iddetalleiniciodeldia) {
        this.iddetalleiniciodeldia = iddetalleiniciodeldia;
    }

    public int getIdiniciodeldia() {
        return idiniciodeldia;
    }

    public void setIdiniciodeldia(int idiniciodeldia) {
        this.idiniciodeldia = idiniciodeldia;
    }

    public int getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(int idproducto) {
        this.idproducto = idproducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecventa() {
        return precventa;
    }

    public void setPrecventa(Double precventa) {
        this.precventa = precventa;
    }

    public String getNombreproducto() {
        return nombreproducto;
    }

    public void setNombreproducto(String nombreproducto) {
        this.nombreproducto = nombreproducto;
    }

    public int getIdalmacen() {
        return idalmacen;
    }

    public void setIdalmacen(int idalmacen) {
        this.idalmacen = idalmacen;
    }


}
