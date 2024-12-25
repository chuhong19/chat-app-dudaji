package vn.giabaochatapp.giabaochatappserver.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.giabaochatapp.giabaochatappserver.data.domains.RequestJoinRoom;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestJoinRoomRepository extends JpaRepository<RequestJoinRoom, Long> {

    boolean existsByUserIdAndChatRoomId(Long userId, Long chatRoomId);

    List<RequestJoinRoom> findByUserId(Long userId);

    Optional<RequestJoinRoom> findByUserIdAndChatRoomId(Long userId, Long chatRoomId);

}
