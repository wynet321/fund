ALTER TABLE `fund`
  ADD COLUMN `start_date` DATE DEFAULT NULL COLLATE 'utf8mb4_general_ci' AFTER `statistic_due_date`,
  ADD COLUMN `end_date` DATE DEFAULT NULL COLLATE 'utf8mb4_general_ci' AFTER `start_date`;