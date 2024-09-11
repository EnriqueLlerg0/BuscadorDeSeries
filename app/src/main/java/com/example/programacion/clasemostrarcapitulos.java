package com.example.programacion;

public class clasemostrarcapitulos
{
    String Nombre_serie;
    String Titulo;
    String Temporada;
    String Num_temp;
    String Resumen;

    public clasemostrarcapitulos(String nombre_serie, String titulo, String temporada, String num_temp, String resumen) {
        Nombre_serie = nombre_serie;
        Titulo = titulo;
        Temporada = temporada;
        Num_temp = num_temp;
        Resumen = resumen;
    }

    public String getNombre_serie() {
        return Nombre_serie;
    }

    public void setNombre_serie(String nombre_serie) {
        Nombre_serie = nombre_serie;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getTemporada() {
        return Temporada;
    }

    public void setTemporada(String temporada) {
        Temporada = temporada;
    }

    public String getNum_temp() {
        return Num_temp;
    }

    public void setNum_temp(String num_temp) {
        Num_temp = num_temp;
    }

    public String getResumen() {
        return Resumen;
    }

    public void setResumen(String resumen) {
        Resumen = resumen;
    }
}
