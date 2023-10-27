package horse.boo.bot.database.repository;

import horse.boo.bot.database.table.ConfigsTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ConfigRepositoryTest extends ConfigsTable {

    @Autowired
    private ConfigRepository configRepository;

    protected Long TEST_ID = 1L;
    protected Long TEST_GUILD_ID = 111111111111111111L;
    protected Long TEST_ADMIN_CHANNEL_ID = 111111111111111111L;
    protected Long TEST_BOT_ID = 111111111111111111L;
    protected String TEST_BOT_LANGUAGE = "english";
    protected Integer TEST_UNRELATED_EMOTE_COUNT = 1;
    protected Long TEST_UNRELATED_EMOTE_ID = 111111111111111111L;
    protected Long TEST_WELCOME_CHANNEL_ID = 111111111111111111L;
    protected Long TEST_GOODBYE_CHANNEL_ID = 111111111111111111L;
    protected Long TEST_LOG_CHANNEL_ID = 111111111111111111L;
    protected Long TEST_BOT_READINESS_CHANNEL_ID = 111111111111111111L;
    protected Integer TEST_UNRELATED_DELETE_TIME_SEC = 1;
    protected boolean TEST_FUNCTION_MUSIC_PLAYER = true;
    protected boolean TEST_FUNCTION_ROLE_SAVER = true;
    protected boolean TEST_FUNCTION_DICE_ROLLER = true;
    protected boolean TEST_FUNCTION_UNRELATED_DELETER = true;


    @Test
    void deleteByGuildId() {

    }

    @Test
    void existsByGuildId() {
        var actual = configRepository.getConfigByGuildId(TEST_GUILD_ID);
        Assertions.assertNotNull(actual);
    }

    @Test
    void findByGuildId() {
        var actual = configRepository.getConfigByGuildId(TEST_GUILD_ID);
        Assertions.assertNotNull(actual);
    }


    @Test
    void getConfigByGuildId() {
        var actual = configRepository.getConfigByGuildId(TEST_GUILD_ID);

        var expected = new ConfigsTable();
        expected.setId(TEST_ID);
        expected.setGuildId(TEST_GUILD_ID);
        expected.setAdminChannelId(TEST_ADMIN_CHANNEL_ID);
        expected.setBotId(TEST_BOT_ID);
        expected.setBotLanguage(TEST_BOT_LANGUAGE);
        expected.setUnrelatedEmoteCount(TEST_UNRELATED_EMOTE_COUNT);
        expected.setUnrelatedEmoteId(TEST_UNRELATED_EMOTE_ID);
        expected.setWelcomeChannelId(TEST_WELCOME_CHANNEL_ID);
        expected.setGoodbyeChannelId(TEST_GOODBYE_CHANNEL_ID);
        expected.setLogChannelId(TEST_LOG_CHANNEL_ID);
        expected.setBotInfoChannelId(TEST_BOT_READINESS_CHANNEL_ID);
        expected.setUnrelatedDeleteTimeSec(TEST_UNRELATED_DELETE_TIME_SEC);
        expected.setFunctionMusicPlayer(TEST_FUNCTION_MUSIC_PLAYER);
        expected.setFunctionRememberingRoles(TEST_FUNCTION_ROLE_SAVER);
        expected.setFunctionDiceRoller(TEST_FUNCTION_DICE_ROLLER);
        expected.setFunctionUnrelatedDeleter(TEST_FUNCTION_UNRELATED_DELETER);

        Assertions.assertEquals(expected.toString(), actual.toString());
    }
}