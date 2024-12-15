package vn.giabaochatapp.giabaochatappserver.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.giabaochatapp.giabaochatappserver.data.domains.UserChatRoom;

import java.util.List;

@Repository
public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long> {

    // Lấy danh sách UserChatRoom theo userId
    List<UserChatRoom> findByUserId(Long userId);

    // Lấy danh sách UserChatRoom theo chatRoomId
    List<UserChatRoom> findByChatRoomId(Long chatRoomId);

    // Kiểm tra xem user đã tham gia phòng chưa
    boolean existsByUserIdAndChatRoomId(Long userId, Long chatRoomId);

    // Xóa quan hệ giữa user và phòng
    void deleteByUserIdAndChatRoomId(Long userId, Long chatRoomId);
}

