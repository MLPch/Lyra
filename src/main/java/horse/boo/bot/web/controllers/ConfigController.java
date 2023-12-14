package horse.boo.bot.web.controllers;


import horse.boo.bot.database.repository.ConfigRepository;
import horse.boo.bot.database.table.ConfigsTable;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Controller
@RequestMapping("/configs")
public class ConfigController {

    private final ConfigRepository configRepository;

    @Autowired
    public ConfigController(ConfigRepository configRepository) {
        this.configRepository = configRepository;

    }

    @GetMapping()
    public String index(@NotNull Model model) {
        model.addAttribute("all_configs", configRepository.findAllConfigs());
        return "configs/index";
    }

    @GetMapping("/{guild_id}")
    public String show(@PathVariable("guild_id") long guild_id, @NotNull Model model) {
        model.addAttribute("config", configRepository.getConfigByGuildId(guild_id));
        return "configs/show";
    }

    @GetMapping("/new")
    public String newConfig(Model model) {
        model.addAttribute("newConfig", new ConfigsTable());
        return "configs/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("config") ConfigsTable config,
                         @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "configs/new";
        configRepository.save(config);
        return "redirect:/configs";
    }

    @GetMapping("/{guild_id}/edit")
    public String edit(@NotNull Model model, @PathVariable("guild_id") long guild_id) {
        model.addAttribute("config", configRepository.getConfigByGuildId(guild_id));
        return "configs/edit";
    }

    @PostMapping("/{guild_id}")
    public String update(@ModelAttribute("config") ConfigsTable config, @NotNull BindingResult bindingResult,
                         @PathVariable("guild_id") long guild_id) {
        if (bindingResult.hasErrors()) return "configs/edit";

        Optional<ConfigsTable> tableEntity = configRepository.findByGuildId(guild_id);

        if (tableEntity.isPresent()) {
            ConfigsTable configEntity = tableEntity.get();
            configEntity.setGuildId(config.getGuildId());
            configEntity.setGuildName(config.getGuildName());
            configEntity.setAdminChannelId(config.getAdminChannelId());
            configEntity.setBotId(configEntity.getBotId());
            configEntity.setBotLanguage(config.getBotLanguage());
            configEntity.setUnrelatedEmoteCount(config.getUnrelatedEmoteCount());
            configEntity.setUnrelatedEmoteId(config.getUnrelatedEmoteId());
            configEntity.setWelcomeChannelId(config.getWelcomeChannelId());
            configEntity.setGoodbyeChannelId(config.getGoodbyeChannelId());
            configEntity.setLogChannelId(config.getLogChannelId());
            configEntity.setBotInfoChannelId(config.getBotInfoChannelId());
            configEntity.setUnrelatedDeleteTimeSec(config.getUnrelatedDeleteTimeSec());
            configEntity.setFunctionMusicPlayer(config.isFunctionMusicPlayer());
            configEntity.setFunctionRememberingRoles(config.isFunctionRememberingRoles());
            configEntity.setFunctionDiceRoller(config.isFunctionDiceRoller());
            configEntity.setFunctionUnrelatedDeleter(config.isFunctionUnrelatedDeleter());

            configRepository.save(configEntity);
        }
        return "redirect:/configs";
    }

    @PostMapping("/del/{guild_id}")
    public String delete(@PathVariable("guild_id") long guild_id) {
        configRepository.removeByGuildId(guild_id);
        return "redirect:/configs";
    }
}
