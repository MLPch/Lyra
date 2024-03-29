package horse.boo.bot.database.enums;

public enum Languages {

    ENGLISH("english"),
    RUSSIAN("russian"),
    UKRAINE("ukraine"),
    CHINESE("chinese");

    final String languageType;

    Languages(String type) {
        this.languageType = type;
    }

    public String getLanguage() {
        return languageType;
    }

}
