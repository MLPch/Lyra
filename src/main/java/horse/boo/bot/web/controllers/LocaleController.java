//package horse.boo.bot.web.controllers;
//
//import horse.boo.bot.database.repository.LocaleRepository;
//import horse.boo.bot.database.table.LocalesTable;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Optional;
//
//@Controller
//@RequestMapping("/locales")
//public class LocaleController {
//
//
//
//    private final LocaleRepository localeRepository;
//
//    @Autowired
//    public LocaleController(LocaleRepository localeRepository) {
//        this.localeRepository = localeRepository;
//    }
//
//    @GetMapping()
//    public String index(@NotNull Model model) {
//        model.addAttribute("locale", localeRepository.index());
//        return "locales/index";
//    }
//
//    @GetMapping("/{guild_id}")
//    public String show(@PathVariable("guild_id") long guild_id, @NotNull Model model) {
//        model.addAttribute("locale", localeRepository.findLocalesTableByGuildId(guild_id));
//        return "locales/show";
//    }
//
//    @GetMapping("/new")
//    public String newLocale(@ModelAttribute("locale") LocalesTable locale) {
//        return "locales/new";
//    }
//
//    @PostMapping()
//    public String create(@ModelAttribute("locale") LocalesTable locale, @NotNull BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) return "locales/new";
//
//        localeRepository.save(locale);
//        return "redirect:/locales";
//    }
//
//    @GetMapping("/{guild_id}/edit")
//    public String edit(@NotNull Model model, @PathVariable("guild_id") long guild_id) {
//        model.addAttribute("localeToEdit", localeRepository.findLocalesTableByGuildId(guild_id).get(0));
//        return "locales/edit";
//    }
//
//    @PostMapping("/{guild_id}")
//    public String update(@ModelAttribute("locale") LocalesTable locale, @NotNull BindingResult bindingResult,
//                         @PathVariable("guild_id") long guild_id) {
//
//        if (bindingResult.hasErrors()) return "locales/edit";
//
//        Optional<LocalesTable> tableEntity = localeRepository.findByGuildId(guild_id);
//
//        if (tableEntity.isPresent()) {
//            LocalesTable localeEntity = tableEntity.get();
//            localeEntity.setLocaleName(locale.getLocaleName());
//            localeEntity.setLocaleEN(locale.getLocaleEN());
//            localeEntity.setLocaleRU(locale.getLocaleRU());
//            localeEntity.setLocaleUA(locale.getLocaleUA());
//            localeEntity.setLocaleCN(locale.getLocaleCN());
//            localeRepository.save(localeEntity);
//        }
//        return "redirect:/locales";
//    }
//
//
//    @DeleteMapping("/{guild_id}")
//    public String delete(@PathVariable("guild_id") long guild_id) {
//        localeRepository.deleteById(guild_id);
//        return "redirect:/locales";
//    }
//}
