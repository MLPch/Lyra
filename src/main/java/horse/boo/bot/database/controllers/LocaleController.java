package horse.boo.bot.database.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import horse.boo.bot.database.dao.LocaleDAO;
import horse.boo.bot.database.models.Locale;

import javax.validation.Valid;

@Controller
@RequestMapping("/languages")
public class LocaleController {

    private final LocaleDAO localeDAO;

    @Autowired
    public LocaleController(LocaleDAO localeDAO) {
        this.localeDAO = localeDAO;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("language", localeDAO.index());
        return "languages/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") long id, Model model) {
        model.addAttribute("locale", localeDAO.show(id));
        return "languages/show";
    }

    @GetMapping("/new")
    public String newLocale(@ModelAttribute("locale") Locale locale) {
        return "languages/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("locale") @Valid Locale locale,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "languages/new";

        localeDAO.save(locale);
        return "redirect:/languages";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") long id) {
        model.addAttribute("locale", localeDAO.show(id));
        return "languages/edit";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("locale") @Valid Locale locale, BindingResult bindingResult,
                         @PathVariable("id") long id) {
        if (bindingResult.hasErrors())
            return "languages/edit";

        localeDAO.update(id, locale);
        return "redirect:/languages";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        localeDAO.delete(id);
        return "redirect:/languages";
    }
}
