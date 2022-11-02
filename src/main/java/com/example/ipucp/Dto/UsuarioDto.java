package com.example.ipucp.Dto;

import com.example.ipucp.Entity.Cargo;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
public class UsuarioDto {
    private String codigo;
    private String nombre;
    private String apellido;
    private String dni;
    private String correo;
    private Cargo cargo;
}
