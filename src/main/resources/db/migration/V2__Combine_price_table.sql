ALTER TABLE `fund_price` 
	ADD COLUMN `return_of_ten_kilo` DECIMAL(8,4) NULL AFTER `accumulated_price`,
	ADD COLUMN `seven_day_annualized_rate_of_return` DECIMAL(8,4)  NULL AFTER `return_of_ten_kilo`,
	CHANGE COLUMN `price` `price` DECIMAL(8,4) NULL AFTER `date`,
	CHANGE COLUMN `accumulated_price` `accumulated_price` DECIMAL(8,4) NULL AFTER `price`;