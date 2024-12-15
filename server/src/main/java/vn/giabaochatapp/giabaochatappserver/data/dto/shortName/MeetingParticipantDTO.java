package vn.giabaochatapp.giabaochatappserver.data.dto.shortName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.giabaochatapp.giabaochatappserver.data.domains.MeetingParticipant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class MeetingParticipantDTO {

    public Long id;
    public Long meetingId;
    public Long hostId;
    public Long participantId;

    public MeetingParticipantDTO (MeetingParticipant meetingParticipant) {
        this.id = meetingParticipant.getId();
        this.meetingId = meetingParticipant.getMeetingId();
        this.hostId = meetingParticipant.getHostId();
        this.participantId = meetingParticipant.getParticipantId();
    }

}
