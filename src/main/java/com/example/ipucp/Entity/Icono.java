package com.example.ipucp.Entity;

import javax.persistence.*;
import java.util.Arrays;

@Entity
public class Icono {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idicono")
    private int idicono;
    @Basic
    @Column(name = "imagen")
    private byte[] imagen;
    @Basic
    @Column(name = "nombre")
    private String nombre;

    public int getIdicono() {
        return idicono;
    }

    public void setIdicono(int idicono) {
        this.idicono = idicono;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Icono icono = (Icono) o;

        if (idicono != icono.idicono) return false;
        if (!Arrays.equals(imagen, icono.imagen)) return false;
        if (nombre != null ? !nombre.equals(icono.nombre) : icono.nombre != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idicono;
        result = 31 * result + Arrays.hashCode(imagen);
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        return result;
    }
}
