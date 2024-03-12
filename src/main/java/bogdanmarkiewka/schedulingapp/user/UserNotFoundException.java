package bogdanmarkiewka.schedulingapp.user;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(UUID id) {
        super(String.format("User not found [id: %s]", id));
    }

}
