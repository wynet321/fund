ALTER TABLE `fund_return_rate_by_period`
	MODIFY `one_year_rate` decimal(6,4) NOT NULL,
    MODIFY `two_year_rate` decimal(6,4)  NOT NULL,
    MODIFY `three_year_rate` decimal(6,4)  NOT NULL,
    MODIFY `four_year_rate` decimal(6,4)  NOT NULL,
    MODIFY `five_year_rate` decimal(6,4)  NOT NULL,
    MODIFY `six_year_rate` decimal(6,4)  NOT NULL,
    MODIFY `seven_year_rate` decimal(6,4)  NOT NULL,
    MODIFY `eight_year_rate` decimal(6,4)  NOT NULL,
    MODIFY `nine_year_rate` decimal(6,4)  NOT NULL,
    MODIFY `ten_year_rate` decimal(6,4)  NOT NULL;