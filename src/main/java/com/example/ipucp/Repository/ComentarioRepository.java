package com.example.ipucp.Repository;

import com.example.ipucp.Entity.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Integer> {
    @Query(value = "select * from comentarios  where idinicidencia=?1 and cargo = '1' order by fecha asc;",nativeQuery = true)
    List<Comentario> IncidenciasComentariosSeguridad(int idIncidencia);

    @Query(value = "select * from comentarios  where idinicidencia=?1 and cargo = '0' order by fecha asc;",nativeQuery = true)
    List<Comentario> IncidenciasComentariosUsuario(int idIncidencia);

    @Query(value = "select now();",nativeQuery = true)
    Instant fecha();
}