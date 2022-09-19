package com.example.ipucp.Repository;

import com.example.ipucp.Entity.Tipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TipoRepository extends JpaRepository<Tipo, Integer> {
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO `ipucp`.`tipo` (`tipo_incidencia`) VALUES (?1)",nativeQuery = true)
    void crearTipoIncidencia(String nombre);
}