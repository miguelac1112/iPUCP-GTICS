package com.example.ipucp.Entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "cargo")
public class Cargo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcargo", nullable = false)
    private Integer id;

    @Column(name = "nombre_cargo", nullable = false, length = 45)
    private String nombreCargo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreCargo() {
        return nombreCargo;
    }

    public void setNombreCargo(String nombreCargo) {
        this.nombreCargo = nombreCargo;
    }

}