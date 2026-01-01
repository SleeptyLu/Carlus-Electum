package org.carluselectum.ecommerce.service.auth;

import org.carluselectum.ecommerce.data.UserRepository;
import org.carluselectum.ecommerce.model.auth.User;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    private UserRepository repo = new UserRepository();

    public void register(String email, String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        repo.save(new User(email, hashedPassword, "CLIENT"));
    }

    public User login(String email, String password) {
        User user = repo.findByEmail(email);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }
        return null;
    }
}