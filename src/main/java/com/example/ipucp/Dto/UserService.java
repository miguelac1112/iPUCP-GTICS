package com.example.ipucp.Dto;

import com.example.ipucp.Entity.Usuario;
import com.example.ipucp.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UsuarioRepository repo;
    public void processOAuthPostLogin(String username){
        Usuario existUser = repo.findByCorreo(username);

    }
}
