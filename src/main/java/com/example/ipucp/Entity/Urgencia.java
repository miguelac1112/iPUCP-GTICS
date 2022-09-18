package com.example.ipucp.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "urgencia")
public class Urgencia {
    @Id
    @Column(name = "idurgencia", nullable = false)
    private Integer id;

    @Column(name = "tipo_urgencia", nullable = false, length = 45)
    private String tipoUrgencia;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipoUrgencia() {
        return tipoUrgencia;
    }

    public void setTipoUrgencia(String tipoUrgencia) {
        this.tipoUrgencia = tipoUrgencia;
    }

}