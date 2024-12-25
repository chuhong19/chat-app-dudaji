package vn.giabaochatapp.giabaochatappserver.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.giabaochatapp.giabaochatappserver.data.domains.File;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

}
