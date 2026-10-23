-- Migration script to update Company-User relationship
-- This script ensures the database schema matches the new entity relationship

-- Check and add status column to Companies table if it doesn't exist
IF NOT EXISTS (
    SELECT * FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_NAME = 'Companies' AND COLUMN_NAME = 'status'
)
BEGIN
    ALTER TABLE Companies
    ADD status VARCHAR(20) DEFAULT 'PENDING';
END

-- Check and add user_id column to Companies table if it doesn't exist
IF NOT EXISTS (
    SELECT * FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_NAME = 'Companies' AND COLUMN_NAME = 'user_id'
)
BEGIN
    ALTER TABLE Companies
    ADD user_id BIGINT;

    -- Add foreign key constraint
    ALTER TABLE Companies
    ADD CONSTRAINT fk_company_user
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
    ON DELETE SET NULL;
END

-- Ensure user_id column has proper foreign key constraint
-- (This will recreate the constraint if it was missing)
BEGIN TRY
    ALTER TABLE Companies DROP CONSTRAINT fk_company_user;
END TRY
BEGIN CATCH
    -- Ignore error if constraint doesn't exist
END CATCH

ALTER TABLE Companies
ADD CONSTRAINT fk_company_user
FOREIGN KEY (user_id) REFERENCES Users(user_id)
ON DELETE SET NULL;

-- Update existing companies to have PENDING status if status is NULL
UPDATE Companies
SET status = 'PENDING'
WHERE status IS NULL;
