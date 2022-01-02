package net.soryu.fund.repository;

import net.soryu.fund.entity.YearRate;
import net.soryu.fund.entity.YearRateIdentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YearRateRepo extends JpaRepository<YearRate, YearRateIdentity> {

    @Query(value = "select entity from YearRate entity where fundId=?1")
    List<YearRate> findAllById(String fundId);

}
