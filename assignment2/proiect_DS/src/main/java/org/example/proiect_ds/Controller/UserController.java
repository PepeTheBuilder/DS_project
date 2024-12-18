package org.example.proiect_ds.Controller;

import org.example.proiect_ds.Entity.User;
import org.example.proiect_ds.Services.UserService;
import org.example.proiect_ds.Utils.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private UserService userService;

    private User currentUser;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if (userService.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This email already exists");
        }
        String passEncoded = Encoder.encodingPassword(user.getPassword());
        user.setPassword(passEncoded);
        userService.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        User existingUser = userService.findByEmail(user.getEmail());

        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        String decodedPassword = Encoder.decodingPassword(existingUser.getPassword());

        if (!decodedPassword.equals(user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        currentUser = existingUser;
        String role;
        if (existingUser.isAdmin()) {
            role = "Redirecting to admin page";
        } else {
            role = "Redirecting to user page";
        }
        return ResponseEntity.ok(role);
    }


    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        if (!isAdmin()) {
            System.out.println("User is not admin");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }


    @GetMapping("/getUser")
    public ResponseEntity<User> getUserById(@RequestBody long id) {
        if (!isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        if (!isAdmin()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User existingUser = userService.findById(user.getId());
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }
        user.setPassword(existingUser.getPassword());
        user.setAdmin(existingUser.isAdmin());
        userService.save(user);
        return ResponseEntity.ok(user);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(@RequestBody long id) {
        if (!isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/isAdmin")
    public boolean isAdmin() {
        return (currentUser != null && currentUser.isAdmin());
    }

    @GetMapping("/getCurrentEmail")
    public ResponseEntity<User> getCurrentEmail() {
        User NewUser = new User();
        NewUser.setEmail(currentUser.getEmail());
        return ResponseEntity.ok(NewUser);
    }
}
