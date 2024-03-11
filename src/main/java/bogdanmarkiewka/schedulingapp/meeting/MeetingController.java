package bogdanmarkiewka.schedulingapp.meeting;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/meetings")
public class MeetingController {

    private final MeetingService meetingService;
    private final MeetingSuggestionService meetingSuggestionService;

    public MeetingController(
            MeetingService meetingService,
            MeetingSuggestionService meetingSuggestionService
    ) {
        this.meetingService = meetingService;
        this.meetingSuggestionService = meetingSuggestionService;
    }

    @PostMapping
    public ResponseEntity<CreateMeetingDto> create(
            @RequestBody CreateMeetingDto request
    ) {
        UUID meetingId = meetingService.createMeeting(request);

        return ResponseEntity
                .created(URI.create("/api/meetings/" + meetingId))
                .build();
    }

    @GetMapping("/upcoming/{userId}")
    public ResponseEntity<List<GetMeetingDto>> getUpcomingMeetings(@PathVariable UUID userId) {
        List<GetMeetingDto> meetings = meetingService.getUpcomingMeetingsForUser(userId);
        return ResponseEntity.ok(meetings);
    }

    @PostMapping("/available-slots")
    public ResponseEntity<List<OffsetDateTime>> getAvailableSlots(
            @RequestBody GetAvailableSlotsDto request
    ) {
        List<OffsetDateTime> availableSlots = meetingSuggestionService.getAvailableTimeslots(
                request
        );
        return ResponseEntity.ok(availableSlots);
    }

}
