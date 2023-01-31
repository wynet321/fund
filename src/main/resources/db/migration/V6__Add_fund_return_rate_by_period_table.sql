CREATE TABLE `fund_return_rate_by_period` (
  `id` char(6) NOT NULL,
  `name` varchar(40) NOT NULL,
  `company_name` varchar(40) NOT NULL,
  `type` varchar(10) NOT NULL,
  `one_year_rate` decimal(6,4) ZEROFILL NOT NULL,
  `two_year_rate` decimal(6,4) ZEROFILL NOT NULL,
  `three_year_rate` decimal(6,4) ZEROFILL NOT NULL,
  `four_year_rate` decimal(6,4) ZEROFILL NOT NULL,
  `five_year_rate` decimal(6,4) ZEROFILL NOT NULL,
  `six_year_rate` decimal(6,4) ZEROFILL NOT NULL,
  `seven_year_rate` decimal(6,4) ZEROFILL NOT NULL,
  `eight_year_rate` decimal(6,4) ZEROFILL NOT NULL,
  `nine_year_rate` decimal(6,4) ZEROFILL NOT NULL,
  `ten_year_rate` decimal(6,4) ZEROFILL NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;