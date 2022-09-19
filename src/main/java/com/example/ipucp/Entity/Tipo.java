package com.example.ipucp.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "tipo")
public class Tipo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtipo", nullable = false)
    private Integer id;

    @Column(name = "tipo_incidencia", nullable = false, length = 45)
    private String tipoIncidencia;

    @Lob
    @Column(name = "foto")
    private byte[] foto;

}