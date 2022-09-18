package com.example.ipucp.Entity;

import javax.persistence.*;

@Entity
public class Tipo {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idtipo")
    private int idtipo;
    @Basic
    @Column(name = "tipo_incidencia")
    private String tipoIncidencia;

    public int getIdtipo() {
        return idtipo;
    }

    public void setIdtipo(int idtipo) {
        this.idtipo = idtipo;
    }

    public String getTipoIncidencia() {
        return tipoIncidencia;
    }

    public void setTipoIncidencia(String tipoIncidencia) {
        this.tipoIncidencia = tipoIncidencia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tipo tipo = (Tipo) o;

        if (idtipo != tipo.idtipo) return false;
        if (tipoIncidencia != null ? !tipoIncidencia.equals(tipo.tipoIncidencia) : tipo.tipoIncidencia != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idtipo;
        result = 31 * result + (tipoIncidencia != null ? tipoIncidencia.hashCode() : 0);
        return result;
    }
}
