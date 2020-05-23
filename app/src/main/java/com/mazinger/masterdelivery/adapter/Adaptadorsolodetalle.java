package com.mazinger.masterdelivery.adapter;

public class Adaptadorsolodetalle {
    int iddetallepedido;
    int idproducto;
    int cantidad;
    Double precventa;

    public int getIddetallepedido() {
        return iddetallepedido;
    }

    public void setIddetallepedido(int iddetallepedido) {
        this.iddetallepedido = iddetallepedido;
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

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public int getIdpedido() {
        return idpedido;
    }

    public void setIdpedido(int idpedido) {
        this.idpedido = idpedido;
    }

    public int getIdalmacen() {
        return idalmacen;
    }

    public void setIdalmacen(int idalmacen) {
        this.idalmacen = idalmacen;
    }

    public String getAdicionales() {
        return adicionales;
    }

    public void setAdicionales(String adicionales) {
        this.adicionales = adicionales;
    }

    public String getCremas() {
        return cremas;
    }

    public void setCremas(String cremas) {
        this.cremas = cremas;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getOjo() {
        return ojo;
    }

    public void setOjo(int ojo) {
        this.ojo = ojo;
    }

    public String getImagenreal() {
        return imagenreal;
    }

    public void setImagenreal(String imagenreal) {
        this.imagenreal = imagenreal;
    }

    public String getComentarioacocina() {
        return comentarioacocina;
    }

    public void setComentarioacocina(String comentarioacocina) {
        this.comentarioacocina = comentarioacocina;
    }

    public String getNombreproductorealm() {
        return nombreproductorealm;
    }

    public void setNombreproductorealm(String nombreproductorealm) {
        this.nombreproductorealm = nombreproductorealm;
    }

    public int getIdalmacenreal() {
        return idalmacenreal;
    }

    public void setIdalmacenreal(int idalmacenreal) {
        this.idalmacenreal = idalmacenreal;
    }

    public Adaptadorsolodetalle(int iddetallepedido, int idproducto, int cantidad, Double precventa, Double subtotal, int idpedido, int idalmacen, String adicionales, String cremas, String comentario, int ojo, String imagenreal, String comentarioacocina, String nombreproductorealm, int idalmacenreal) {
        this.iddetallepedido = iddetallepedido;
        this.idproducto = idproducto;
        this.cantidad = cantidad;
        this.precventa = precventa;
        this.subtotal = subtotal;
        this.idpedido = idpedido;
        this.idalmacen = idalmacen;
        this.adicionales = adicionales;
        this.cremas = cremas;
        this.comentario = comentario;
        this.ojo = ojo;
        this.imagenreal = imagenreal;
        this.comentarioacocina = comentarioacocina;
        this.nombreproductorealm = nombreproductorealm;
        this.idalmacenreal = idalmacenreal;
    }

    Double subtotal;
    int idpedido;
    int idalmacen;
    String adicionales;
    String cremas;
    String comentario;
    int ojo;
    String imagenreal;
    String comentarioacocina;
    String nombreproductorealm;
    int idalmacenreal;
}
