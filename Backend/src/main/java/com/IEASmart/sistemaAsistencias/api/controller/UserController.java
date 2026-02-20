package com.IEASmart.sistemaAsistencias.api.controller;

import com.IEASmart.sistemaAsistencias.domain.model.Admin;
import com.IEASmart.sistemaAsistencias.domain.model.Professor;
import com.IEASmart.sistemaAsistencias.domain.model.User;
import com.IEASmart.sistemaAsistencias.domain.model.valueObject.UserType;
import com.IEASmart.sistemaAsistencias.application.service.AuthorizationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("api/user")
public class UserController {

    private final AuthorizationService authorizationService;

    public UserController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping("/me")
    public ResponseEntity<User> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            // Si no está autenticado, devolvemos 401 (no 200 con objeto vacío): es más claro para el cliente
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof OAuth2User)) {
            // Si no es un OAuth2User esperado, devolvemos 401
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        OAuth2User userInfo = (OAuth2User) principal;
        String email = userInfo.getAttribute("email");

        if (email == null || email.isBlank()) {
            // Sin email no podemos identificar al usuario; devolvemos 400 Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<UserType> userTypeOpt = authorizationService.getUserTypeByEmail(email);

        if (userTypeOpt.isEmpty()) {
            // Usuario autenticado pero sin rol conocido en nuestra DB
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        User user;
        if (userTypeOpt.get() == UserType.ADMIN) {
            user = new Admin();
        } else if (userTypeOpt.get() == UserType.PROFESSOR) {
            user = new Professor();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Mapear atributos del proveedor de identidad. Los nombres de los atributos dependen del proveedor (Google, etc.).
        // Intentamos usar atributos estándar y caemos a valores derivados si es necesario.
        String fullName = userInfo.getAttribute("name");
        String givenName = userInfo.getAttribute("given_name");
        String familyName = userInfo.getAttribute("family_name");

        if (fullName != null && (givenName == null && familyName == null)) {
            // Si sólo tenemos fullName, intentar dividir en dos apellidos/nombres básicos
            String[] parts = fullName.trim().split(" ");
            if (parts.length == 1) {
                user.setNames(parts[0]);
            } else if (parts.length == 2) {
                user.setNames(parts[0]);
                user.setFirstLastName(parts[1]);
            } else {
                user.setNames(parts[0]);
                user.setFirstLastName(parts[1]);
                // resto a secondLastName
                StringBuilder sb = new StringBuilder();
                for (int i = 2; i < parts.length; i++) {
                    if (sb.length() > 0) sb.append(' ');
                    sb.append(parts[i]);
                }
                user.setSecondLastName(sb.toString());
            }
        } else {
            // Usar given_name / family_name si están disponibles
            if (givenName != null) user.setNames(givenName);
            if (familyName != null) user.setFirstLastName(familyName);
        }

        user.setEmail(email);
        Optional<Long> idOpt = authorizationService.getIdByEmail(email);
        idOpt.ifPresent(user::setUserId);

        String picture = userInfo.getAttribute("picture");
        if (picture != null) user.setPictureUrl(picture);

        return ResponseEntity.ok(user);
    }
}
