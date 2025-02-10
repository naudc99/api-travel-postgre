package es.project.apiagencia.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import es.project.apiagencia.entities.RoleEntity;
import es.project.apiagencia.entities.UserEntity;
import es.project.apiagencia.models.JwtTokenDTO;
import es.project.apiagencia.models.LoginDTO;
import es.project.apiagencia.models.ResponseDTO;
import es.project.apiagencia.repositories.RoleRepository;
import es.project.apiagencia.repositories.UserRepository;
import es.project.apiagencia.security.JWTUtilityService;

import java.util.*;

@Service
public class AuthService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JWTUtilityService jwtUtilityService;
    
    public AuthService(UserRepository userRepository, RoleRepository roleRepository, JWTUtilityService jwtUtilityService){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtilityService = jwtUtilityService;
    }
    public ResponseEntity<List<String>> getAllUserNames() {
        try {
            List<UserEntity> users = userRepository.findAll();
            List<String> names = new ArrayList<>();
            if (users.isEmpty())
                return ResponseEntity.noContent().build();
            for (UserEntity userEntity : users)
                names.add(userEntity.getName());
            return ResponseEntity.ok(names);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    public ResponseEntity<JwtTokenDTO> login(LoginDTO login) throws Exception {
        try {
            Optional<UserEntity> userOPT = userRepository.findByEmail(login.getEmail());
            if (userOPT.isEmpty())
                return ResponseEntity.notFound().build();
            else {
                UserEntity user = userOPT.get();
                if (verifyPassword(login.getPassword(), user.getPassword())) {
                    JwtTokenDTO jwtTokenDTO = new JwtTokenDTO(jwtUtilityService.generateJWT(user.getId(),  user.getRole()));
                    return ResponseEntity.ok(jwtTokenDTO);
                } else
                    return ResponseEntity.internalServerError().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private boolean verifyPassword(String enteredPassword, String storedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(enteredPassword, storedPassword);
    }

    public ResponseEntity<ResponseDTO> register(UserEntity userNew, BindingResult result) {
        ResponseDTO response = new ResponseDTO();
        try {
            if (result != null && result.hasErrors()) {
                for (FieldError error : result.getFieldErrors())
                    response.newError(String.format("%s: %s", error.getField(), error.getDefaultMessage()));
                return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
            }
            Optional<UserEntity> existingUser = userRepository.findByName(userNew.getName());
            if (existingUser.isPresent()) {
                response.newError("Nombre en uso, por favor elija otro");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }
            existingUser = userRepository.findByEmail(userNew.getEmail());
            if (existingUser.isPresent()) {
                response.newError("Email ya registrado");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }
            UserEntity userTEMP = getTemplateUser();
            Optional<UserEntity> userOPT = updateTemplateUser(userTEMP, userNew);
            if (userOPT.isEmpty())
                return ResponseEntity.unprocessableEntity().build();
            response.newMessage("Usuario creado");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private UserEntity getTemplateUser() {
        int threshold = 3600;
        Optional<UserEntity> userTEMP = userRepository.findFirstUpdatable(threshold);
        if (userTEMP.isEmpty()) {
            UserEntity userEntity = new UserEntity();
            userEntity.setName("NombreNoValido");
            userEntity.setEmail("emailn@oval.ido");
            userEntity.setPassword("ContraseñaNoValida");
            userEntity = userRepository.save(userEntity);
            return userEntity;
        }
        return userTEMP.get();
    }

    private Optional<UserEntity> updateTemplateUser(UserEntity userTemplate, UserEntity updatedUser) {
        try {
            userTemplate.setLifeSpan(updatedUser.getLifeSpan().plusYears(100));
            userTemplate.setName(updatedUser.getName());
            userTemplate.setEmail(updatedUser.getEmail());
            if (!updatedUser.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#ñÑ])[A-Za-z\\d@$!%*?&#ñÑ]{8,}$"))
                return Optional.empty();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
            userTemplate.setPassword(encoder.encode(updatedUser.getPassword()));
            Optional<RoleEntity> roleOTP = roleRepository.findByName("USER");
            if(roleOTP.isEmpty())
                return Optional.empty();
            RoleEntity rol = new RoleEntity();
            rol=roleOTP.get();
            userTemplate.setRole(rol);
            userTemplate.setImage("profile.png");
            
            UserEntity savedUser = userRepository.save(userTemplate);
            userRepository.save(userTemplate);

            return Optional.of(savedUser);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}