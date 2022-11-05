package horse.boo.bot.setup.config;

import horse.boo.bot.setup.steps.Languages;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyService {
    String path = "src/main/resources/locales/";
    String appConfigPath = path + "locale.properties";

    private Properties getProperties() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream(appConfigPath));

        return appProps;
    }

    public String getGreetings(Languages language) throws IOException {
        String localeName = language.getLanguage();

        return getProperties().getProperty("locale." + localeName + ".greetings0");
    }

}
