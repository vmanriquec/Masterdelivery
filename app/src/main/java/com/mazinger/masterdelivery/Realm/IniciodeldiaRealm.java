package com.mazinger.masterdelivery.Realm;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class IniciodeldiaRealm extends RealmObject {
    @PrimaryKey
    private int idiniciodeldia;

    public int getIdiniciodeldia() {
        return idiniciodeldia;
    }

    public void setIdiniciodeldia(int idiniciodeldia) {
        this.idiniciodeldia = idiniciodeldia;
    }

    public int getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    public int getIdalmacen() {
        return idalmacen;
    }

    public void setIdalmacen(int idalmacen) {
        this.idalmacen = idalmacen;
    }

    public int getIdvalidadorentrega() {
        return idvalidadorentrega;
    }

    public void setIdvalidadorentrega(int idvalidadorentrega) {
        this.idvalidadorentrega = idvalidadorentrega;
    }

    public int getIdvalidadorrecibe() {
        return idvalidadorrecibe;
    }

    public void setIdvalidadorrecibe(int idvalidadorrecibe) {
        this.idvalidadorrecibe = idvalidadorrecibe;
    }

    public Date getFechadeentrega() {
        return fechadeentrega;
    }

    public void setFechadeentrega(Date fechadeentrega) {
        this.fechadeentrega = fechadeentrega;
    }

    public int getNumerodedocumento() {
        return numerodedocumento;
    }

    public void setNumerodedocumento(int numerodedocumento) {
        this.numerodedocumento = numerodedocumento;
    }

    public int getIdestados() {
        return idestados;
    }

    public void setIdestados(int idestados) {
        this.idestados = idestados;
    }

    public int getIdtipomovimiento() {
        return idtipomovimiento;
    }

    public void setIdtipomovimiento(int idtipomovimiento) {
        this.idtipomovimiento = idtipomovimiento;
    }

    public Date getFechainicial() {
        return fechainicial;
    }

    public void setFechainicial(Date fechainicial) {
        this.fechainicial = fechainicial;
    }

    public RealmList<Detallepedidorealm> getDetallepedidorealms() {
        return detallepedidorealms;
    }

    public void setDetallepedidorealms(RealmList<Detallepedidorealm> detallepedidorealms) {
        this.detallepedidorealms = detallepedidorealms;
    }

    private int idusuario;
    private int idalmacen;
    private int idvalidadorentrega;
    private int idvalidadorrecibe;
    private Date fechadeentrega;
    private int numerodedocumento;
    private int idestados;
    private int  idtipomovimiento;
    private Date fechainicial;

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    private Double total;
    private RealmList<Detallepedidorealm> detallepedidorealms;

}
