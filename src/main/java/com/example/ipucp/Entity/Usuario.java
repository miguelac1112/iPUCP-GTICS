package com.example.ipucp.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @Column(name = "codigo", nullable = false, length = 8)
    private String id;

    @Column(name = "nombre", nullable = false, length = 45)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 45)
    private String apellido;

    @Column(name = "correo", nullable = false, length = 45)
    private String correo;

    @Column(name = "celular", nullable = false, length = 45)
    private String celular;

    @Column(name = "contra", length = 45)
    private String contra;

    @Column(name = "dni", nullable = false, length = 8)
    private String dni;

    @Column(name = "estado", nullable = false)
    private Byte estado;

    @Lob
    @Column(name = "foto")
    private byte[] foto;

    @ManyToOne
    @JoinColumn(name = "idrol", nullable = false)
    private Rol rol;

    @ManyToOne
    @JoinColumn(name = "idcargo", nullable = false)
    private Cargo cargo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "icono_idicono")
    private Icono icono;

    @Column(name = "strikes", nullable = false)
    private Byte strikes;

    @Lob
    @Column(name = "justificacion")
    private String justificacion;

    @Column(name = "ban", nullable = false)
    private Byte ban;
}