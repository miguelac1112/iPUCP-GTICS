package com.example.ipucp.Entity;

import javax.persistence.*;

@Entity
public class Urgencia {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idurgencia")
    private int idurgencia;
    @Basic
    @Column(name = "tipo_urgencia")
    private String tipoUrgencia;

    public int getIdurgencia() {
        return idurgencia;
    }

    public void setIdurgencia(int idurgencia) {
        this.idurgencia = idurgencia;
    }

    public String getTipoUrgencia() {
        return tipoUrgencia;
    }

    public void setTipoUrgencia(String tipoUrgencia) {
        this.tipoUrgencia = tipoUrgencia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Urgencia urgencia = (Urgencia) o;

        if (idurgencia != urgencia.idurgencia) return false;
        if (tipoUrgencia != null ? !tipoUrgencia.equals(urgencia.tipoUrgencia) : urgencia.tipoUrgencia != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idurgencia;
        result = 31 * result + (tipoUrgencia != null ? tipoUrgencia.hashCode() : 0);
        return result;
    }
}
