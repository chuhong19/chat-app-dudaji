package vn.giabaochatapp.giabaochatappserver.data.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.giabaochatapp.giabaochatappserver.data.domains.Permission;

import java.util.Optional;

@Repository
public interface PermissionRepository extends CrudRepository<Permission, Long> {
    Optional<Permission> findByPermission(String permission);

    @Query(value = "select count(*) from permissions_roles where permission_id = ?1", nativeQuery = true)
    Long countPermissionUsage(Long permissionId);

    void deleteByPermission(String permission);
}
