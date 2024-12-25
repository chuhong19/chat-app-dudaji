package vn.giabaochatapp.giabaochatappserver.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.giabaochatapp.giabaochatappserver.data.domains.ChatRoom;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    boolean existsById(Long id);

}

