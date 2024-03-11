package bogdanmarkiewka.schedulingapp.meeting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface MeetingRepository extends JpaRepository<MeetingEntity, UUID> {

    List<MeetingEntity> findByTime(OffsetDateTime time);

    @Query("SELECT m FROM MeetingEntity m JOIN UserMeetingEntity um ON m.id = um.meetingEntity.id WHERE um.userEntity.id IN :userIds AND m.time BETWEEN :startTime AND :endTime")
    List<MeetingEntity> findMeetingsByUsersAndTimeRange(
            @Param("userIds") Set<UUID> userIds,
            @Param("startTime") OffsetDateTime startTime,
            @Param("endTime") OffsetDateTime endTime
    );

}
