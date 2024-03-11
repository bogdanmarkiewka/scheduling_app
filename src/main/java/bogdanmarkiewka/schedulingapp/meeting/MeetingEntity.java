package bogdanmarkiewka.schedulingapp.meeting;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "meetings")
public class MeetingEntity {

    static final int DEFAULT_MEETING_DURATION_IN_HOURS = 1;

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(nullable = false)
    private OffsetDateTime time;

    public UUID getId() {
        return id;
    }

    public OffsetDateTime getTime() {
        return time;
    }

    public void setTime(OffsetDateTime time) {
        this.time = time;
    }

    public OffsetDateTime getEndTime() {
        return this.time.plusHours(DEFAULT_MEETING_DURATION_IN_HOURS);
    }

}
