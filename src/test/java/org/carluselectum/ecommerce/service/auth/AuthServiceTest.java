package org.carluselectum.ecommerce.service.auth;

import static org.junit.jupiter.api.Assertions.*;
import org.carluselectum.ecommerce.model.auth.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuthServiceTest {
    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService();
        // O novo register recebe apenas Strings, não o objeto User completo
        // Nota: se correres o teste várias vezes, podes ter erro de "Duplicate Entry"
        // na BD
        authService.register("joao@email.com", "Pass123!");
    }

    @Test
    void testLogin_Success() {
        // Agora o login devolve um objeto User (sucesso) ou null (falha)
        User result = authService.login("joao@email.com", "Pass123!");
        assertNotNull(result, "Login deve devolver um objeto User com credenciais corretas.");
        assertEquals("joao@email.com", result.getEmail());
    }

    @Test
    void testLogin_WrongPassword() {
        // Se a password estiver errada, deve devolver null
        User result = authService.login("joao@email.com", "errada");
        assertNull(result, "Login deve devolver null com password incorreta.");
    }

    @Test
    void testLogin_NonExistentUser() {
        User result = authService.login("naoexiste@email.com", "123");
        assertNull(result, "Login deve devolver null para utilizador não registado.");
    }
}