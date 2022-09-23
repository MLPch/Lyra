package horse.boo.bot.setup.steps;

public enum Languages {

    RUSSIAN("Russian"),
    ENGLISH("English");

    final String languageType;

    Languages(String type) {
        this.languageType = type;
    }

    public String getLanguage() {
        return languageType;
    }
}
