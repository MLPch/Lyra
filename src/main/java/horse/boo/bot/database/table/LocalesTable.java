package horse.boo.bot.database.table;

import jakarta.persistence.*;

@Entity
@Table(name = "locale_neww")
public class LocalesTable {

    public LocalesTable() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "guild_name")
    private String guildName;

    @Column(name = "guild_id")
    private Long guildId;

    @Column(name = "language")
    private String language;

    @Column(name = "locale_type")
    private String localeType;

    @Column(name = "mode_type")
    private String modeType;

    @Column(name = "field_type")
    private String fieldType;

    @Column(name = "value")
    private String value;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuildName() {
        return guildName;
    }

    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }

    public Long getGuildId() {
        return guildId;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLocaleType() {
        return localeType;
    }

    public void setLocaleType(String localeType) {
        this.localeType = localeType;
    }

    public String getModeType() {
        return modeType;
    }

    public void setModeType(String modeType) {
        this.modeType = modeType;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return "LocalesTable{" +
                "id=" + id +
                ", guildName='" + guildName + '\'' +
                ", guildId=" + guildId +
                ", language='" + language + '\'' +
                ", localeType='" + localeType + '\'' +
                ", modeType='" + modeType + '\'' +
                ", fieldType='" + fieldType + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
