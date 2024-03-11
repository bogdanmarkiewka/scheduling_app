package bogdanmarkiewka.schedulingapp.meeting;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

public record CreateMeetingDto(
        OffsetDateTime time,
        Set<UUID> userIds
) {
}
