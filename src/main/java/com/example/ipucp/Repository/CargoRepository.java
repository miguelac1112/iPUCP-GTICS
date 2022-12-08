package com.example.ipucp.Repository;

import com.example.ipucp.Entity.Cargo;
import com.example.ipucp.Entity.Inicidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Integer> {

    @Query(value="select * from cargo where (idcargo='1' or idcargo='3' or idcargo='4' or idcargo='5');",nativeQuery = true)
    List<Cargo> cargosSeguridad();
}