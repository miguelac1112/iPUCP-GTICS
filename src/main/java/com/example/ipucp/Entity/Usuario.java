package com.example.ipucp.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @Column(name = "codigo", nullable = false, length = 8)
    @Pattern(regexp="^[0-9]{8}$",message="Ingresar 8 dígitos")
    private String id;

    @Column(name = "nombre", nullable = false, length = 45)
    @Size(max = 45, message = "Máximo 45 caracteres")
    @Pattern(regexp="^[a-zA-Z\\s]*$",message="Solo letras")
    @NotBlank(message = "Ingrese un nombre")
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 45)
    @Size(max = 45, message = "Máximo 45 caracteres")
    @Pattern(regexp="^[a-zA-Z\\s]*$",message="Solo letras")
    @NotBlank(message = "Ingrese un apellido")
    private String apellido;

    @Column(name = "correo", nullable = false, length = 45)
    @Size(max = 45, message = "Máximo 45 caracteres")
    @Pattern(regexp="^[\\w-\\.]+@pucp.edu.pe$",message="Usar formato de correo institucional")
    private String correo;

    @Column(name = "celular", nullable = false, length = 45)
    @Pattern(regexp="^[0-9]{9}$",message="Ingresar 9 dígitos")
    private String celular;

    @Column(name = "contra", length = 45)
    private String contra;

    @Column(name = "dni", nullable = false, length = 8)
    @Pattern(regexp="^[0-9]{8}$",message="Ingresar 8 dígitos")
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