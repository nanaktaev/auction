package by.company.auction;

import by.company.auction.console.MenuConfig;
import by.company.auction.console.MenuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.TimeZone;

@SpringBootApplication
@EnableJpaRepositories
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private MenuConfig menuConfig;
    @Autowired
    private MenuUtil menuUtil;

    @Override
    public void run(String... args) {

        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"));
        menuConfig.WELCOME_MENU.open();
        menuUtil.readCommand(menuConfig.WELCOME_MENU);

    }

}



