package com.mazinger.masterdelivery.modelo;

public class Tipomovimiento {
    private int idtipomovimiento;
    private String nombre;

    public Tipomovimiento(int idtipomovimiento,  String nombre )
    {
        super();
        this.idtipomovimiento=idtipomovimiento;
        this.nombre=nombre;

    }



    public int getIdtipomovimiento() {
        return idtipomovimiento;
    }

    public void setIdtipomovimiento(int idtipomovimiento) {
        this.idtipomovimiento = idtipomovimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString(){
        return  String.valueOf(nombre);
    }

}
