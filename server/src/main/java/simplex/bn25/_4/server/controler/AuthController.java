package simplex.bn25._4.server.controler;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")   // あるいは /api/reports でもOK
public class AuthController {

    /**
     * React 側の PrivateRoute から叩られる認証チェック用エンドポイント。
     * 認証済みなら 200 OK + "trainee01" のような文字列を返します。
     */
    @GetMapping("/whoami")
    public ResponseEntity<String> whoAmI(Authentication authentication) {
        return ResponseEntity.ok(authentication.getName());
    }
}