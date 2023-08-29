//package deprecated.dump.variables;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import horse.boo.bot.setup.config.GuildLocaleService;
//import lombok.extern.slf4j.Slf4j;
//import net.dv8tion.jda.api.entities.Guild;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Slf4j
//@Component
//@Deprecated
//public class VariablesImporter {
//
//    private static final ObjectMapper mapper = new ObjectMapper();
//    private GuildLocaleService variables;
//
//
//    public VariablesImporter() throws IOException {
//        getVariablesProperties();
//    }
//
//    public GuildLocaleService getVariables(String json) throws IOException {
//        variables = parseConfig(json);
//        return variables;
//    }
//
//    private void getVariablesProperties()
//            throws IOException {
//
//        String json = getPropertiesJson();
//        parseConfig(json);
//
//    }
//
//    private String getPropertiesJson(Guild guild) throws IOException {
//
////TODO: Сюда надо передать получаемый джсон
//
//        return null;
//    }
//
//    private GuildLocaleService parseConfig(String json) throws IOException {
//
//        //        JsonFactory factory = mapper.getFactory();
////        JsonParser parser = factory.createParser(json);
////        JsonNode jsonNode = mapper.readTree(parser);
////
////        JsonNode locatedNode = jsonNode.path("variables");
////
////        variables = mapper.readValue(mapper.writeValueAsString(locatedNode.get("variables")), VariablesImpl.class);
//        return mapper.readValue(json, GuildLocaleService.class);
//    }
//}
