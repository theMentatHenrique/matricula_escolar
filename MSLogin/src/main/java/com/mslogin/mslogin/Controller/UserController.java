package com.mslogin.mslogin.Controller;


import com.ms.common.DTO.BaseDTO;
import com.mslogin.mslogin.DTO.DTOCadastrarUsuario;
import com.mslogin.mslogin.DTO.DTOLogarUsuario;
import com.mslogin.mslogin.Model.User;
import com.mslogin.mslogin.Repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private static boolean logado = false;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/cadastrar_usuario")
    public BaseDTO cadastrarUsuario(@RequestBody DTOCadastrarUsuario dtoCadastrarUsuario) {
        try{
            User user = new User();
            User userBanco = userRepository.findByUser(dtoCadastrarUsuario.email(), dtoCadastrarUsuario.senha());
            if (userBanco != null) {
                throw new Exception("Usuário já existente.");

            }
            BeanUtils.copyProperties(dtoCadastrarUsuario, user);
            userRepository.save(user);
            return new BaseDTO(true, "Usuário cadastrado com sucesso.");
        } catch (Exception e) {
            return new BaseDTO(false, e.getMessage());
        }
    }

    @GetMapping("/logar_usuario/")
    public BaseDTO logarUsuario(@RequestBody @Valid DTOLogarUsuario dtoLogarUsuario) {
        try {
            User user = userRepository.findByUser(dtoLogarUsuario.email(), dtoLogarUsuario.senha());
            if (user == null) {
                throw new Exception("Usuário não encontrado.");
            }
            logado = true;
            return new BaseDTO(true, "Usuário logado com sucesso.");

        } catch (Exception e) {
            return new BaseDTO(false, e.getMessage());
        }
    }

    @GetMapping("/deslogar_usuario/")
    public BaseDTO logoutUsuario() {
        if(logado) {
            logado = false;
            return new BaseDTO(true, "Usuário deslogado.");
        }
        return new BaseDTO(false, "Não foi possível deslogar o usuário.");
    }
}