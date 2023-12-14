package horse.boo.bot.database.enums;

public enum LocaleType {
    GREETINGS("greetings"),
    FAREWELL("farewell"),
    UNRELATED("unrelated"),
    GATEKEEPER("gatekeeper"),
    CONSTRUCTOR("constructor");

    final String localeType;

    LocaleType(String type) {
        this.localeType = type;
    }

    public String getLocaleType() {
        return localeType;
    }
}
