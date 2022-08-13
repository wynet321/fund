package net.canglong.fund.repository;

import net.canglong.fund.entity.MonthRate;
import net.canglong.fund.entity.MonthRateIdentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonthRateRepo extends JpaRepository<MonthRate, MonthRateIdentity> {

    @Query(value = "select entity from MonthRate entity where fundId=?1")
    List<MonthRate> findAllById(String fundId);

}
