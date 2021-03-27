package org.example.service;

import org.apache.log4j.Logger;
import org.example.repository.ProjectRepository;
import org.example.dto.LoginForm;
import org.example.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final ProjectRepository<User> userRepo;

    @Autowired
    public LoginService(ProjectRepository<User> userRepo) {
        this.userRepo = userRepo;
    }

    private Logger logger = Logger.getLogger(LoginService.class);

    public boolean authenticate(LoginForm loginForm) {
        logger.info("try auth with user-form: " + loginForm);
//        String usr = loginForm.getUsername();
//        String pwd = loginForm.getPassword();
//        User user = userRepo.retreiveAll().stream().filter(e -> usr.equals(e.getUsr()))
//                .filter(e -> pwd.equals(e.getPwd()))
//                .findFirst().orElse(null);
//        return user != null;
        return loginForm.getUsername().equals("root") && loginForm.getPassword().equals("123");
    }

    public boolean signUp(LoginForm loginForm) {
        String usr = loginForm.getUsername();
        String pwd = loginForm.getPassword();
        User existingUser = userRepo.retreiveAll().stream().filter(e -> usr.equals(e.getUsr()))
                .findFirst().orElse(null);
        if (existingUser != null) {
            logger.info("user " + usr + " already exists!");
            return false;
        }
        else {
            User user = new User();
            user.setUsr(usr);
            user.setPwd(pwd);
            logger.info("trying to add user: " + user);
            userRepo.store(user);
            return true;
        }
    }

}
