package com.mazinger.masterdelivery.modelo;

public class Valedescuento {
    String nombredescuento,montodescuento,montocosto,nombrecosto;

    public String getNombredescuento() {
        return nombredescuento;
    }

    public void setNombredescuento(String nombredescuento) {
        this.nombredescuento = nombredescuento;
    }

    @Override
    public String toString() {
        return "Valedescuento{" +
                "nombredescuento='" + nombredescuento + '\'' +
                ", montodescuento='" + montodescuento + '\'' +
                ", montocosto='" + montocosto + '\'' +
                ", nombrecosto='" + nombrecosto + '\'' +
                '}';
    }

    public String getMontodescuento() {
        return montodescuento;
    }

    public void setMontodescuento(String montodescuento) {
        this.montodescuento = montodescuento;
    }

    public String getMontocosto() {
        return montocosto;
    }

    public void setMontocosto(String montocosto) {
        this.montocosto = montocosto;
    }

    public String getNombrecosto() {
        return nombrecosto;
    }

    public void setNombrecosto(String nombrecosto) {
        this.nombrecosto = nombrecosto;
    }

    public Valedescuento(String nombredescuento, String montodescuento, String montocosto, String nombrecosto) {
        this.nombredescuento = nombredescuento;
        this.montodescuento = montodescuento;

        this.montocosto = montocosto;
        this.nombrecosto = nombrecosto;
    }
    public Valedescuento(){
    }

}
