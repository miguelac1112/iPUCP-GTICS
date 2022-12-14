package com.example.ipucp.Entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "icono")
public class Icono implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idicono", nullable = false)
    private Integer id;

    @Column(name = "imagen")
    private byte[] imagen ;

    @Column(name = "nombre", nullable = false, length = 45)
    private String nombre;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}