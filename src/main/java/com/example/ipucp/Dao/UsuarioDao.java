package com.example.ipucp.Dao;

import com.example.ipucp.Dto.UsuarioDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UsuarioDao {
    public void guardarUsuario(UsuarioDto usuario) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = "http://localhost:8081/saveUser";
        HttpEntity<UsuarioDto> httpEntity = new HttpEntity<>(usuario, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(url, httpEntity, UsuarioDto.class);

    }
}
