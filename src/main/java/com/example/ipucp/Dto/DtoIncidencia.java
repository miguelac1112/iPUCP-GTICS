package com.example.ipucp.Dto;

import com.example.ipucp.Entity.Inicidencia;

public class DtoIncidencia {
    private int id;
    private String nombre;
    private String idtipo;
    private String ubicacion;
    private String codigo;
    private String fecha;
    private String destacado;

    public DtoIncidencia(){

    }

    public DtoIncidencia(Inicidencia inicidencia, int i){
        this.id = i;
        this.nombre = " "+inicidencia.getNombre()+" ";
        this.idtipo = " "+inicidencia.getIdtipo().getTipoIncidencia()+" ";
        this.ubicacion = " "+inicidencia.getUbicacion().getNombre()+" ";
        this.codigo = " "+inicidencia.getCodigo().getNombre()+" "+inicidencia.getCodigo().getApellido()+" ";
        this.fecha = " "+inicidencia.getFecha().toString().split(String.valueOf('T'))[0]+" ";
        this.destacado = " "+inicidencia.getDestacado()+" ";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdtipo() {
        return idtipo;
    }

    public void setIdtipo(String idtipo) {
        this.idtipo = idtipo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDestacado() {
        return destacado;
    }

    public void setDestacado(String destacado) {
        this.destacado = destacado;
    }
}
