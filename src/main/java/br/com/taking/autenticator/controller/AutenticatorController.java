package br.com.taking.autenticator.controller;

import br.com.taking.autenticator.dtos.UserDTO;
import br.com.taking.autenticator.dtos.ApiResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AutenticatorController {

    private final Map<String, String> userCredentials;

    public AutenticatorController(Map<String, String> userCredentials) {
        this.userCredentials = userCredentials;
        userCredentials.put("admin", "admin");
        userCredentials.put("user1", "password1");
        userCredentials.put("user2", "password2");
        userCredentials.put("user3", "password3");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
        String password = userCredentials.get(userDTO.getUser());
        if (password != null && password.equals(userDTO.getPassword())) {
            return ResponseEntity.ok()
                    .body(ApiResponseDTO.builder()
                            .code("200")
                            .message("OK")
                            .description("Usuário autenticado com sucesso.")
                    .build());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponseDTO.builder()
                            .code("401")
                            .message("Unauthorized")
                            .description("Usuário ou senha inválidos.")
                            .build());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        if (userCredentials.containsKey(userDTO.getUser())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponseDTO.builder()
                    .code("409")
                    .message("conflict")
                    .description("Usuário já cadastrado.")
                    .build());
        }
        userCredentials.put(userDTO.getUser(), userDTO.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.builder()
                        .code("201")
                        .message("created")
                        .description("Usuário criado com sucesso.")
                        .build());
    }
}
