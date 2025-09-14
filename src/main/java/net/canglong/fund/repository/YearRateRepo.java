package net.canglong.fund.repository;

import java.util.List;
import net.canglong.fund.entity.YearRate;
import net.canglong.fund.entity.YearRateIdentity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface YearRateRepo extends JpaRepository<YearRate, YearRateIdentity> {

  @Query(value = "select entity from YearRate entity where fund_id=?1")
  List<YearRate> findAllById(String fundId);

  @Query(value = "select entity from YearRate entity where fund_id=?1 and year=?2")
  YearRate findByIdAndYear(String fundId, int year);

  @Query(value = "select entity from YearRate entity where name like %?1%")
  List<YearRate> findByNameContaining(String fundName);

  @Query(value = "select entity from YearRate entity where name like %?1% and year=?2")
  YearRate findByNameAndYear(String fundName, int year);

  @Query(value = "SELECT entity FROM YearRate entity where YEAR=:year and type IN :types ORDER BY rate DESC")
  Page<YearRate> findAllByTypesAndYear(List<String> types, int year, Pageable pageable);

  @Query(value = "SELECT r.yearRateIdentity.fundId, r.name, r.companyName, r.type, avg(r.rate) as average,  STDDEV(rate) AS stddev FROM YearRate r WHERE r.yearRateIdentity.year>=:year AND r.type IN :types group by r.yearRateIdentity.fundId order by average desc")
  Page<Object[]> findAverageRankByTypesAndYear(List<String> types, int year,
      Pageable pageable);
}
