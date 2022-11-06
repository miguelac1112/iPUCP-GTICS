package com.example.ipucp.Dto;

import com.example.ipucp.Entity.Cargo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UsuarioDto {
    @Pattern(regexp="^[0-9]{8}$",message="Ingresar 8 dígitos numericos")
    private String codigo;

    @Pattern(regexp="^[a-zA-Z\\s]*$",message="Solo letras")
    @NotBlank(message = "Ingrese nombres")
    private String nombre;

    @Pattern(regexp="^[a-zA-Z\\s]*$",message="Solo letras")
    @NotBlank(message = "Ingrese apellidos")
    private String apellido;

    @Column(name = "dni", nullable = false, length = 8)
    @Pattern(regexp="^[0-9]{8}$",message="Ingresar 8 dígitos")
    private String dni;

    @Size(max = 45, message = "Máximo 45 caracteres")
    @Pattern(regexp="^[\\w-\\.]+@pucp.edu.pe$",message="Ingrese correo en formato institucional")
    private String correo;

    private Cargo cargo;
}
