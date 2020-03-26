package by.company.auction.console;

import by.company.auction.model.*;
import by.company.auction.services.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class SampleEntitiesCreator {
    static void createSampleEntities() {
        List<User> sampleUsers = Arrays.asList(
                new User("1", "1", "tester", Role.VENDOR, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
                new User("admin@mail.com", "admin", "admin", Role.ADMIN, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
                new User("user@mail.com", "user", "user", Role.USER, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
                new User("vendor@mail.com", "vendor", "vendor", Role.VENDOR, 1, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
                new User("vendor2@mail.com", "vendor2", "vendor2", Role.VENDOR, 2, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
                new User("2", "2", "tester2", Role.USER, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
                new User("3", "3", "tester3", Role.USER, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
        );

        List<Lot> sampleLots = Arrays.asList(
                new Lot("Тестовый лот 1", "Это описание длящегося лота.", new BigDecimal(100), new BigDecimal(100), new BigDecimal(10),
                        LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1), 3, 4, 1, new ArrayList<>(), new ArrayList<>()),
                new Lot("Тестовый лот 2", "Это описание горящего лота.", new BigDecimal(800), new BigDecimal(800), new BigDecimal(25),
                        LocalDateTime.now().minusHours(1), LocalDateTime.now().plusMinutes(3), 1, 5, 1, new ArrayList<>(), new ArrayList<>()),
                new Lot("Тестовый лот 3", "Это описание истекшего лота.", new BigDecimal(75), new BigDecimal(75), new BigDecimal(10),
                        LocalDateTime.now().minusHours(1), LocalDateTime.now().minusMinutes(3), 2, 5, 2, new ArrayList<>(), new ArrayList<>()),
                new Lot("Тестовый лот 4", "Это описание истекшего лота.", new BigDecimal(60), new BigDecimal(60), new BigDecimal(12),
                        LocalDateTime.now().minusHours(1), LocalDateTime.now().minusMinutes(3), 2, 5, 2, new ArrayList<>(), new ArrayList<>())
        );

        List<Town> sampleTowns = Arrays.asList(
                new Town("Минск", 1, 2),
                new Town("Барановичи", 3, 4),
                new Town("Брест", (Integer) null)
        );

        List<Category> sampleCategories = Arrays.asList(
                new Category("Недвижимость", 2),
                new Category("Мебель", 3, 4),
                new Category("Антиквариат", 1)
        );

        List<Company> sampleCompanies = Arrays.asList(
                new Company("\"Антикварная лавка\"", 4),
                new Company("ООО \"Рога и Копыта\"", 5)
        );


        for (User user : sampleUsers) {
            UserService.getInstance().create(user);
        }

        for (Lot lot : sampleLots) {
            LotService.getInstance().create(lot);
        }

        for (Town town : sampleTowns) {
            TownService.getInstance().create(town);
        }

        for (Category category : sampleCategories) {
            CategoryService.getInstance().create(category);
        }

        for (Company company : sampleCompanies) {
            CompanyService.getInstance().create(company);
        }
    }
}
