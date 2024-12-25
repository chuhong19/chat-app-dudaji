package vn.giabaochatapp.giabaochatappserver.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.giabaochatapp.giabaochatappserver.data.domains.PostReport;

import java.util.List;

@Repository
public interface PostReportRepository extends JpaRepository<PostReport, Long> {

    @Query("SELECT pr.postId FROM PostReport pr")
    List<Long> findAllPostIds();

    boolean existsByPostIdAndReportUserId(Long postId, Long userId);
}
