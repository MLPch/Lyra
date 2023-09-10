package horse.boo.bot.database.table;

import net.bytebuddy.utility.nullability.MaybeNull;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "locale")
public class LocalesTable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "guild_id")
    private long guildId;

    @Column(name = "locale_name")
    private String localeName;

    @Column(name = "locale_en")
    private String localeEN;

    @Column(name = "locale_ru")
    private String localeRU;

    @Column(name = "locale_ua")
    @MaybeNull
    private String localeUA;

    @Column(name = "locale_cn")
    @MaybeNull
    private String localeCN;


    public LocalesTable() {
    }


    public long getId() {
        return id;
    }

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    public String getLocaleName() {
        return localeName;
    }

    public void setLocaleName(String localeName) {
        this.localeName = localeName;
    }

    public String getLocaleEN() {
        return localeEN;
    }

    public void setLocaleEN(String localeEN) {
        this.localeEN = localeEN;
    }

    public String getLocaleRU() {
        return localeRU;
    }

    public void setLocaleRU(String localeRU) {
        this.localeRU = localeRU;
    }

    @MaybeNull
    public String getLocaleUA() {
        return localeUA;
    }

    public void setLocaleUA(@MaybeNull String localeUA) {
        this.localeUA = localeUA;
    }

    @MaybeNull
    public String getLocaleCN() {
        return localeCN;
    }

    public void setLocaleCN(@MaybeNull String localeCN) {
        this.localeCN = localeCN;
    }

    /**
     * @param language - Язык искомого значения
     * @return - Строка с заранее записанным в БД значением соответствующая параметру language.
     */
    public String getValueByLanguage(@NotNull String language) {
        return switch (language) {
            case "english" -> getLocaleEN();
            case "russian" -> getLocaleRU();
            case "ukraine" -> getLocaleUA();
            case "china" -> getLocaleCN();
            default -> "";
        };
    }

    /**
     * @param language - Язык изменяемого значения
     * @param value - Значение для установки
     */
    public void setValueByLanguage(@NotNull String language, String value) {
        switch (language) {
            case "english" -> setLocaleEN(value);
            case "russian" -> setLocaleRU(value);
            case "ukraine" -> setLocaleUA(value);
            case "china" -> setLocaleCN(value);
        };
    }

    @Override
    public String toString() {
        return "LocalesTable{" +
                "id=" + id +
                ", guildId=" + guildId +
                ", localeName='" + localeName + '\'' +
                ", localeEN='" + localeEN + '\'' +
                ", localeRU='" + localeRU + '\'' +
                ", localeUA='" + localeUA + '\'' +
                ", localeCN='" + localeCN + '\'' +
                '}';
    }

}
