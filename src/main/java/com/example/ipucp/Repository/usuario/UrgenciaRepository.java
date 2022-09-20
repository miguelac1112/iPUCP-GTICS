package com.example.ipucp.Repository.usuario;

import com.example.ipucp.Entity.Urgencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrgenciaRepository extends JpaRepository<Urgencia, Integer> {
}
