package com.example.ipucp.Repository;

import com.example.ipucp.Entity.Inicidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InicidenciaRepository extends JpaRepository<Inicidencia, Integer> {
}