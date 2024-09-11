package com.example.programacion;

public class clasemostrarseries
{
    String nombre;
    String imagen;
    String nota;

    public clasemostrarseries(String nombre, String imagen, String nota) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.nota = nota;
    }

    public clasemostrarseries(String nombre, String imagen) {
        this.nombre = nombre;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }
}
