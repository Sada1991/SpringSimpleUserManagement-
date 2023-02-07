package net.igap.repository;


import java.util.Optional;
import net.igap.model.entity.Province;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceRepository extends JpaRepository<Province,Long> {
  Optional<Province> findProvinceByEnableIsTrueAndProvinceName(String provinceName);
  Page<Province> findAllByEnableIsTrue(Pageable pageable);
  Optional<Province> findByEnableIsTrueAndId(Long provinceId);

}
