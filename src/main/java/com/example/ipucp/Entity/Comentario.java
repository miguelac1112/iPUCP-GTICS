package com.example.ipucp.Entity;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "comentarios")
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "comentario", nullable = false)
    private String comentario;

    @Column(name = "fecha", nullable = false)
    private Instant fecha;

    @Column(name = "cargo", nullable = false)
    private Byte cargo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idinicidencia", nullable = false)
    private Inicidencia idinicidencia;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public Byte getCargo() {
        return cargo;
    }

    public void setCargo(Byte cargo) {
        this.cargo = cargo;
    }

    public Inicidencia getIdinicidencia() {
        return idinicidencia;
    }

    public void setIdinicidencia(Inicidencia idinicidencia) {
        this.idinicidencia = idinicidencia;
    }

}