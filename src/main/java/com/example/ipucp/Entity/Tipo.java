package com.example.ipucp.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
    @Size(max = 45, message = "Máximo 45 caracteres")
    @NotBlank(message = "Ingrese un nombre para el tipo de incidencia")
    private String tipoIncidencia;

    @Lob
    @Column(name = "foto")
    private byte[] foto;

}