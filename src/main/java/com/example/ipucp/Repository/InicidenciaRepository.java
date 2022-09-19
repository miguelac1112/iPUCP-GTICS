package com.example.ipucp.Repository;

import com.example.ipucp.Entity.Inicidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InicidenciaRepository extends JpaRepository<Inicidencia, Integer> {
    @Query(value="select * from inicidencia where codigo= ?1 ;",nativeQuery = true)
    List<Inicidencia> userIncidencias(String codigo);
}