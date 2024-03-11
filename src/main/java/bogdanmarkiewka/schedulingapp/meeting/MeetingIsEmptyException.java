package bogdanmarkiewka.schedulingapp.meeting;

public class MeetingIsEmptyException extends RuntimeException {

    public MeetingIsEmptyException() {
        super("Meeting must have at least one user.");
    }

}
