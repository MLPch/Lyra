CREATE TABLE IF NOT EXISTS lyra_locale
(
    id          SERIAL PRIMARY KEY NOT NULL ,
    locale_name TEXT NOT NULL,
    locale_en   TEXT NOT NULL,
    locale_ru   TEXT NOT NULL,
    locale_ua   TEXT,
    locale_cn   TEXT
);