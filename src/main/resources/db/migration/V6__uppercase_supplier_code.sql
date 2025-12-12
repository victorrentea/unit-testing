-- Datafix: uppercase all existing supplier codes
-- Null-safe and idempotent (running multiple times keeps codes uppercase)
UPDATE supplier
SET code = UPPER(code)
WHERE code IS NOT NULL AND code <> UPPER(code);
