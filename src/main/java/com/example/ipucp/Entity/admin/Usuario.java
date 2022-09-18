package com.example.ipucp.Entity.admin;

import javax.persistence.*;
import java.util.Arrays;

@Entity
public class Usuario {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "codigo")
    private String codigo;
    @Basic
    @Column(name = "nombre")
    private String nombre;
    @Basic
    @Column(name = "apellido")
    private String apellido;
    @Basic
    @Column(name = "contra")
    private String contra;
    @Basic
    @Column(name = "dni")
    private String dni;
    @Basic
    @Column(name = "estado")
    private byte estado;
    @Basic
    @Column(name = "foto")
    private byte[] foto;
    @Basic
    @Column(name = "idrol")
    private int idrol;
    @Basic
    @Column(name = "idcargo")
    private int idcargo;
    @Basic
    @Column(name = "icono_idicono")
    private Integer iconoIdicono;
    @Basic
    @Column(name = "ban")
    private byte ban;
    @Basic
    @Column(name = "justificacion")
    private String justificacion;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public byte getEstado() {
        return estado;
    }

    public void setEstado(byte estado) {
        this.estado = estado;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public int getIdrol() {
        return idrol;
    }

    public void setIdrol(int idrol) {
        this.idrol = idrol;
    }

    public int getIdcargo() {
        return idcargo;
    }

    public void setIdcargo(int idcargo) {
        this.idcargo = idcargo;
    }

    public Integer getIconoIdicono() {
        return iconoIdicono;
    }

    public void setIconoIdicono(Integer iconoIdicono) {
        this.iconoIdicono = iconoIdicono;
    }

    public byte getBan() {
        return ban;
    }

    public void setBan(byte ban) {
        this.ban = ban;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Usuario usuario = (Usuario) o;

        if (estado != usuario.estado) return false;
        if (idrol != usuario.idrol) return false;
        if (idcargo != usuario.idcargo) return false;
        if (ban != usuario.ban) return false;
        if (codigo != null ? !codigo.equals(usuario.codigo) : usuario.codigo != null) return false;
        if (nombre != null ? !nombre.equals(usuario.nombre) : usuario.nombre != null) return false;
        if (apellido != null ? !apellido.equals(usuario.apellido) : usuario.apellido != null) return false;
        if (contra != null ? !contra.equals(usuario.contra) : usuario.contra != null) return false;
        if (dni != null ? !dni.equals(usuario.dni) : usuario.dni != null) return false;
        if (!Arrays.equals(foto, usuario.foto)) return false;
        if (iconoIdicono != null ? !iconoIdicono.equals(usuario.iconoIdicono) : usuario.iconoIdicono != null)
            return false;
        if (justificacion != null ? !justificacion.equals(usuario.justificacion) : usuario.justificacion != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = codigo != null ? codigo.hashCode() : 0;
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (apellido != null ? apellido.hashCode() : 0);
        result = 31 * result + (contra != null ? contra.hashCode() : 0);
        result = 31 * result + (dni != null ? dni.hashCode() : 0);
        result = 31 * result + (int) estado;
        result = 31 * result + Arrays.hashCode(foto);
        result = 31 * result + idrol;
        result = 31 * result + idcargo;
        result = 31 * result + (iconoIdicono != null ? iconoIdicono.hashCode() : 0);
        result = 31 * result + (int) ban;
        result = 31 * result + (justificacion != null ? justificacion.hashCode() : 0);
        return result;
    }
}
