package es.project.apiagencia.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import es.project.apiagencia.entities.UserEntity;
import es.project.apiagencia.models.PasswordUpdateDTO;
import es.project.apiagencia.models.ResponseDTO;
import es.project.apiagencia.models.ReviewDTO;
import es.project.apiagencia.models.UserDTO;
import es.project.apiagencia.models.UserRolesDTO;
import es.project.apiagencia.services.AuthService;
import es.project.apiagencia.services.UserService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserControllers {

    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping
    public ResponseEntity<List<UserRolesDTO>> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}/roles")
    public ResponseEntity<UserRolesDTO> getUserRoles(@PathVariable Long userId) {
        return userService.getUserRoles(userId);
    }

    @PatchMapping("/{userId}/name")
    public ResponseEntity<UserDTO> updateName(@PathVariable Long userId, @RequestBody String nameNew) {
        return userService.updateName(userId, nameNew);
    }

    @PatchMapping("/{userId}/email")
    public ResponseEntity<UserDTO> updateEmail(@PathVariable Long userId, @RequestBody String emailNew) {
        return userService.updateEmail(userId, emailNew);
    }

    @PatchMapping("/{userId}/password")
    public ResponseEntity<UserDTO> updatePassword(@PathVariable Long userId,
            @RequestBody PasswordUpdateDTO passwords) {
        return userService.updatePassword(userId, passwords);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseDTO> removeUser(@PathVariable Long userId) {
        return userService.removeUser(userId);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ResponseDTO> updateUser(@PathVariable Long userId, @RequestBody UserEntity updatedUser) {
        return userService.updateUser(userId, updatedUser);
    }
}
