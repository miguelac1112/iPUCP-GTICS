package com.example.ipucp.Controller;

import com.example.ipucp.Repository.admin.CargoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    CargoRepository cargoRepository;

    @GetMapping("/listar")
    public String listar() {
        return "admin/listar";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("listaCargos",cargoRepository.findAll());
        return "admin/newForm";
    }


    @GetMapping("/incidencias")
    public String incidencias() {
        return "admin/incidencias";
    }
}
