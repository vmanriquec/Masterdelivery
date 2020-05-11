package com.mazinger.masterdelivery.modelo;

public class Usuariocompleto {
    private String nombreusuario,
            apellidos,
            claveusuario,
            imagen,
            idfacebook,
            nombrefacebook,
            idfirebase,
            telefono,
            contrasena,
            correo,
            direccion,
    latitud,
    longitud,
    referencia;
    private int idusuariocompleto,idalmacen;


    @Override
    public String toString() {
        return "Usuariocompleto{" +
                "nombreusuario='" + nombreusuario + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", telefono='" + telefono + '\'' +
                ", correo='" + correo + '\'' +
                '}';
    }

    public String getNombreusuario() {
        return nombreusuario;
    }

    public void setNombreusuario(String nombreusuario) {
        this.nombreusuario = nombreusuario;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getClaveusuario() {
        return claveusuario;
    }

    public void setClaveusuario(String claveusuario) {
        this.claveusuario = claveusuario;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
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

    public String getIdfirebase() {
        return idfirebase;
    }

    public void setIdfirebase(String idfirebase) {
        this.idfirebase = idfirebase;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getIdusuariocompleto() {
        return idusuariocompleto;
    }

    public void setIdusuariocompleto(int idusuariocompleto) {
        this.idusuariocompleto = idusuariocompleto;
    }

    public int getIdalmacen() {
        return idalmacen;
    }

    public void setIdalmacen(int idalmacen) {
        this.idalmacen = idalmacen;
    }

    public Usuariocompleto(String nombreusuario, String apellidos, String claveusuario, String imagen, String idfacebook, String nombrefacebook, String idfirebase, String telefono, String contrasena, String correo, String direccion, int idusuariocompleto, int idalmacen,String latitud,String longitud,String referencia) {
        this.nombreusuario = nombreusuario;
        this.apellidos = apellidos;
        this.claveusuario = claveusuario;
        this.imagen = imagen;
        this.idfacebook = idfacebook;
        this.nombrefacebook = nombrefacebook;
        this.idfirebase = idfirebase;
        this.telefono = telefono;
        this.contrasena = contrasena;
        this.correo = correo;
        this.direccion = direccion;
        this.idusuariocompleto = idusuariocompleto;
        this.idalmacen = idalmacen;
        this.longitud=longitud;
        this.latitud=latitud;
        this.referencia=referencia;
    }

    public Usuariocompleto(){



    }
}
