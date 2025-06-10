package croco.prjcustomernotification.service.interfaces;

import croco.prjcustomernotification.dto.AuthRequest;
import croco.prjcustomernotification.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    ResponseEntity<?> registerUser(RegisterRequest registerRequest);
    ResponseEntity<?> createAuthenticationToken(AuthRequest authRequest);
}
