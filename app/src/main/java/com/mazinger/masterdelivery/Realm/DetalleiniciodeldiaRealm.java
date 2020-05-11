package com.mazinger.masterdelivery.Realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DetalleiniciodeldiaRealm extends RealmObject {
    @PrimaryKey
    private int iddetalleiniciodeldia;
    private int idiniciodeldia;
    private int idproducto;
    private int cantidad;
    private Double precventa;
    private String nombreproducto;
    private int idalmacen;
    private Double subtotal;
    private RealmList<ProductoRealm> productoRealms;
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

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public RealmList<ProductoRealm> getProductoRealms() {
        return productoRealms;
    }

    public void setProductoRealms(RealmList<ProductoRealm> productoRealms) {
        this.productoRealms = productoRealms;
    }


}
