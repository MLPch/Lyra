package horse.boo.bot.database.enums;

public enum FieldType {
    EMBED_STRING_ABOVE("stringAbove"),
    EMBED_TITLE("title"),
    EMBED_FIELD_NAME("fieldName"),
    EMBED_FIELD_VALUE("fieldValue"),
    EMBED_FOOTER_TEXT("footerText"),


    GATEKEEPER_SUBSCRIBE_EPHEMERAL("subscribeEphemeral"),
    GATEKEEPER_UNSUBSCRIBE_EPHEMERAL("unsubscribeEphemeral"),

    CONSTRUCTOR_REPLY("constructorReply");

    final String fieldType;

    FieldType(String type) {
        this.fieldType = type;
    }

    public String getFieldType() {
        return fieldType;
    }
}
