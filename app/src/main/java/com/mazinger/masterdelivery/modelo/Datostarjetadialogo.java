package com.mazinger.masterdelivery.modelo;

public class Datostarjetadialogo {
    int iddatosdetarjetas;

    public Datostarjetadialogo(int iddatosdetarjetas, int cantidad, String producto, Double total) {
        this.iddatosdetarjetas = iddatosdetarjetas;
        this.cantidad = cantidad;
        this.producto = producto;
        this.total = total;
    }

    public int getIddatosdetarjetas() {
        return iddatosdetarjetas;
    }

    public void setIddatosdetarjetas(int iddatosdetarjetas) {
        this.iddatosdetarjetas = iddatosdetarjetas;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    int cantidad;

    @Override
    public String toString() {
        return "Datostarjetadialogo{" +
                "producto='" + producto + '\'' +
                '}';
    }

    String producto;
    Double total;

}
