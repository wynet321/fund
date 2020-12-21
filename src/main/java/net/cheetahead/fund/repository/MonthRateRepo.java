package net.cheetahead.fund.repository;

import net.cheetahead.fund.entity.MonthRate;
import net.cheetahead.fund.entity.MonthRateIdentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonthRateRepo extends JpaRepository<MonthRate, MonthRateIdentity> {

    @Query(value = "select entity from MonthRate entity where fundId=?1")
    List<MonthRate> findAllById(String fundId);

}
