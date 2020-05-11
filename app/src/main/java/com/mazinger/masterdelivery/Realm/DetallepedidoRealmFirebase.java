package com.mazinger.masterdelivery.Realm;

public class DetallepedidoRealmFirebase {

    public int id;
    public int ojo;

    public DetallepedidoRealmFirebase(int id, int ojo, String subtotal, String idpedido, int cantidadrealm, Double precventarealm, String nombreproductorealm, String imagenrealm, int idalmacenrealm, int idproductorealm, String comentarioacocina) {
        this.id = id;
        this.ojo = ojo;
        this.subtotal = subtotal;
        this.idpedido = idpedido;
        this.cantidadrealm = cantidadrealm;
        this.precventarealm = precventarealm;
        this.nombreproductorealm = nombreproductorealm;
        this.imagenrealm = imagenrealm;
        this.idalmacenrealm = idalmacenrealm;
        this.idproductorealm = idproductorealm;
        this.comentarioacocina = comentarioacocina;
    }

    private String subtotal;
    private String idpedido;
    private int cantidadrealm;
    private Double precventarealm;
    private String nombreproductorealm;
    private String imagenrealm;
    private int idalmacenrealm;
    private int idproductorealm;
    private String comentarioacocina;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOjo() {
        return ojo;
    }

    public void setOjo(int ojo) {
        this.ojo = ojo;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getIdpedido() {
        return idpedido;
    }

    public void setIdpedido(String idpedido) {
        this.idpedido = idpedido;
    }

    public int getCantidadrealm() {
        return cantidadrealm;
    }

    public void setCantidadrealm(int cantidadrealm) {
        this.cantidadrealm = cantidadrealm;
    }

    public Double getPrecventarealm() {
        return precventarealm;
    }

    public void setPrecventarealm(Double precventarealm) {
        this.precventarealm = precventarealm;
    }

    public String getNombreproductorealm() {
        return nombreproductorealm;
    }

    public void setNombreproductorealm(String nombreproductorealm) {
        this.nombreproductorealm = nombreproductorealm;
    }

    public String getImagenrealm() {
        return imagenrealm;
    }

    public void setImagenrealm(String imagenrealm) {
        this.imagenrealm = imagenrealm;
    }

    public int getIdalmacenrealm() {
        return idalmacenrealm;
    }

    public void setIdalmacenrealm(int idalmacenrealm) {
        this.idalmacenrealm = idalmacenrealm;
    }

    public int getIdproductorealm() {
        return idproductorealm;
    }

    public void setIdproductorealm(int idproductorealm) {
        this.idproductorealm = idproductorealm;
    }

    public String getComentarioacocina() {
        return comentarioacocina;
    }

    public void setComentarioacocina(String comentarioacocina) {
        this.comentarioacocina = comentarioacocina;
    }
    public DetallepedidoRealmFirebase(){



    }



}
