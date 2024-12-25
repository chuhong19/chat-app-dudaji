package vn.giabaochatapp.giabaochatappserver.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.giabaochatapp.giabaochatappserver.data.domains.Post;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    @Override
    Optional<Post> findById(Long id);

}
