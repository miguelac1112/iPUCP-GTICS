package com.example.ipucp.Entity.admin;

import javax.persistence.*;

@Entity
@Table(name = "cargo")
public class Cargo {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idcargo")
    private int idcargo;
    @Basic
    @Column(name = "nombre_cargo")
    private String nombreCargo;

    public int getIdcargo() {
        return idcargo;
    }

    public void setIdcargo(int idcargo) {
        this.idcargo = idcargo;
    }

    public String getNombreCargo() {
        return nombreCargo;
    }

    public void setNombreCargo(String nombreCargo) {
        this.nombreCargo = nombreCargo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cargo cargo = (Cargo) o;

        if (idcargo != cargo.idcargo) return false;
        if (nombreCargo != null ? !nombreCargo.equals(cargo.nombreCargo) : cargo.nombreCargo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idcargo;
        result = 31 * result + (nombreCargo != null ? nombreCargo.hashCode() : 0);
        return result;
    }
}
