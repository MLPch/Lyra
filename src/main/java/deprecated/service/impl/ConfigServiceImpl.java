//package horse.boo.bot.database.service.impl;
//
//import horse.boo.bot.database.entity.Config;
//import horse.boo.bot.database.repository.ConfigRepository;
//import horse.boo.bot.database.service.ConfigService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class ConfigServiceImpl implements ConfigService {
//
//    @Autowired
//    private ConfigRepository configRepository;
//
//    @Override
//    public Config addConfig(Config config) {
//        Config savedConfig = configRepository.saveAndFlush(config);
//        return savedConfig;
//    }
//
//    @Override
//    public void delete(long id) {
//        configRepository.delete(id);
//    }
//
//    @Override
//    public Config getByGuildId(long guildId) {
//        return configRepository.findByGuildId(guildId);
//    }
//
//    @Override
//    public Config editConfig(Config config) {
//        return configRepository.saveAndFlush(config);
//    }
//
//    @Override
//    public List<Config> getAll() {
//        return configRepository.findAll();
//    }
//}
