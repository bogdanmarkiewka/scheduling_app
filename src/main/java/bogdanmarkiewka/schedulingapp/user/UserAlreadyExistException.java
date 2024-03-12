package bogdanmarkiewka.schedulingapp.user;

public class UserAlreadyExistException extends RuntimeException {

    public UserAlreadyExistException(String email) {
        super(String.format("User with given email already exist [email: %s]", email));
    }

}
