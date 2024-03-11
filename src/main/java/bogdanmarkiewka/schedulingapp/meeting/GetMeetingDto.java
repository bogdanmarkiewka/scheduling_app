package bogdanmarkiewka.schedulingapp.meeting;

import java.time.OffsetDateTime;
import java.util.UUID;

public record GetMeetingDto(
        UUID id,
        OffsetDateTime time
) {
}
