package bogdanmarkiewka.schedulingapp.meeting


import org.springframework.test.util.ReflectionTestUtils
import spock.lang.Specification

import java.time.OffsetDateTime

class MeetingSuggestionServiceSpec extends Specification {

    MeetingSuggestionService meetingSuggestionService

    MeetingRepository meetingRepository

    void setup() {
        meetingRepository = Stub(MeetingRepository.class)

        meetingSuggestionService = new MeetingSuggestionService(
                meetingRepository
        )
    }

    def "should return correct available slots"() {
        given:
        Set<UUID> userIds = Set.of(UUID.randomUUID(), UUID.randomUUID())
        OffsetDateTime startRange = OffsetDateTime.now()
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
        OffsetDateTime endRange = startRange.plusHours(MeetingSuggestionService.DEFAULT_SEARCHING_DURATION_IN_HOURS)
        GetAvailableSlotsDto getAvailableSlotsDto = new GetAvailableSlotsDto(userIds, startRange)

        List<MeetingEntity> existingMeetings = []

        for (int i = 0; i < 6; i++) {
            MeetingEntity meetingEntity = createTestMeeting(
                    UUID.randomUUID(),
                    startRange.plusHours(i)
            )
            existingMeetings.add(meetingEntity)
        }

        when:
        meetingRepository.findMeetingsByUsersAndTimeRange(
                userIds,
                startRange,
                endRange
        ) >> existingMeetings

        and:
        List<OffsetDateTime> result = meetingSuggestionService.getAvailableTimeslots(getAvailableSlotsDto)

        then:
        !result.isEmpty()
        result.size() == 6
        result.get(0).isAfter(startRange)
        result.get(result.size() - 1).isBefore(endRange)
    }

    private static MeetingEntity createTestMeeting(UUID id, OffsetDateTime nowPlusDay) {
        MeetingEntity meetingEntity = new MeetingEntity()
        meetingEntity.setTime(nowPlusDay)
        ReflectionTestUtils.setField(meetingEntity, "id", id)
        return meetingEntity
    }

}
