package net.soryu.fund.repository;

import net.soryu.fund.entity.CurrencyFundPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyFundPriceRepo extends JpaRepository<CurrencyFundPrice, String> {

}
