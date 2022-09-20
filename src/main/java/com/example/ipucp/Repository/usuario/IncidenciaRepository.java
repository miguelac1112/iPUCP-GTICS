package com.example.ipucp.Repository.usuario;

import com.example.ipucp.Entity.Inicidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidenciaRepository extends JpaRepository<Inicidencia, Integer> {
}
