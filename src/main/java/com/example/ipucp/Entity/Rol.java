package com.example.ipucp.Entity;

import javax.persistence.*;

@Entity
public class Rol {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idrol")
    private int idrol;
    @Basic
    @Column(name = "nombre_rol")
    private String nombreRol;

    public int getIdrol() {
        return idrol;
    }

    public void setIdrol(int idrol) {
        this.idrol = idrol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rol rol = (Rol) o;

        if (idrol != rol.idrol) return false;
        if (nombreRol != null ? !nombreRol.equals(rol.nombreRol) : rol.nombreRol != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idrol;
        result = 31 * result + (nombreRol != null ? nombreRol.hashCode() : 0);
        return result;
    }
}
