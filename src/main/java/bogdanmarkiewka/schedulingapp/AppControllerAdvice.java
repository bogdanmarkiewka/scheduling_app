package bogdanmarkiewka.schedulingapp;

import bogdanmarkiewka.schedulingapp.meeting.InvalidMeetingTimeException;
import bogdanmarkiewka.schedulingapp.meeting.MeetingInThePastException;
import bogdanmarkiewka.schedulingapp.meeting.MeetingIsEmptyException;
import bogdanmarkiewka.schedulingapp.meeting.MeetingTimeOccupiedException;
import bogdanmarkiewka.schedulingapp.user.UserAlreadyExistException;
import bogdanmarkiewka.schedulingapp.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class AppControllerAdvice {

    @ExceptionHandler({
            UserNotFoundException.class
    })
    public ResponseEntity<ErrorMessage> handleNotFoundException(RuntimeException exception) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorMessage errorDetails = new ErrorMessage(
                exception.getMessage(),
                status.value()
        );

        return ResponseEntity.status(status).body(errorDetails);
    }

    @ExceptionHandler({
            UserAlreadyExistException.class,
            InvalidMeetingTimeException.class,
            MeetingInThePastException.class,
            MeetingTimeOccupiedException.class,
            MeetingIsEmptyException.class
    })
    public ResponseEntity<ErrorMessage> handleBadRequestException(RuntimeException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorMessage errorDetails = new ErrorMessage(
                exception.getMessage(),
                status.value()
        );

        return ResponseEntity.status(status).body(errorDetails);
    }
}
