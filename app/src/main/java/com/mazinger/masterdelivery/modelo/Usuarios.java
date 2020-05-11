
package com.mazinger.masterdelivery.modelo;

/**
 * Created by Perseo on 04/08/2014.
 */
public class Usuarios {
    private String nombreusuario,claveusuario,imagen,idfacebook,nombrefacebook;
private int idusuario,idalmacen;

    public Usuarios(int idusuario, String nombreusuario, String claveusuario, int idalmacen , String imagen,String idfacebook,String nombrefacebook)
    {
        super();
        this.idusuario=idusuario;
        this.nombreusuario=nombreusuario;
        this.claveusuario=claveusuario;
        this.idalmacen=idalmacen;
        this.imagen=imagen;
        this.idfacebook=idfacebook;
        this.nombrefacebook=nombrefacebook;
    }

    public int getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }





    public String getNombreusuario() {
        return nombreusuario;
    }

    public void setNombreusuario(String nombreusuario) {
        this.nombreusuario = nombreusuario;
    }

    public String getClaveusuario() {
        return claveusuario;
    }

    public void setClaveusuario(String claveusuario) {
        this.claveusuario = claveusuario;
    }


    public int getIdalmacen() {
        return idalmacen;
    }

    public void setIdalmacen(int idalmacen) {
        this.idalmacen = idalmacen;
    }


    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString(){
        return this.nombreusuario;
    }

    public String getIdfacebook() {
        return idfacebook;
    }

    public void setIdfacebook(String idfacebook) {
        this.idfacebook = idfacebook;
    }

    public String getNombrefacebook() {
        return nombrefacebook;
    }

    public void setNombrefacebook(String nombrefacebook) {
        this.nombrefacebook = nombrefacebook;
    }
}
