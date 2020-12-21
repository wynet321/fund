package net.cheetahead.fund.repository;

import net.cheetahead.fund.entity.Fund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FundRepo extends JpaRepository<Fund, String> {

    @Query("select entity from Fund entity where company_id=?1")
    public List<Fund> findAllByCompanyId(String companyId);

}
