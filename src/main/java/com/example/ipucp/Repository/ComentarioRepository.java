package com.example.ipucp.Repository;

import com.example.ipucp.Entity.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Integer> {
    @Query(value = "select * from comentarios  where idinicidencia=?1 and cargo = '1' order by fecha asc;",nativeQuery = true)
    List<Comentario> IncidenciasComentariosSeguridad(int idIncidencia);

    @Query(value = "select * from comentarios  where idinicidencia=?1 and cargo = '0' order by fecha asc;",nativeQuery = true)
    List<Comentario> IncidenciasComentariosUsuario(int idIncidencia);

    @Query(value = "SELECT DATE_SUB(now(), INTERVAL 5 HOUR);",nativeQuery = true)
    Instant fecha();

    @Query(value = "select * from comentarios\n" +
            "where idinicidencia=?1 and cargo = '1' and fecha = (select max(fecha) from comentarios where idinicidencia=?2 and cargo = '1');",nativeQuery = true)
    Comentario comentario(int idIncidencia, int idIncidencia2);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO `comentarios` (`text_comentario`, `fecha`, `cargo`, `idinicidencia`) VALUES (?1, ?2,'1', ?3);",nativeQuery = true)
    void comentarIncidencia(String comentario,Instant fecha ,int id);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO `comentarios` (`text_comentario`,`fecha`, `cargo`, `idinicidencia`) VALUES (?1, ?2,'0', ?3);",nativeQuery = true)
    void comentarIncidenciaUsuario(String comentario, Instant fecha, int id);
}