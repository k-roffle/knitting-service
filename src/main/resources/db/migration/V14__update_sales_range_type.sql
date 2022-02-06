ALTER TABLE product DROP COLUMN IF EXISTS specified_sales_start_date;
ALTER TABLE product DROP COLUMN IF EXISTS specified_sales_end_date;
ALTER TABLE product ADD COLUMN specified_sales_started_at TIMESTAMP WITH TIME ZONE;
ALTER TABLE product ADD COLUMN specified_sales_ended_at TIMESTAMP WITH TIME ZONE;
