package vn.giabaochatapp.giabaochatappserver.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.giabaochatapp.giabaochatappserver.data.domains.RefreshToken;
import vn.giabaochatapp.giabaochatappserver.data.domains.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    @Modifying
    int deleteByUser(User user);

    @Query("SELECT t FROM RefreshToken t WHERE t.user.id = :id AND t.revoked = false")
    List<RefreshToken> findAllValidRefreshTokenByUser(Long id);
}
