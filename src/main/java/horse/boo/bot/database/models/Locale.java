package horse.boo.bot.database.models;

import net.bytebuddy.utility.nullability.MaybeNull;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class Locale {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 50, message = "Name should be between 2 and 30 characters")
    private String locale_name;

    @NotEmpty(message = "locale_en should not be empty")
    private String locale_en;

    @NotEmpty(message = "locale_ru should not be empty")
    private String locale_ru;
    @MaybeNull
    private String locale_ua;
    @MaybeNull
    private String locale_cn;

    public Locale() {

    }

    public Locale(long id, String locale_name, String locale_en, String locale_ru, @MaybeNull String locale_ua, @MaybeNull String locale_cn) {
        this.id = id;
        this.locale_name = locale_name;
        this.locale_en = locale_en;
        this.locale_ru = locale_ru;
        this.locale_ua = locale_ua;
        this.locale_cn = locale_cn;
    }

    @Override
    public String toString() {
        return "\nLocale{" +
                "id=" + id +
                ", locale_name='" + locale_name + '\'' +
                ", locale_en='" + locale_en + '\'' +
                ", locale_ru='" + locale_ru + '\'' +
                ", locale_ua='" + locale_ua + '\'' +
                ", locale_cn='" + locale_cn + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLocale_name() {
        return locale_name;
    }

    public void setLocale_name(String locale_name) {
        this.locale_name = locale_name;
    }

    public String getLocale_en() {
        return locale_en;
    }

    public void setLocale_en(String locale_en) {
        this.locale_en = locale_en;
    }

    public String getLocale_ru() {
        return locale_ru;
    }

    public void setLocale_ru(String locale_ru) {
        this.locale_ru = locale_ru;
    }

    @MaybeNull
    public String getLocale_ua() {
        return locale_ua;
    }

    public void setLocale_ua(@MaybeNull String locale_ua) {
        this.locale_ua = locale_ua;
    }

    @MaybeNull
    public String getLocale_cn() {
        return locale_cn;
    }

    public void setLocale_cn(@MaybeNull String locale_cn) {
        this.locale_cn = locale_cn;
    }
}

