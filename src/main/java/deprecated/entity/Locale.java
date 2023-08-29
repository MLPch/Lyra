//package horse.boo.bot.database.entity;
//
//import horse.boo.bot.enums.Languages;
//import net.bytebuddy.utility.nullability.MaybeNull;
//
//import javax.persistence.*;
//import javax.validation.constraints.NotEmpty;
//import javax.validation.constraints.Size;
//import java.io.Serializable;
//
//@Entity
//@Table(name = "locale")
//public class Locale implements Serializable {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long id;
//    @Column
//    @NotEmpty(message = "config_id should not be empty")
//    private long config_id;
//    @Column
//    @NotEmpty(message = "Name should not be empty")
//    @Size(min = 2, max = 50, message = "Name should be between 2 and 30 characters")
//    private String locale_name;
//
//    @Column
//    @NotEmpty(message = "locale_en should not be empty")
//    private String locale_en;
//
//    @Column
//    @NotEmpty(message = "locale_ru should not be empty")
//    private String locale_ru;
//    @Column
//    @MaybeNull
//    private String locale_ua;
//    @Column
//    @MaybeNull
//    private String locale_cn;
//
//
//    protected Locale() {
//
//    }
//
//    public Locale(long id, long config_id, String locale_name, String locale_en, String locale_ru, @MaybeNull String locale_ua, @MaybeNull String locale_cn) {
//        this.id = id;
//        this.config_id = config_id;
//        this.locale_name = locale_name;
//        this.locale_en = locale_en;
//        this.locale_ru = locale_ru;
//        this.locale_ua = locale_ua;
//        this.locale_cn = locale_cn;
//    }
//
//    @Override
//    public String toString() {
//        return "\nLocale{" +
//                "id=" + id +
//                ", locale_name='" + locale_name + '\'' +
//                ", config_id='"  + config_id +  '\'' +
//                ", locale_en='" + locale_en + '\'' +
//                ", locale_ru='" + locale_ru + '\'' +
//                ", locale_ua='" + locale_ua + '\'' +
//                ", locale_cn='" + locale_cn + '\'' +
//                '}';
//    }
//
//    public long getId() {
//        return id;
//    }
//
//    public long getConfig_id() {
//        return config_id;
//    }
//
//    public String getLocale_name() {
//        return locale_name;
//    }
//
//    public void setLocale_name(String locale_name) {
//        this.locale_name = locale_name;
//    }
//
//    public String getLocale_en() {
//        return locale_en;
//    }
//
//    public void setLocale_en(String locale_en) {
//        this.locale_en = locale_en;
//    }
//
//    public String getLocale_ru() {
//        return locale_ru;
//    }
//
//    public void setLocale_ru(String locale_ru) {
//        this.locale_ru = locale_ru;
//    }
//
//    @MaybeNull
//    public String getLocale_ua() {
//        return locale_ua;
//    }
//
//    public void setLocale_ua(@MaybeNull String locale_ua) {
//        this.locale_ua = locale_ua;
//    }
//
//    @MaybeNull
//    public String getLocale_cn() {
//        return locale_cn;
//    }
//
//    public void setLocale_cn(@MaybeNull String locale_cn) {
//        this.locale_cn = locale_cn;
//    }
//
//    public String getLocaleStringByLanguage(Languages language){
//        String result = null;
//        switch (language) {
//            case ENGLISH:
//                result = locale_en;
//                break;
//            case RUSSIAN:
//                result = locale_ru;
//                break;
//            case UKRAINE:
//                result = locale_ua;
//                break;
//            case CHINA:
//                result = locale_cn;
//                break;
//            default:
//                break;
//        }
//        return result;
//    }
//}
//
