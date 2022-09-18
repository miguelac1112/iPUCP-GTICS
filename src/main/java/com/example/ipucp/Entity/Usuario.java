package com.example.ipucp.Entity;

import javax.persistence.*;

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

    @Column(name = "contra", length = 45)
    private String contra;

    @Column(name = "dni", nullable = false, length = 8)
    private String dni;

    @Column(name = "estado", nullable = false)
    private Byte estado;

    @Column(name = "foto")
    private byte[] foto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idrol", nullable = false)
    private Rol idrol;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idcargo", nullable = false)
    private Cargo idcargo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "icono_idicono")
    private Icono iconoIdicono;

    @Column(name = "ban", nullable = false)
    private Byte ban;

    @Lob
    @Column(name = "justificacion")
    private String justificacion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Byte getEstado() {
        return estado;
    }

    public void setEstado(Byte estado) {
        this.estado = estado;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public Rol getIdrol() {
        return idrol;
    }

    public void setIdrol(Rol idrol) {
        this.idrol = idrol;
    }

    public Cargo getIdcargo() {
        return idcargo;
    }

    public void setIdcargo(Cargo idcargo) {
        this.idcargo = idcargo;
    }

    public Icono getIconoIdicono() {
        return iconoIdicono;
    }

    public void setIconoIdicono(Icono iconoIdicono) {
        this.iconoIdicono = iconoIdicono;
    }

    public Byte getBan() {
        return ban;
    }

    public void setBan(Byte ban) {
        this.ban = ban;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

}