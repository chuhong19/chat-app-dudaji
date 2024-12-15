package vn.giabaochatapp.giabaochatappserver.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.giabaochatapp.giabaochatappserver.data.domains.MeetingRating;

import java.util.Optional;

@Repository
public interface MeetingRatingRepository extends JpaRepository<MeetingRating, Long> {
    Optional<MeetingRating> findByMeetingIdAndUserId(Long meetingId, Long userId);
}
