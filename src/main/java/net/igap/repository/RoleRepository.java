package net.igap.repository;

import java.util.Optional;
import net.igap.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
  Optional<Role> findByName(String roleName);
}
