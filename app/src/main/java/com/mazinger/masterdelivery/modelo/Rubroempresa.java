package com.mazinger.masterdelivery.modelo;

public class Rubroempresa {

    int idrubroempresa;
    String nombrerubro;
    String estadorubro;
String imagenrubro;

    public String getImagenrubro() {
        return imagenrubro;
    }

    public void setImagenrubro(String imagenrubro) {
        this.imagenrubro = imagenrubro;
    }

    public Rubroempresa(int idrubroempresa, String nombrerubro, String estadorubro, String imagenrubro) {
        this.idrubroempresa = idrubroempresa;
        this.nombrerubro = nombrerubro;
        this.estadorubro = estadorubro;
        this.imagenrubro = imagenrubro;
    }

    public int getIdrubroempresa() {
        return idrubroempresa;
    }

    public void setIdrubroempresa(int idrubroempresa) {
        this.idrubroempresa = idrubroempresa;
    }

    public String getNombrerubro() {
        return nombrerubro;
    }

    public void setNombrerubro(String nombrerubro) {
        this.nombrerubro = nombrerubro;
    }

    public String getEstadorubro() {
        return estadorubro;
    }

    public void setEstadorubro(String estadorubro) {
        this.estadorubro = estadorubro;
    }
}
