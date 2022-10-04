package com.example.ipucp.Repository;

import com.example.ipucp.Dto.TipoIncidenReporte;
import com.example.ipucp.Entity.Tipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TipoRepository extends JpaRepository<Tipo, Integer> {
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO `ipucp`.`tipo` (`tipo_incidencia`) VALUES (?1)",nativeQuery = true)
    void crearTipoIncidencia(String nombre);

    @Query(value = "select t.idtipo as 'id', tipo_incidencia as 'tipoIncidencia', idinicidencia\n" +
            "from tipo t\n" +
            "\tleft join inicidencia i on (t.idtipo = i.idtipo)\n" +
            "group by t.idtipo",nativeQuery = true)
    List<TipoIncidenReporte> listaIncidencias();
}