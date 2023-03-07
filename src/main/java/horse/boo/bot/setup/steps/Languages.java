package horse.boo.bot.setup.steps;

public enum Languages {

    ENGLISH("English"),
    RUSSIAN("Russian"),
    UKRAINE("Ukraine"),
    CHINA("China");

    final String languageType;

    Languages(String type) {
        this.languageType = type;
    }

    public String getLanguage() {
        return languageType;
    }
}
