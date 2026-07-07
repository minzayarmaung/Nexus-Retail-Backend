-- Insert core offices
INSERT INTO office (name, opening_date)
SELECT 'HEAD_OFFICE', '1970-01-01'
WHERE NOT EXISTS (SELECT 1 FROM office WHERE name = 'HEAD_OFFICE');

-- Ensure hierarchy is populated for any newly-created offices
UPDATE office SET hierarchy = '.' || id || '.' WHERE hierarchy IS NULL;
