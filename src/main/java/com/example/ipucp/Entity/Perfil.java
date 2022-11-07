package com.example.ipucp.Entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class Perfil implements Serializable {
    private String name;
    private String fileBase64;
}
