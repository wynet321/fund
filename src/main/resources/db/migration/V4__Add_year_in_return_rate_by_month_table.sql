ALTER TABLE `fund_return_rate_by_month`
    DROP PRIMARY KEY;

ALTER TABLE `fund_return_rate_by_month`
	ADD COLUMN `year` TINYINT(4) UNSIGNED NOT NULL AFTER `fund_id`;

ALTER TABLE `fund_return_rate_by_month`
	CHANGE COLUMN `month` `month` TINYINT(2) UNSIGNED NOT NULL;

ALTER TABLE `fund_return_rate_by_month`
	ADD PRIMARY KEY (`fund_id`,`year`, `month`);