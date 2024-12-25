package vn.giabaochatapp.giabaochatappserver.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.giabaochatapp.giabaochatappserver.data.domains.UserChatRoom;

import java.util.List;

@Repository
public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long> {

    List<UserChatRoom> findByUserId(Long userId);

    boolean existsByUserIdAndChatRoomId(Long userId, Long chatRoomId);

    void deleteByUserIdAndChatRoomId(Long userId, Long chatRoomId);
}
