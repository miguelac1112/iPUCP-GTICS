package com.example.ipucp.Dao;

import com.example.ipucp.Entity.Cargo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class CargoDao {

    public List<Cargo> listarCargos() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Cargo[]> response = restTemplate.getForEntity(
                "http://20.232.117.53:8081/listCargos", Cargo[].class);

        return Arrays.asList(response.getBody());
    }
}
