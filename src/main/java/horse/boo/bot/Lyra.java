package horse.boo.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("horse.boo")
public class Lyra {

    public static void main(String... args) {
        SpringApplication.run(Lyra.class, args);
    }

}