ALTER TABLE `fund_return_rate_by_month`
  ADD COLUMN `name` VARCHAR(40) NOT NULL COLLATE 'utf8mb4_general_ci' AFTER `month`,
  ADD COLUMN `company_name` VARCHAR(40) NOT NULL COLLATE 'utf8mb4_general_ci' AFTER `name`,
  ADD COLUMN `type` VARCHAR(10) NOT NULL COLLATE 'utf8mb4_general_ci' AFTER `company_name`;

ALTER TABLE `fund_return_rate_by_year`
  ADD COLUMN `name` VARCHAR(40) NOT NULL COLLATE 'utf8mb4_general_ci' AFTER `year`,
  ADD COLUMN `company_name` VARCHAR(40) NOT NULL COLLATE 'utf8mb4_general_ci' AFTER `name`,
  ADD COLUMN `type` VARCHAR(10) NOT NULL COLLATE 'utf8mb4_general_ci' AFTER `company_name`;