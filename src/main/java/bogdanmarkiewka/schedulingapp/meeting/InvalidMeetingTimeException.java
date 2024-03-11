package bogdanmarkiewka.schedulingapp.meeting;

public class InvalidMeetingTimeException extends RuntimeException {

    public InvalidMeetingTimeException() {
        super("Meeting time must be at the start of an hour.");
    }
}
