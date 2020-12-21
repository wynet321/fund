package net.cheetahead.fund.repository;

import net.cheetahead.fund.entity.CurrencyPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyPriceRepo extends JpaRepository<CurrencyPrice, String> {

}
