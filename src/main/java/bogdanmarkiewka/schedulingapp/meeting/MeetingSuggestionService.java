package bogdanmarkiewka.schedulingapp.meeting;

import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class MeetingSuggestionService {

    static final int DEFAULT_SEARCHING_DURATION_IN_HOURS = 12;

    private final MeetingRepository meetingRepository;

    public MeetingSuggestionService(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    public List<OffsetDateTime> getAvailableTimeslots(
            GetAvailableSlotsDto getAvailableSlotsDto
    ) {
        var userIds = getAvailableSlotsDto.userIds();
        var startRange = getAvailableSlotsDto.startRange();
        OffsetDateTime endRange = startRange.plusHours(DEFAULT_SEARCHING_DURATION_IN_HOURS);
        return this.getAvailableTimeslots(userIds, startRange, endRange);
    }

    private List<OffsetDateTime> getAvailableTimeslots(
            Set<UUID> userIds,
            OffsetDateTime startRange,
            OffsetDateTime endRange
    ) {
        List<MeetingEntity> existingMeetings = meetingRepository.findMeetingsByUsersAndTimeRange(
                        userIds,
                        startRange,
                        endRange
                ).stream()
                .sorted(Comparator.comparing(MeetingEntity::getTime))
                .toList();

        List<OffsetDateTime> availableSlots = new ArrayList<>();
        OffsetDateTime slotStartTime = startRange;

        // Loop until slotStartTime is before (endRange - DEFAULT_MEETING_DURATION_IN_HOURS)
        while (!slotStartTime.isAfter(endRange.minusHours(MeetingEntity.DEFAULT_MEETING_DURATION_IN_HOURS))) {
            final OffsetDateTime slotEndTime = slotStartTime.plusHours(MeetingEntity.DEFAULT_MEETING_DURATION_IN_HOURS);
            boolean isSlotAvailable = true;

            for (MeetingEntity meeting : existingMeetings) {
                if (this.isOverlapping(meeting.getTime(), meeting.getEndTime(), slotStartTime, slotEndTime)) {
                    isSlotAvailable = false;
                    slotStartTime = meeting.getTime().plusHours(MeetingEntity.DEFAULT_MEETING_DURATION_IN_HOURS);
                    break;
                }
            }

            if (isSlotAvailable) {
                availableSlots.add(slotStartTime);
            }

            slotStartTime = slotEndTime;
        }

        return availableSlots;
    }

    private boolean isOverlapping(
            OffsetDateTime meetingStartTime,
            OffsetDateTime meetingEndTime,
            OffsetDateTime slotStartTime,
            OffsetDateTime slotEndTime
    ) {
        return slotStartTime.isBefore(meetingEndTime) && slotEndTime.isAfter(meetingStartTime);
    }

}
