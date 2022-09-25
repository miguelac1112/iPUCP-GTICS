package com.example.ipucp.Entity;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "inicidencia")
public class Inicidencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idinicidencia", nullable = false)
    private Integer id;

    @Column(name = "descripcion", nullable = false, length = 45)
    private String descripcion;

    @Column(name = "ubicacion", nullable = false, length = 45)
    private String ubicacion;

    @Column(name = "localizacion", nullable = false, length = 45)
    private String localizacion;

    @Column(name = "nombre", nullable = false, length = 45)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "codigo", nullable = false)
    private Usuario codigo;

    @Column(name = "foto")
    private byte[] foto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idurgencia", nullable = false)
    private Urgencia idurgencia;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idtipo", nullable = false)
    private Tipo idtipo;

    @Lob
    @Column(name = "comentario")
    private String comentario;

    @Column(name = "estado", nullable = false)
    private Byte estado;

    @Column(name = "destacado", nullable = false)
    private int destacado;

    @Column(name = "fecha")
    private Instant fecha;

    @Column(name = "em_medica")
    private Byte emMedica;

    @Lob
    @Column(name = "comentario_usuario")
    private String comentarioUsuario;

    public int getDestacado() {
        return destacado;
    }

    public void setDestacado(int destacado) {
        this.destacado = destacado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Usuario getCodigo() {
        return codigo;
    }

    public void setCodigo(Usuario codigo) {
        this.codigo = codigo;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public Urgencia getIdurgencia() {
        return idurgencia;
    }

    public void setIdurgencia(Urgencia idurgencia) {
        this.idurgencia = idurgencia;
    }

    public Tipo getIdtipo() {
        return idtipo;
    }

    public void setIdtipo(Tipo idtipo) {
        this.idtipo = idtipo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Byte getEstado() {
        return estado;
    }

    public void setEstado(Byte estado) {
        this.estado = estado;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public Byte getEmMedica() {
        return emMedica;
    }

    public void setEmMedica(Byte emMedica) {
        this.emMedica = emMedica;
    }

    public String getComentarioUsuario() {
        return comentarioUsuario;
    }

    public void setComentarioUsuario(String comentarioUsuario) {
        this.comentarioUsuario = comentarioUsuario;
    }
}