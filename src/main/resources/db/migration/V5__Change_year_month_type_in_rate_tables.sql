ALTER TABLE `fund_return_rate_by_month`
	CHANGE COLUMN `year` `year` SMALLINT(4) UNSIGNED NOT NULL;

ALTER TABLE `fund_return_rate_by_year`
	CHANGE COLUMN `year` `year` SMALLINT(4) UNSIGNED NOT NULL;