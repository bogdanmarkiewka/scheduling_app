package bogdanmarkiewka.schedulingapp.meeting;

public class MeetingTimeOccupiedException extends RuntimeException {

    public MeetingTimeOccupiedException() {
        super("Given meeting time slot is occupied.");
    }
}
