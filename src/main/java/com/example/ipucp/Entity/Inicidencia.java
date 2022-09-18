package com.example.ipucp.Entity;

import javax.persistence.*;
import java.util.Arrays;

@Entity
public class Inicidencia {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idinicidencia")
    private int idinicidencia;
    @Basic
    @Column(name = "descripcion")
    private String descripcion;
    @Basic
    @Column(name = "ubicacion")
    private String ubicacion;
    @Basic
    @Column(name = "localizacion")
    private String localizacion;
    @Basic
    @Column(name = "nombre")
    private String nombre;
    @Basic
    @Column(name = "codigo")
    private String codigo;
    @Basic
    @Column(name = "foto")
    private byte[] foto;
    @Basic
    @Column(name = "idurgencia")
    private int idurgencia;
    @Basic
    @Column(name = "idtipo")
    private int idtipo;

    public int getIdinicidencia() {
        return idinicidencia;
    }

    public void setIdinicidencia(int idinicidencia) {
        this.idinicidencia = idinicidencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public int getIdurgencia() {
        return idurgencia;
    }

    public void setIdurgencia(int idurgencia) {
        this.idurgencia = idurgencia;
    }

    public int getIdtipo() {
        return idtipo;
    }

    public void setIdtipo(int idtipo) {
        this.idtipo = idtipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Inicidencia that = (Inicidencia) o;

        if (idinicidencia != that.idinicidencia) return false;
        if (idurgencia != that.idurgencia) return false;
        if (idtipo != that.idtipo) return false;
        if (descripcion != null ? !descripcion.equals(that.descripcion) : that.descripcion != null) return false;
        if (ubicacion != null ? !ubicacion.equals(that.ubicacion) : that.ubicacion != null) return false;
        if (localizacion != null ? !localizacion.equals(that.localizacion) : that.localizacion != null) return false;
        if (nombre != null ? !nombre.equals(that.nombre) : that.nombre != null) return false;
        if (codigo != null ? !codigo.equals(that.codigo) : that.codigo != null) return false;
        if (!Arrays.equals(foto, that.foto)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idinicidencia;
        result = 31 * result + (descripcion != null ? descripcion.hashCode() : 0);
        result = 31 * result + (ubicacion != null ? ubicacion.hashCode() : 0);
        result = 31 * result + (localizacion != null ? localizacion.hashCode() : 0);
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (codigo != null ? codigo.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(foto);
        result = 31 * result + idurgencia;
        result = 31 * result + idtipo;
        return result;
    }
}
