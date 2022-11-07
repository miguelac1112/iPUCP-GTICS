package com.example.ipucp.Dao;

import com.example.ipucp.Entity.Perfil;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PerfilDao {
    public Perfil obtenerImagen(String id) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Perfil> response = restTemplate.getForEntity(
                "http://3.213.104.118:9000/azureStorage/download?code=" + id, Perfil.class);

        return response.getBody();
    }

    public void subirImagen(Perfil perfil){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = "http://3.213.104.118:9000/azureStorage/upload";
        HttpEntity<Perfil> httpEntity = new HttpEntity<>(perfil, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(url, httpEntity, Perfil.class);


    }
}
