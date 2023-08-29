//package horse.boo.bot.database.controllers;
//
//import horse.boo.bot.database.dao.ConfigDAO;
//import horse.boo.bot.database.entity.Config;
//import net.dv8tion.jda.api.entities.Guild;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//
//@Controller
//@RequestMapping("/configs")
//public class ConfigController {
//    private final ConfigDAO configDAO;
//
//    @Autowired
//    public ConfigController(ConfigDAO configDAO) {
//        this.configDAO = configDAO;
//    }
//
//    @GetMapping()
//    public String index(Model model) {
//        model.addAttribute("config", configDAO.index());
//        return "configs/index";
//    }
//
//    @GetMapping("/{id}")
//    public String show(@PathVariable("id") long guild_id, Model model) {
//        model.addAttribute("config", configDAO.show(guild_id));
//        return "configs/show";
//    }
//
//    @GetMapping("/new")
//    public String newConfig(@ModelAttribute("config") Config config) {
//        return "configs/new";
//    }
//
//    @PostMapping()
//    public String create(@ModelAttribute("config") @Valid Config config,
//                         BindingResult bindingResult) {
//        if (bindingResult.hasErrors())
//            return "configs/new";
//
//        configDAO.save(config);
//        return "redirect:/configs";
//    }
//
//    @GetMapping("/{id}/edit")
//    public String edit(Model model, @PathVariable("id") long guild_id) {
//        model.addAttribute("locale", configDAO.show(guild_id));
//        return "configs/edit";
//    }
//
//    @PostMapping("/{id}")
//    public String update(@ModelAttribute("config") @Valid Config config, BindingResult bindingResult,
//                         @PathVariable("id") long guild_id) {  // убедиться в работоспособности
//        if (bindingResult.hasErrors())
//            return "configs/edit";
//
//        configDAO.update(guild_id, config);
//        return "redirect:/configs";
//    }
//
//    @DeleteMapping("/{id}")
//    public String delete(@PathVariable("id") Guild guild) {
//        configDAO.delete(guild.getIdLong());
//        return "redirect:/configs";
//    }
//}
