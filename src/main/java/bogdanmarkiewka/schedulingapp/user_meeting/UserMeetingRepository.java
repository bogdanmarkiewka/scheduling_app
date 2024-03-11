package bogdanmarkiewka.schedulingapp.user_meeting;

import bogdanmarkiewka.schedulingapp.meeting.MeetingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserMeetingRepository extends JpaRepository<UserMeetingEntity, UUID> {

    @Query("SELECT ume.meetingEntity FROM UserMeetingEntity ume WHERE ume.userEntity.id = :userId AND ume.meetingEntity.time > CURRENT_TIMESTAMP ORDER BY ume.meetingEntity.time ASC")
    List<MeetingEntity> findUpcomingMeetingsByUserId(@Param("userId") UUID userId);

}
