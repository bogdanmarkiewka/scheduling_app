package bogdanmarkiewka.schedulingapp.meeting;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

public record GetAvailableSlotsDto(
        Set<UUID> userIds,
        OffsetDateTime startRange
) {
}
