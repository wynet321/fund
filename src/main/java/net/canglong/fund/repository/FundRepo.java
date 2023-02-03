package net.canglong.fund.repository;

import java.util.List;
import net.canglong.fund.entity.Fund;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FundRepo extends JpaRepository<Fund, String> {

  @Query("select entity from Fund entity where company_id=?1")
  Page<Fund> findAllByCompanyId(String companyId, Pageable pageable);

  Fund findByName(String name);

  @Query("select entity from Fund entity where type=?1")
  List<Fund> findAllByType(String type);

  @Query("select entity from Fund entity where type<>?1")
  List<Fund> findAllExcludesType(String type);

  @Query("select entity from Fund entity where type in ?1")
  List<Fund> findAllByTypes(List<String> types);

  @Query(nativeQuery = true, value = "select distinct type from fund")
  List<String> findAllTypes();
}
