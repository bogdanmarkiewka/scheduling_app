
package bogdanmarkiewka.schedulingapp.meeting;

public class MeetingInThePastException extends RuntimeException {

    public MeetingInThePastException() {
        super("Meeting time cannot be in the past.");
    }

}
