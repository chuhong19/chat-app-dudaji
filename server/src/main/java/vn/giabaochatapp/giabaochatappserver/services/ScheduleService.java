package vn.giabaochatapp.giabaochatappserver.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.giabaochatapp.giabaochatappserver.data.domains.Meeting;
import vn.giabaochatapp.giabaochatappserver.data.enums.MeetingStatus;
import vn.giabaochatapp.giabaochatappserver.data.repository.MeetingRepository;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ScheduleService {

    @Autowired
    public MeetingRepository meetingRepository;

    @Scheduled(fixedRate = 60000)
    public void handleStartMeeting(){
        Date currentTime = new Date();
        List<Meeting> meetings = meetingRepository.findAll();
        for (Meeting meeting : meetings) {
            // Check status SCHEDULED to ONGOING
            if (meeting.getStatus() == MeetingStatus.SCHEDULED && meeting.getTimeStart().before(currentTime)) {
                meeting.setStatus(MeetingStatus.ONGOING);
                meetingRepository.save(meeting);
            }
            // Check status ONGOING to COMPLETED
            else if (meeting.getStatus() == MeetingStatus.ONGOING && meeting.getTimeStart().getTime() + meeting.getDuration() * 60000 < currentTime.getTime()) {
                meeting.setStatus(MeetingStatus.COMPLETED);
                meetingRepository.save(meeting);
            }
        }
    }

}
