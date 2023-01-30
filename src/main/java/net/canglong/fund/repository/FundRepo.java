package net.canglong.fund.repository;

import net.canglong.fund.entity.Fund;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FundRepo extends JpaRepository<Fund, String> {

    @Query("select entity from Fund entity where company_id=?1")
    Page<Fund> findAllByCompanyId(String companyId, Pageable pageable);

    Fund findByName(String name);

    @Query("select entity from Fund entity where type=?1")
    List<Fund> findAllByType(String type);

    @Query("select entity from Fund entity where type<>?1")
    List<Fund> findAllExcludesType(String type);
}
