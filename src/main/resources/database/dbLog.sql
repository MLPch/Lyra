--initial create--

CREATE TABLE IF NOT EXISTS locale
(
    id                  SERIAL PRIMARY KEY NOT NULL ,
    guild_id            BIGINT,
    locale_name         TEXT NOT NULL,
    locale_en           TEXT NOT NULL,
    locale_ru           TEXT NOT NULL,
    locale_ua           TEXT,
    locale_cn           TEXT
);


CREATE TABLE IF NOT EXISTS config
(
    id                          bigint not null
                                primary key,
    guild_id                    bigint,
    admin_channel_id            bigint,
    bot_id                      bigint,
    bot_language                text,
    unrelated_emote_count       integer,
    unrelated_emote_id          bigint,
    welcome_channel_id          bigint,
    goodbye_channel_id          bigint,
    log_channel_id              bigint,
    bot_readiness_channel_id    bigint,
    unrelated_delete_time_sec   integer,
    function_music_player       boolean,
    function_role_saver         boolean,
    function_dice_roller        boolean,
    function_unrelated_deleter  boolean
);

-- ??? --