package vn.giabaochatapp.giabaochatappserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.giabaochatapp.giabaochatappserver.data.dto.mix.MeetingIdDTO;
import vn.giabaochatapp.giabaochatappserver.data.dto.request.CreateMeetingRequest;
import vn.giabaochatapp.giabaochatappserver.data.dto.request.RateMeetingRequest;
import vn.giabaochatapp.giabaochatappserver.data.dto.shortName.MeetingDTO;
import vn.giabaochatapp.giabaochatappserver.data.dto.shortName.MeetingParticipantDTO;
import vn.giabaochatapp.giabaochatappserver.services.MeetingService;

@RestController
@RequestMapping(value = "/meeting")
public class MeetingController {

    @Autowired
    public MeetingService meetingService;

    @PostMapping("/create")
    public MeetingDTO createMeeting(@RequestBody CreateMeetingRequest request) {
        return meetingService.createMeeting(request);
    }

    @PostMapping("/cancel")
    public void cancelMeeting(@RequestBody MeetingIdDTO request) {
        meetingService.cancelMeeting(request);
    }

    @PostMapping("/join")
    public MeetingParticipantDTO joinMeeting(@RequestBody MeetingIdDTO request) {
        return meetingService.joinMeeting(request);
    }

    @PostMapping("/leave")
    public void leaveMeeting(@RequestBody MeetingIdDTO request) {
        meetingService.leaveMeeting(request);
    }

    @PostMapping("/rate")
    public MeetingDTO rateMeeting(@RequestBody RateMeetingRequest request) {
        return meetingService.rateMeeting(request);
    }
}
