package bogdanmarkiewka.schedulingapp.meeting;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MeetingMapper {

    GetMeetingDto toGetMeetingDto(MeetingEntity meetingEntity);

    MeetingEntity toEntity(CreateMeetingDto createMeetingDto);

}

