package bogdanmarkiewka.schedulingapp.user_meeting;

import bogdanmarkiewka.schedulingapp.meeting.MeetingEntity;
import bogdanmarkiewka.schedulingapp.user.UserEntity;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "user_meeting")
public class UserMeetingEntity {

    @EmbeddedId
    private UserMeetingId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne
    @MapsId("meetingId")
    @JoinColumn(name = "meeting_id")
    private MeetingEntity meetingEntity;

    public UserMeetingEntity() {
    }

    public UserMeetingEntity(UserEntity userEntity, MeetingEntity meetingEntity) {
        this.userEntity = userEntity;
        this.meetingEntity = meetingEntity;
        this.id = new UserMeetingEntity.UserMeetingId(userEntity.getId(), meetingEntity.getId());

    }

    @Embeddable
    public static class UserMeetingId implements Serializable {
        private UUID userId;
        private UUID meetingId;

        public UserMeetingId() {
        }

        public UserMeetingId(UUID userEntityId, UUID meetingEntityId) {
            this.userId = userEntityId;
            this.meetingId = meetingEntityId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserMeetingId that = (UserMeetingId) o;
            return Objects.equals(userId, that.userId) &&
                    Objects.equals(meetingId, that.meetingId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, meetingId);
        }

    }

}
