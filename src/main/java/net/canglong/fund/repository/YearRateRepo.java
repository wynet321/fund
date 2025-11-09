package net.canglong.fund.repository;

import java.util.List;
import net.canglong.fund.entity.YearRate;
import net.canglong.fund.entity.YearRateIdentity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface YearRateRepo extends JpaRepository<YearRate, YearRateIdentity> {

  @Query(value = "select entity from YearRate entity where entity.yearRateIdentity.fundId = ?1")
  List<YearRate> findAllById(String fundId);

  @Query(value = "select entity from YearRate entity where entity.yearRateIdentity.fundId = ?1 and entity.yearRateIdentity.year = ?2")
  YearRate findByIdAndYear(String fundId, int year);

  @Query(value = "select entity from YearRate entity where name like %?1%")
  List<YearRate> findByNameContaining(String fundName);

  @Query(value = "select entity from YearRate entity where name like %?1% and entity.yearRateIdentity.year = ?2")
  YearRate findByNameAndYear(String fundName, int year);

  @Query(value = "SELECT entity FROM YearRate entity where entity.yearRateIdentity.year = :year and entity.type IN :types ORDER BY entity.rate DESC")
  Page<YearRate> findAllByTypesAndYear(@Param("types") List<String> types, @Param("year") int year, Pageable pageable);

  @Query(value = "SELECT r.yearRateIdentity.fundId, r.name, r.companyName, r.type, avg(r.rate) as average,  STDDEV(r.rate) AS stddev FROM YearRate r WHERE r.yearRateIdentity.year>=:year AND r.type IN :types group by r.yearRateIdentity.fundId, r.name, r.companyName, r.type order by average desc")
  Page<Object[]> findAverageRankByTypesAndYear(@Param("types") List<String> types, @Param("year") int year,
      Pageable pageable);
      
  @Modifying
  @Transactional
  @Query("delete from YearRate y where y.yearRateIdentity.fundId = :fundId")
  void deleteByFundId(@Param("fundId") String fundId);
}
