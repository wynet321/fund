package net.canglong.fund.repository;

import java.util.List;
import net.canglong.fund.entity.PeriodRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeriodRateRepo extends JpaRepository<PeriodRate, String> {

  Page<PeriodRate> findByType(String type, Pageable pageable);

  List<PeriodRate> findByNameContaining(String fundName);
}
