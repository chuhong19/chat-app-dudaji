package vn.giabaochatapp.giabaochatappserver.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.giabaochatapp.giabaochatappserver.data.domains.ChatRoom;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // Kiểm tra phòng có tồn tại dựa trên ID
    boolean existsById(Long id);

    // Tìm kiếm danh sách phòng theo tên (tùy chọn, nếu cần tính năng search)
    List<ChatRoom> findByRoomNameContaining(String roomName);
}

