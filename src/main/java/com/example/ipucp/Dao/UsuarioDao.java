package com.example.ipucp.Dao;

import com.example.ipucp.Dto.UsuarioDto;
import com.example.ipucp.Dto.UsuarioDto2;
import com.example.ipucp.Entity.Cargo;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class UsuarioDao {
    public void guardarUsuario(UsuarioDto usuario) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = "http://20.232.117.53:8081/saveUser";
        HttpEntity<UsuarioDto> httpEntity = new HttpEntity<>(usuario, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(url, httpEntity, UsuarioDto.class);

    }

    public List<UsuarioDto> listarUsuarios() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UsuarioDto[]> response = restTemplate.getForEntity(
                "http://20.232.117.53:8081/listarUsuarios", UsuarioDto[].class);

        return Arrays.asList(response.getBody());
    }

    public UsuarioDto buscarOtro(String codigo){
        String searchUrl = "http://20.232.117.53:8081/findOther/" + codigo;
        RestTemplate restTemplate = new RestTemplate();
        try{
            ResponseEntity<UsuarioDto2> responseEntity = restTemplate.getForEntity(searchUrl,UsuarioDto2.class);

            if(responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody().getExiste().equals("true")){
                System.out.println("Posicion A ################################################################################");
                System.out.println(responseEntity.getBody().getExiste());
                return responseEntity.getBody().getUsuario();
            }
            System.out.println("Posicion B ################################################################################");
            return null;
        }catch (Exception e){
            System.out.println("Posicion C ################################################################################");
            return null;
        }
    }
}
