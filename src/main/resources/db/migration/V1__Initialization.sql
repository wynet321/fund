CREATE TABLE `company` (
  `id` char(8) NOT NULL,
  `name` varchar(40) NOT NULL,
  `abbr` varchar(20) NOT NULL,
  `created_on` date NOT NULL,
  `address` varchar(10) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `currency_price` (
  `fund_id` char(6) NOT NULL,
  `date` date NOT NULL,
  `return_of_ten_kilo` decimal(8,4) NOT NULL,
  `seven_day_annualized_rate_of_return` decimal(8,4) NOT NULL,
  PRIMARY KEY (`fund_id`,`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `fund` (
  `id` char(6) NOT NULL,
  `parent_id` char(6) DEFAULT NULL,
  `name` varchar(40) NOT NULL,
  `company_id` char(8) NOT NULL,
  `type` varchar(10) NOT NULL,
  `current_page` int(10) unsigned NOT NULL DEFAULT '0',
  `average_rate_by_month` decimal(6,4) DEFAULT NULL,
  `average_rate_by_year` decimal(6,4) DEFAULT NULL,
  `total_rate` decimal(7,4) DEFAULT NULL,
  `statistic_due_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `fund_price` (
  `fund_id` char(6) NOT NULL,
  `date` date NOT NULL,
  `price` decimal(8,4) NOT NULL,
  `accumulated_price` decimal(8,4) NOT NULL,
  PRIMARY KEY (`fund_id`,`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `fund_return_rate_by_month` (
  `fund_id` char(6) NOT NULL,
  `month` tinyint(6) unsigned NOT NULL,
  `rate` decimal(6,4) NOT NULL,
  PRIMARY KEY (`fund_id`,`month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `fund_return_rate_by_year` (
  `fund_id` char(6) NOT NULL,
  `year` tinyint(4) unsigned NOT NULL,
  `rate` decimal(6,4) NOT NULL,
  PRIMARY KEY (`fund_id`,`year`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;