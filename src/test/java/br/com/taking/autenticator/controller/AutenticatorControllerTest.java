package br.com.taking.autenticator.controller;

import br.com.taking.autenticator.dtos.ApiResponseDTO;
import br.com.taking.autenticator.dtos.UserDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public class AutenticatorControllerTest {
    private AutenticatorController autenticatorController;
    private Map<String, String> userCredentials;

    @BeforeEach
    public void setup() {
        userCredentials = new HashMap<>();
        autenticatorController = new AutenticatorController(userCredentials);
    }

    @Test
    @DisplayName("Autenticação retorno com sucesso quando as credenciais são válidas")
    public void authenticateReturnsOkWhenCredentialsAreValid() {
        userCredentials.put("validUser", "validPassword");
        UserDTO userDTO = new UserDTO("validUser", "validPassword");

        ResponseEntity<?> response = autenticatorController.authenticate(userDTO);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("200", ((ApiResponseDTO) response.getBody()).getCode());
    }

    @Test
    @DisplayName("Autenticação com retorno não autorizado quando as credenciais são inválidas")
    public void authenticateReturnsUnauthorizedWhenCredentialsAreInvalid() {
        userCredentials.put("validUser", "validPassword");
        UserDTO userDTO = new UserDTO("validUser", "invalidPassword");

        ResponseEntity<?> response = autenticatorController.authenticate(userDTO);

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assertions.assertEquals("401", ((ApiResponseDTO) response.getBody()).getCode());
    }

    @Test
    @DisplayName("Cria usuário e senha quando usuário não existe ou não cadastrado")
    public void createUserReturnsCreatedWhenUserDoesNotExist() {
        UserDTO userDTO = new UserDTO("newUser", "newPassword");

        ResponseEntity<?> response = autenticatorController.createUser(userDTO);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals("201", ((ApiResponseDTO) response.getBody()).getCode());
    }

    @Test
    @DisplayName("Criação de usuário retorna conflito quando usuário já existente ou cadastrado")
    public void createUserReturnsConflictWhenUserAlreadyExists() {
        userCredentials.put("existingUser", "existingPassword");
        UserDTO userDTO = new UserDTO("existingUser", "newPassword");

        ResponseEntity<?> response = autenticatorController.createUser(userDTO);

        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assertions.assertEquals("409", ((ApiResponseDTO) response.getBody()).getCode());
    }
}
