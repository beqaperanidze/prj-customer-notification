package croco.prjcustomernotification.service.implementation;

import croco.prjcustomernotification.dto.AuthRequest;
import croco.prjcustomernotification.dto.AuthResponse;
import croco.prjcustomernotification.dto.RegisterRequest;
import croco.prjcustomernotification.model.Admin;
import croco.prjcustomernotification.repository.AdminRepository;
import croco.prjcustomernotification.security.CustomUserDetailsService;
import croco.prjcustomernotification.security.JwtUtil;
import croco.prjcustomernotification.service.interfaces.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService,
                           JwtUtil jwtUtil, AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<?> registerUser(RegisterRequest registerRequest) {
        if (adminRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is already taken");
        }

        Admin admin = Admin.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .build();

        adminRepository.save(admin);

        final String jwt = jwtUtil.generateToken(admin.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(jwt));
    }

    public ResponseEntity<?> createAuthenticationToken(AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(new AuthResponse(jwt));
    }
}