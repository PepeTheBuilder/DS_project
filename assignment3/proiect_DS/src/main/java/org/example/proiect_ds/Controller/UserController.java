package org.example.proiect_ds.Controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.example.proiect_ds.Entity.User;
import org.example.proiect_ds.Services.UserService;
import org.example.proiect_ds.Utils.Encoder;
import org.example.proiect_ds.Utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {
        "http://localhost:3600",
        "http://localhost:4200",
        "http://localhost:8080",
        "http://localhost:8081",
        "http://localhost:8082",
        "http://localhost:8083"},
        maxAge = 3600,
        allowCredentials = "true")
public class UserController {
    @Autowired
    private UserService userService;
    private User currentUser;
    private static final String SECRET_KEY = "49C2E57DCEC1823F5F1DB2546754E49C2E57DCEC1823F5F1DB2546754E"; // Replace with a securely stored key

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

        if (!existingUser.getPassword().equals(Encoder.encodingPassword(user.getPassword()))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        // Generate JWT
        String jwtToken = Jwts.builder()
                .setSubject(existingUser.getUsername())
                .claim("email", existingUser.getEmail())
                .claim("isAdmin", existingUser.isAdmin())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour expiration
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        HttpHeaders headers = new HttpHeaders();
        // Set token as a cookie
        headers.add("Set-Cookie", "token=" + jwtToken + "; Max-Age=3600; Path=/");
        headers.add("Access-Control-Expose-Headers", "Set-Cookie"); // Expose Set-Cookie to frontend


        String role;
        if (existingUser.isAdmin()) {
            role = "Redirecting to admin page x"+jwtToken+"z";
        } else {
            role = "Redirecting to user page x"+jwtToken+"z";
        }

        return ResponseEntity.ok().headers(headers).body(role);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers(HttpServletRequest request) {
        // Retrieve cookies from the request
        String token = getTokenOut(request);

        // Validate the token (you need a method for validating JWT)
        Boolean isAdmin = validateTokenAndGetRole(token);

        if (Boolean.FALSE.equals(isAdmin)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // If the user is admin, retrieve and return all users
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    private String getTokenOut(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        System.out.println("Cookies:" + Arrays.toString(cookies));
        if (cookies == null) {
            return "";
        }
        String token = "";
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                token = cookie.getValue();
                break;
            }
        }
        // If no token found in cookies, return unauthorized response
        if (token == null) {
            return "";
        }
        return token;
    }

    private Boolean validateTokenAndGetRole(String token) {
        // Validate the token
        if (!JwtUtil.isTokenValid(token)) {
            return null;
        }

        // Extract the "isAdmin" claim from the token
        return (Boolean) JwtUtil.extractClaims(token).get("isAdmin");
    }

    //This For chat
    @GetMapping("/getAllUsersEmail")
    public ResponseEntity<List<String>> getAllEmails() {
        List<User> users = userService.getAllUsers();
        List<String> emails = new ArrayList<>();
        for (User user : users) {
            emails.add(user.getEmail());
        }
        return ResponseEntity.ok(emails);
    }

    @GetMapping("/getUser")
    public ResponseEntity<User> getUserById(@RequestBody long id, HttpServletRequest request) {

        String token = getTokenOut(request);
        if (Boolean.FALSE.equals(validateTokenAndGetRole(token))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user, HttpServletRequest request) {
        String token = getTokenOut(request);
        Boolean aux = validateTokenAndGetRole(token);
        if (Boolean.FALSE.equals(aux)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
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
    public ResponseEntity<Void> deleteUser(@RequestBody long id, HttpServletRequest request) {
        String token = getTokenOut(request);
        if (Boolean.FALSE.equals(validateTokenAndGetRole(token))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/isAdmin")
    public boolean isAdmin() {
        return (currentUser != null && currentUser.isAdmin());
    }

    @GetMapping("/getCurrentUser")
    public ResponseEntity<User> getCurrentUser(HttpServletRequest request) {
        String username = getUserOut(request);

        if (username.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        User user = userService.findByUsername(username);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    private String getUserOut(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        System.out.println("Cookies:" + Arrays.toString(cookies));
        if (cookies == null) {
            return "";
        }
        String token = "";
        for (Cookie cookie : cookies) {
            if ("email".equals(cookie.getName())) {
                token = cookie.getValue();
                break;
            }
        }
        // If no token found in cookies, return unauthorized response
        if (token == null) {
            return "";
        }
        return token;
    }

}
