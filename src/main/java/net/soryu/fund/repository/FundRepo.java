package net.soryu.fund.repository;

import net.soryu.fund.entity.Fund;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FundRepo extends JpaRepository<Fund, String> {

    @Query("select entity from Fund entity where company_id=?1")
    public Page<Fund> findAllByCompanyId(String companyId, Pageable pageable);

    public Fund findByName(String name);
}
