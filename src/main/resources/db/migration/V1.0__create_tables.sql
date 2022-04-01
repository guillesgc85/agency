-- CREATE advertisement table
DROP TABLE IF EXISTS public.advertisement;
CREATE TABLE public.advertisement(
    id                      UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    dealer_id               UUID,
    vehicle                 VARCHAR,
    price                   VARCHAR,
    state                   VARCHAR NOT NULL DEFAULT 'DRAFT',
    created_at              TIMESTAMP NOT NULL DEFAULT now(),
    updated_at              TIMESTAMP
);
-- CREATE INDEXES
CREATE INDEX IF NOT EXISTS ads_dealer_id_idx ON public.advertisement(dealer_id);
CREATE INDEX IF NOT EXISTS ads_dealer_id_and_state_idx ON public.advertisement(dealer_id, state);
CREATE INDEX IF NOT EXISTS ads_created_at_idx ON public.advertisement(created_at);
CREATE INDEX IF NOT EXISTS ads_updated_at_idx ON public.advertisement(updated_at);

-- CREATE dealer table
DROP TABLE IF EXISTS public.dealer;
CREATE TABLE public.dealer(
    id                  UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name                VARCHAR NOT NULL,
    ads_limit           NUMERIC(18, 2) NOT NULL DEFAULT 0 CHECK (ads_limit >= 0),
    handler_limit       VARCHAR DEFAULT 'ERROR_MESSAGE',
    created_at          TIMESTAMP NOT NULL DEFAULT now(),
    updated_at          TIMESTAMP
);

-- CREATE INDEXES
CREATE INDEX IF NOT EXISTS dealer_created_at_idx ON public.advertisement(created_at);
CREATE INDEX IF NOT EXISTS dealer_updated_at_idx ON public.advertisement(updated_at);
