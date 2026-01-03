package org.carluselectum.ecommerce.service.auth;

import org.carluselectum.ecommerce.model.auth.User;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuthServiceTest {
    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService();
        authService.register("joao@email.com", "Pass123!");
    }

    @Test
    void testLogin_Success() {
        User result = authService.login("joao@email.com", "Pass123!");
        assertNotNull(result, "Login deve devolver um objeto User com credenciais corretas.");
        assertEquals("joao@email.com", result.getEmail());
    }

    @Test
    void testLogin_WrongPassword() {
        User result = authService.login("joao@email.com", "errada");
        assertNull(result, "Login deve devolver null com password incorreta.");
    }

    @Test
    void testLogin_NonExistentUser() {
        User result = authService.login("naoexiste@email.com", "123");
        assertNull(result, "Login deve devolver null para utilizador não registado.");
    }
}