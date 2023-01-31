package net.canglong.fund.repository;

import net.canglong.fund.entity.PeriodRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PeriodRateRepo extends JpaRepository<PeriodRate, String> {

    @Query(value = "select entity from PeriodRate entity where type=:type order by one_year_rate desc")
    public Page<PeriodRate> getPeriodRateOrderByOneYearRateDesc(String type, Pageable pageable);

    @Query(value = "select entity from PeriodRate entity where type=:type order by three_year_rate desc")
    public Page<PeriodRate> getPeriodRateOrderByThreeYearRateDesc(String type, Pageable pageable);

    @Query(value = "select entity from PeriodRate entity where type=:type order by five_year_rate desc")
    public Page<PeriodRate> getPeriodRateOrderByFiveYearRateDesc(String type, Pageable pageable);

    @Query(value = "select entity from PeriodRate entity where type=:type order by eight_year_rate desc")
    public Page<PeriodRate> getPeriodRateOrderByEightYearRateDesc(String type, Pageable pageable);

    @Query(value = "select entity from PeriodRate entity where type=:type order by ten_year_rate desc")
    public Page<PeriodRate> getPeriodRateOrderByTenYearRateDesc(String type, Pageable pageable);
}
