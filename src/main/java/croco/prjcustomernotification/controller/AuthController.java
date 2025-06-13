package croco.prjcustomernotification.controller;

import croco.prjcustomernotification.dto.AuthRequest;
import croco.prjcustomernotification.dto.RegisterRequest;
import croco.prjcustomernotification.service.interfaces.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Operations related to user authentication and registration")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account in the system")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User registered successfully"), @ApiResponse(responseCode = "400", description = "Invalid registration data or username already exists", content = @Content)})
    public ResponseEntity<?> registerUser(@Parameter(description = "Registration details") @Valid @RequestBody RegisterRequest registerRequest) {
        return authService.registerUser(registerRequest);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Validates user credentials and returns an authentication token")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Authentication successful"), @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)})
    public ResponseEntity<?> createAuthenticationToken(@Parameter(description = "Authentication credentials") @Valid @RequestBody AuthRequest authRequest) {
        return authService.createAuthenticationToken(authRequest);
    }
}