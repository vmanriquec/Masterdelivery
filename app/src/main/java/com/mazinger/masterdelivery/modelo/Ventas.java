package com.mazinger.masterdelivery.modelo;

public class Ventas {
    public int getIdmesa() {
        return idmesa;
    }

    public void setIdmesa(int idmesa) {
        this.idmesa = idmesa;
    }

    public Double getTotalpedido() {
        return totalpedido;
    }

    public void setTotalpedido(Double totalpedido) {
        this.totalpedido = totalpedido;
    }

    private int idmesa;
    private Double totalpedido;




    public Ventas(int idmesa,Double totalpedido)
    {
        super();
        this.idmesa=idmesa;
        this.totalpedido=totalpedido;


    }



}

