-- Portfolio CV prompt mode: cv (recruiter-style) vs archive (reflective self-assessment).
-- Apply manually or via migration tool; existing rows get 'cv'.

ALTER TABLE _sw_mileage_portfolio_cv
    ADD COLUMN mode VARCHAR(16) NOT NULL DEFAULT 'cv'
        COMMENT 'cv | archive';
