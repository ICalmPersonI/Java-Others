package recipes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import recipes.entity.User;
import recipes.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {

    @Autowired
    private UserService service;

    @PostMapping()
    public void register(HttpServletRequest request, HttpServletResponse response, @Valid @RequestBody User user) {
        user.setActive(true);
        user.setRoles("USER");
        String name = user.getEmail();
        String password = user.getPassword();
        if (service.saveUser(user)) {
            authWithHttpServletRequest(request, name, password);
            response.setStatus(200);
        } else {
            response.setStatus(400);
        }
    }

    public void authWithHttpServletRequest(HttpServletRequest request, String username, String password) {
        try {
            System.out.println(request.getUserPrincipal());
            if (request.getUserPrincipal() != null) {
                request.logout();
            }
            request.login(username, password);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }
}
