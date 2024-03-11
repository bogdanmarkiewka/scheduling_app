package bogdanmarkiewka.schedulingapp.meeting

import bogdanmarkiewka.schedulingapp.user.UserEntity
import bogdanmarkiewka.schedulingapp.user.UserNotFoundException
import bogdanmarkiewka.schedulingapp.user.UserRepository
import bogdanmarkiewka.schedulingapp.user_meeting.UserMeetingEntity
import bogdanmarkiewka.schedulingapp.user_meeting.UserMeetingRepository
import org.springframework.test.util.ReflectionTestUtils
import spock.lang.Specification

import java.time.OffsetDateTime

class MeetingServiceSpec extends Specification {

    MeetingService meetingService

    MeetingRepository meetingRepository
    UserRepository userRepository
    UserMeetingRepository userMeetingRepository
    MeetingMapper meetingMapper

    OffsetDateTime nowPlusDay

    void setup() {
        meetingRepository = Stub(MeetingRepository.class)
        userRepository = Stub(UserRepository.class)
        userMeetingRepository = Stub(UserMeetingRepository.class)
        meetingMapper = Stub(MeetingMapper.class)

        meetingService = new MeetingService(
                meetingRepository,
                userRepository,
                userMeetingRepository,
                meetingMapper
        )

        nowPlusDay = OffsetDateTime.now()
                .plusDays(1)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
    }

    def "should create meeting successfully"() {
        given:
        UUID userId = UUID.randomUUID()
        UUID meetingId = UUID.randomUUID()

        CreateMeetingDto createMeetingDto = new CreateMeetingDto(nowPlusDay, Set.of(userId))
        UserEntity userEntity = createTestUser("John Doe")
        MeetingEntity meetingEntity = createTestMeeting(meetingId, nowPlusDay)

        when:
        userRepository.findById(userId) >> Optional.of(userEntity)
        meetingMapper.toEntity(createMeetingDto) >> meetingEntity
        meetingRepository.save(meetingEntity) >> meetingEntity
        userMeetingRepository.save(_ as UserMeetingEntity) >> Mock(UserMeetingEntity)

        then:
        UUID createdMeetingId = meetingService.createMeeting(createMeetingDto)
        createdMeetingId == meetingId
    }

    def "should throw MeetingIsEmptyException if userIds is empty"() {
        given:
        CreateMeetingDto createMeetingDto = new CreateMeetingDto(nowPlusDay, Collections.emptySet())

        when:
        meetingService.createMeeting(createMeetingDto)

        then:
        def exception = thrown(MeetingIsEmptyException)
        exception.message == "Meeting must have at least one user."
    }

    def "should throw UserNotFoundException if user does not exist"() {
        given:
        UUID userId = UUID.randomUUID()
        UUID meetingId = UUID.randomUUID()

        CreateMeetingDto createMeetingDto = new CreateMeetingDto(nowPlusDay, Set.of(userId))
        MeetingEntity meetingEntity = createTestMeeting(meetingId, nowPlusDay)

        when:
        userRepository.findById(userId) >> Optional.empty()
        meetingMapper.toEntity(createMeetingDto) >> meetingEntity
        meetingRepository.save(meetingEntity) >> meetingEntity

        meetingService.createMeeting(createMeetingDto)

        then:
        def exception = thrown(UserNotFoundException)
        exception.message == "User not found [id: ${userId}]"
    }

    def "should throw MeetingInThePastException if meeting time is in the past"() {
        given:
        UUID userId = UUID.randomUUID()
        OffsetDateTime timeInThePast = OffsetDateTime.now()
                .minusDays(1)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)

        CreateMeetingDto createMeetingDto = new CreateMeetingDto(timeInThePast, Set.of(userId))

        when:
        meetingService.createMeeting(createMeetingDto)

        then:
        def exception = thrown(MeetingInThePastException)
        exception.message == "Meeting time cannot be in the past."
    }

    def "should throw InvalidMeetingTimeException if meeting time is not on hour mark"() {
        given:
        UUID userId = UUID.randomUUID()
        OffsetDateTime timeNotOnHourMark = OffsetDateTime.now()
                .plusDays(1)
                .withMinute(1)
                .withSecond(1)
                .withNano(1)

        CreateMeetingDto createMeetingDto = new CreateMeetingDto(timeNotOnHourMark, Set.of(userId))

        when:
        meetingService.createMeeting(createMeetingDto)

        then:
        def exception = thrown(InvalidMeetingTimeException)
        exception.message == "Meeting time must be at the start of an hour."
    }

    def "should throw MeetingTimeOccupiedException if meeting time is occupied"() {
        given:
        UUID userId = UUID.randomUUID()
        UUID meetingId = UUID.randomUUID()

        MeetingEntity meetingEntity = createTestMeeting(meetingId, nowPlusDay)
        CreateMeetingDto createMeetingDto = new CreateMeetingDto(nowPlusDay, Set.of(userId))

        when:
        meetingRepository.findByTime(nowPlusDay) >> List.of(meetingEntity)

        and:
        meetingService.createMeeting(createMeetingDto)

        then:
        def exception = thrown(MeetingTimeOccupiedException)
        exception.message == "Given meeting time slot is occupied."
    }

    def "should return upcoming meetings for user"() {
        given:
        UUID userId = UUID.randomUUID()
        UUID meetingId = UUID.randomUUID()

        UserEntity userEntity = createTestUser("John Doe")
        MeetingEntity meetingEntity = createTestMeeting(meetingId, nowPlusDay)
        GetMeetingDto getMeetingDto = new GetMeetingDto(meetingId, nowPlusDay)

        when:
        userRepository.findById(userId) >> Optional.of(userEntity)
        userMeetingRepository.findUpcomingMeetingsByUserId(userId) >> List.of(meetingEntity)
        meetingMapper.toGetMeetingDto(meetingEntity) >> getMeetingDto

        then:
        List<GetMeetingDto> upcomingMeetings = meetingService.getUpcomingMeetingsForUser(userId)
        upcomingMeetings.size() == 1
        upcomingMeetings.get(0).id() == meetingId
    }

    def "should throw UserNotFoundException if user does not exist when searching for upcoming meetings"() {
        given:
        UUID userId = UUID.randomUUID()

        when:
        userRepository.findById(userId) >> Optional.empty()

        and:
        meetingService.getUpcomingMeetingsForUser(userId)

        then:
        def exception = thrown(UserNotFoundException)
        exception.message == "User not found [id: ${userId}]"
    }

    private static UserEntity createTestUser(String name) {
        UserEntity userEntity = new UserEntity()
        userEntity.setName(name)
        return userEntity
    }

    private static MeetingEntity createTestMeeting(UUID id, OffsetDateTime nowPlusDay) {
        MeetingEntity meetingEntity = new MeetingEntity()
        meetingEntity.setTime(nowPlusDay)
        ReflectionTestUtils.setField(meetingEntity, "id", id)
        return meetingEntity
    }

}
