package net.canglong.fund.repository;

import java.util.List;
import net.canglong.fund.entity.MonthRate;
import net.canglong.fund.entity.MonthRateIdentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthRateRepo extends JpaRepository<MonthRate, MonthRateIdentity> {

  @Query(value = "select entity from MonthRate entity where fund_id=:fundId and year=:year order by month asc")
  List<MonthRate> findAllById(String fundId, int year);

}
