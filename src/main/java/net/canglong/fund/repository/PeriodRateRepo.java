package net.canglong.fund.repository;

import net.canglong.fund.entity.PeriodRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PeriodRateRepo extends JpaRepository<PeriodRate, String> {

    @Query(value = "select entity from PeriodRate entity where type in :types order by one_year_rate desc")
    public Page<PeriodRate> getPeriodRateOrderByOneYearRateDesc(List<String> types, Pageable pageable);

    @Query(value = "select entity from PeriodRate entity where type in :types order by three_year_rate desc")
    public Page<PeriodRate> getPeriodRateOrderByThreeYearRateDesc(List<String> types, Pageable pageable);

    @Query(value = "select entity from PeriodRate entity where type in :types order by five_year_rate desc")
    public Page<PeriodRate> getPeriodRateOrderByFiveYearRateDesc(List<String> types, Pageable pageable);

    @Query(value = "select entity from PeriodRate entity where type in :types order by eight_year_rate desc")
    public Page<PeriodRate> getPeriodRateOrderByEightYearRateDesc(List<String> types, Pageable pageable);

    @Query(value = "select entity from PeriodRate entity where type in :types order by ten_year_rate desc")
    public Page<PeriodRate> getPeriodRateOrderByTenYearRateDesc(List<String> types, Pageable pageable);
}
