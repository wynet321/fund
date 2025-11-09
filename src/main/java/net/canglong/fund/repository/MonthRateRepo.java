package net.canglong.fund.repository;

import java.util.List;
import net.canglong.fund.entity.MonthRate;
import net.canglong.fund.entity.MonthRateIdentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MonthRateRepo extends JpaRepository<MonthRate, MonthRateIdentity> {

  @Query("select m from MonthRate m where m.monthRateIdentity.fundId = :fundId and m.monthRateIdentity.year = :year order by m.monthRateIdentity.month asc")
  List<MonthRate> findAllById(@Param("fundId") String fundId, @Param("year") int year);
  
  @Modifying
  @Transactional
  @Query("delete from MonthRate m where m.monthRateIdentity.fundId = :fundId")
  void deleteByFundId(@Param("fundId") String fundId);
}
