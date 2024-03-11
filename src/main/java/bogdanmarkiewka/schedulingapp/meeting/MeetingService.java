package bogdanmarkiewka.schedulingapp.meeting;

import bogdanmarkiewka.schedulingapp.user.UserEntity;
import bogdanmarkiewka.schedulingapp.user.UserNotFoundException;
import bogdanmarkiewka.schedulingapp.user.UserRepository;
import bogdanmarkiewka.schedulingapp.user_meeting.UserMeetingEntity;
import bogdanmarkiewka.schedulingapp.user_meeting.UserMeetingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final UserMeetingRepository userMeetingRepository;
    private final MeetingMapper meetingMapper;

    public MeetingService(
            MeetingRepository meetingRepository,
            UserRepository userRepository,
            UserMeetingRepository userMeetingRepository,
            MeetingMapper meetingMapper
    ) {
        this.meetingRepository = meetingRepository;
        this.userRepository = userRepository;
        this.userMeetingRepository = userMeetingRepository;
        this.meetingMapper = meetingMapper;
    }

    @Transactional
    public UUID createMeeting(CreateMeetingDto createMeetingDto) {
        validateMeeting(createMeetingDto);

        MeetingEntity meetingEntity = meetingMapper.toEntity(createMeetingDto);
        meetingEntity = meetingRepository.save(meetingEntity);

        for (UUID userId : createMeetingDto.userIds()) {
            UserEntity userEntity = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException(userId));
            saveUserMeeting(userEntity, meetingEntity);
        }

        return meetingEntity.getId();
    }

    public List<GetMeetingDto> getUpcomingMeetingsForUser(UUID userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return userMeetingRepository.findUpcomingMeetingsByUserId(userId)
                .stream()
                .map(meetingMapper::toGetMeetingDto)
                .collect(Collectors.toList());
    }

    private void saveUserMeeting(UserEntity userEntity, MeetingEntity meetingEntity) {
        UserMeetingEntity userMeetingEntity = new UserMeetingEntity(userEntity, meetingEntity);
        userMeetingRepository.save(userMeetingEntity);
    }


    private void validateMeeting(CreateMeetingDto createMeetingDto) {
        validateMeetingUsers(createMeetingDto.userIds());
        validateMeetingTime(createMeetingDto.time());
    }

    private void validateMeetingUsers(Set<UUID> userIds) {
        if (userIds.isEmpty()) {
            throw new MeetingIsEmptyException();
        }
    }

    private void validateMeetingTime(OffsetDateTime time) {
        if (time.isBefore(OffsetDateTime.now())) {
            throw new MeetingInThePastException();
        }

        if (time.getMinute() != 0 || time.getSecond() != 0 || time.getNano() != 0) {
            throw new InvalidMeetingTimeException();
        }

        List<MeetingEntity> meetingsAtSameTime = meetingRepository.findByTime(time);
        if (!meetingsAtSameTime.isEmpty()) {
            throw new MeetingTimeOccupiedException();
        }
    }

}
