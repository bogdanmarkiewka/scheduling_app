package bogdanmarkiewka.schedulingapp.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<CreateUserDto> create(
            @RequestBody CreateUserDto request
    ) {
        UUID userID = userService.create(request);

        return ResponseEntity
                .created(URI.create("/api/users/" + userID))
                .build();
    }

}
