package by.company.auction.console.menu;

import by.company.auction.model.*;
import by.company.auction.services.*;
import by.company.auction.validators.LotValidator;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static by.company.auction.console.menu.MenuConfig.*;
import static by.company.auction.secuirty.AuthenticatonConfig.authentication;

class CommandConfig {

    private static final UserService userService = UserService.getInstance();
    private static final CategoryService categoryService = CategoryService.getInstance();
    private static final LotService lotService = LotService.getInstance();
    private static final BidService bidService = BidService.getInstance();
    private static final TownService townService = TownService.getInstance();
    private static final CompanyService companyService = CompanyService.getInstance();
    private static final MessageService messageService = MessageService.getInstance();

    private static Lot editedLot = null;

    static final Command LOGIN_COMMAND = new Command("login", "войти.", () -> {

        String email = MenuUtil.readStringValue("Введите ваш email:");
        String password = MenuUtil.readStringValue("Введите ваш пароль:");

        User user = userService.findUserByEmail(email);

        if (user != null && password.equals(user.getPassword())) {
            authentication.authenticateUser(user);
            Role role = user.getRole();
            MenuUtil.getMainMenuByRole(role).open();
            MenuUtil.readCommand(MenuUtil.getMainMenuByRole(role));
        } else {
            System.out.println("Введенные данные не верны.\n");
            MenuUtil.readCommand(WELCOME_MENU);
        }
        return null;
    });

    static final Command QUIT_COMMAND = new Command("quit", "завершить работу.", () -> {
        System.out.println("Работа завершена.");
        System.exit(0);
        return null;
    });

    static final Command LOGOUT_COMMAND = new Command("out", "выйти из аккаунта.", () -> {
        authentication.clear();
        WELCOME_MENU.open();
        MenuUtil.readCommand(WELCOME_MENU);
        return null;
    });

    static final Command VIEW_ALL_LOTS_COMMAND = new Command("all", "посмотреть все лоты.", () -> {
        List<Lot> lots = lotService.findAll();
        if (lots.isEmpty()) {
            System.out.println("Пока что аукцион пуст.");
            MenuUtil.readCommand(MenuUtil.getMainMenuByRole(authentication.getUserRole()));
        }

        System.out.println("Все лоты:");
        for (Lot lot : lots) {
            System.out.println(lot);
        }
        MenuUtil.readCommand(MenuUtil.getMainMenuByRole(authentication.getUserRole()));
        return null;
    });

    static final Command VIEW_BIDS_COMMAND = new Command("bids", "посмотреть ставки на лот.\n", () -> {

        Integer lotId = Integer.parseInt(MenuUtil.readNumericValue("Введите id лота:"));

        List<Bid> bids = bidService.findBidsByLotId(lotId);

        System.out.println("Ставки на лоте №" + lotId + ":");
        if (bids.isEmpty()) {
            System.out.println("Пока что на этом лоте нет ставок.");
        } else {
            Collections.reverse(bids);
            for (Bid bid : bids) {
                System.out.println(bid);
            }
        }

        MenuUtil.readCommand(MenuUtil.getMainMenuByRole(authentication.getUserRole()));
        return null;
    });

    static final Command VIEW_LOTS_BY_TOWN = new Command("town", "посмотреть лоты по городам.", () -> {

        List<Town> towns = townService.findAll();

        if (towns.isEmpty()) {
            System.out.println("Пока не было зарегистрировано ни одного города.");
            MenuUtil.readCommand(MenuUtil.getMainMenuByRole(authentication.getUserRole()));
        }

        System.out.println("Доступные города:");
        for (Town town : towns) {
            System.out.println(town);
        }

        Integer townId = Integer.parseInt(MenuUtil.readNumericValue("\nВведите id города:"));

        List<Lot> lots = lotService.findLotsByTownId(townId);

        if (lots.isEmpty()) {
            System.out.println("Пока что в этом городе нет лотов.");
        } else {
            for (Lot lot : lots) {
                System.out.println(lot);
            }
        }

        MenuUtil.readCommand(MenuUtil.getMainMenuByRole(authentication.getUserRole()));
        return null;
    });

    static final Command VIEW_LOTS_BY_CATEGORY = new Command("cat", "посмотреть лоты по категориям.", () -> {

        List<Category> categories = categoryService.findAll();

        if (categories.isEmpty()) {
            System.out.println("Пока не было зарегистрировано ни одной категории.");
            MenuUtil.readCommand(MenuUtil.getMainMenuByRole(authentication.getUserRole()));
        }

        System.out.println("Доступные категории:");
        for (Category category : categories) {
            System.out.println(category);
        }

        Integer categoryId = Integer.parseInt(MenuUtil.readNumericValue("\nВведите id категории:"));

        List<Lot> lots = lotService.findLotsByCategoryId(categoryId);

        if (lots.isEmpty()) {
            System.out.println("Пока что в этой категории нет лотов.");
        } else {
            for (Lot lot : lots) {
                System.out.println(lot);
            }
        }

        MenuUtil.readCommand(MenuUtil.getMainMenuByRole(authentication.getUserRole()));
        return null;
    });

    static final Command SET_ROLE_COMMAND = new Command("role", "управление ролями пользователей.", () -> {

        Integer userId = Integer.parseInt(MenuUtil.readNumericValue("Введите id пользователя:"));
        User user = userService.findById(userId);
        if (user == null) {
            System.out.println("По данному id пользователь не найден.");
            MenuUtil.readCommand(MAIN_MENU_ADMIN);
        }
        System.out.println(user + "\n");

        String roleString = MenuUtil.readRoleValue("Введите новую роль пользователя (USER, VENDOR, ADMIN):");

        Integer companyId = null;
        if (roleString.equals("VENDOR")) {
            if (companyService.findAll().isEmpty()) {
                System.out.println("Прежде чем назначать роль VENDOR необходимо зарегистрировать хотя бы одну компанию.");
                MenuUtil.readCommand(MAIN_MENU_ADMIN);
            }
            System.out.println("Доступные компании:");
            for (Company company : companyService.findAll()) {
                System.out.println(company);
            }
            companyId = Integer.parseInt(MenuUtil.readNumericValue("\nВведите id компании продавца:"));
        }

        user = userService.updateUserRole(userId, roleString, companyId);
        System.out.println("Роль пользователя изменена:\n" + user);

        MenuUtil.readCommand(MAIN_MENU_ADMIN);
        return null;
    });

    static final Command CREATE_LOT_COMMAND = new Command("clot", "создать новый лот.", () -> {

        if (categoryService.findAll().isEmpty() || townService.findAll().isEmpty()) {
            System.out.println("Ошибка. Администратору необходимо добавить хотя бы одну категорию и один город," +
                    " прежде чем появится возможность добавлять лоты.");
            MenuUtil.readCommand(MAIN_MENU_VENDOR);
        }

        String name = MenuUtil.readStringValue("Введите название лота:");

        System.out.println();
        for (Category category : categoryService.findAll()) {
            System.out.println(category);
        }

        Integer categoryId = Integer.parseInt(MenuUtil.readNumericValue("\nВведите id категории лота:"));
        String description = MenuUtil.readStringValue("Введите описание лота:");
        BigDecimal priceStart = new BigDecimal(MenuUtil.readNumericValue("Введите стартовую цену лота:"));
        BigDecimal step = new BigDecimal(MenuUtil.readNumericValue("Введите минимальный шаг цены лота:"));
        LocalDateTime closes = MenuUtil.readDateTimeValue("Введите дату и время окончания торгов.\nКорректный формат: гггг-ММ-дд ЧЧ:мм\nПример: 2020-07-21 16:30");

        System.out.println();
        for (Town town : townService.findAll()) {
            System.out.println(town);
        }

        Integer townId = Integer.parseInt(MenuUtil.readNumericValue("\nВведите id города, где находится лот:"));

        Lot lot = lotService.createLot(new Lot(name, description, priceStart, step, closes, categoryId, townId));
        System.out.println("Лот создан:\n" + lot);

        MenuUtil.readCommand(MAIN_MENU_VENDOR);
        return null;
    });

    static final Command CREATE_TOWN_COMMAND = new Command("ctown", "добавить город.", () -> {

        String name = StringUtils.capitalize(MenuUtil.readStringValue("Введите название города:").toLowerCase());
        if (!(townService.findByName(name) == null)) {
            System.out.println("Данный город уже был добавлен.");
            MenuUtil.readCommand(MAIN_MENU_ADMIN);
        }
        townService.create(new Town(name));
        System.out.println("Город добавлен.");
        MenuUtil.readCommand(MAIN_MENU_ADMIN);
        return null;
    });

    static final Command CREATE_CATEGORY_COMMAND = new Command("ccat", "добавить категорию.", () -> {

        String name = StringUtils.capitalize(MenuUtil.readStringValue("Введите название категории:").toLowerCase());
        if (!(categoryService.findByName(name) == null)) {
            System.out.println("Данная категория уже существует.");
            MenuUtil.readCommand(MAIN_MENU_ADMIN);
        }
        categoryService.create(new Category(name));
        System.out.println("Категория добавлена.");
        MenuUtil.readCommand(MAIN_MENU_ADMIN);
        return null;
    });

    static final Command CREATE_COMPANY_COMMAND = new Command("ccom", "добавить компанию.", () -> {

        String name = MenuUtil.readStringValue("Введите название компании:");
        if (!(companyService.findByName(name) == null)) {
            System.out.println("Данная компания уже зарегистрирована.");
            MenuUtil.readCommand(MAIN_MENU_ADMIN);
        }
        companyService.create(new Company(name));
        System.out.println("Компания добавлена.");
        MenuUtil.readCommand(MAIN_MENU_ADMIN);
        return null;
    });

    static final Command EDIT_LOT_COMMAND = new Command("elot", "редактировать лот.", () -> {

        Role role = authentication.getUserRole();
        Integer userId = authentication.getUserId();

        Integer lotId = Integer.parseInt(MenuUtil.readNumericValue("Введите id лота:"));
        editedLot = lotService.findById(lotId);

        if (lotService.findById(lotId) == null) {
            System.out.println("По данному id лот не найден.");
            MenuUtil.readCommand(MenuUtil.getMainMenuByRole(role));
        }

        if (role.equals(Role.VENDOR)) {
            LotValidator.validateOwnership(editedLot, userId);
        }

        System.out.println("Редактируемый лот:\n" + editedLot);

        EDIT_LOT_MENU.open();
        MenuUtil.readCommand(EDIT_LOT_MENU);
        return null;
    });

    static final Command EDIT_TITLE_COMMAND = new Command("title", "изменить название.", () -> {
        editedLot.setTitle(MenuUtil.readStringValue("Введите название лота:"));

        lotService.update(editedLot);

        System.out.println("Название изменено.\n");
        MenuUtil.readCommand(EDIT_LOT_MENU);
        return null;
    });

    static final Command EDIT_DESCRIPTION_COMMAND = new Command("desc", "изменить описание.", () -> {
        editedLot.setDescription(MenuUtil.readStringValue("Введите описание лота:"));

        lotService.update(editedLot);

        System.out.println("Описание изменено.\n");
        MenuUtil.readCommand(EDIT_LOT_MENU);
        return null;
    });

    static final Command EDIT_STEP_COMMAND = new Command("step", "изменить минимальный шаг.", () -> {
        editedLot.setStep(new BigDecimal((MenuUtil.readNumericValue("Введите минимальный шаг цены лота:"))));

        LotValidator.validateStep(editedLot);
        lotService.update(editedLot);

        System.out.println("Шаг изменен.\n");
        MenuUtil.readCommand(EDIT_LOT_MENU);
        return null;
    });

    static final Command EDIT_CLOSES_COMMAND = new Command("close", "изменить дату окончания торгов.", () -> {
        editedLot.setCloses(MenuUtil.readDateTimeValue("Введите дату и время окончания торгов.\nКорректный формат: гггг-ММ-дд ЧЧ:мм\nПример: 2020-07-21 16:30"));

        LotValidator.validateClosingDate(editedLot);
        lotService.update(editedLot);

        System.out.println("Дата оконачания торгов изменена.\n");
        MenuUtil.readCommand(EDIT_LOT_MENU);
        return null;
    });

    static final Command EDIT_CATEGORY_COMMAND = new Command("cat", "изменить категорию.", () -> {

        for (Category category : categoryService.findAll()) {
            System.out.println(category);
        }
        editedLot.setCategoryId(Integer.parseInt(MenuUtil.readNumericValue("\nВведите id категории лота:")));

        LotValidator.validateCategory(editedLot);
        lotService.update(editedLot);

        System.out.println("Категория изменена.\n");
        MenuUtil.readCommand(EDIT_LOT_MENU);
        return null;
    });

    static final Command EDIT_TOWN_COMMAND = new Command("town", "изменить город.", () -> {

        for (Town town : townService.findAll()) {
            System.out.println(town);
        }
        editedLot.setTownId(Integer.parseInt(MenuUtil.readNumericValue("\nВведите id города, где находится лот:")));

        LotValidator.validateTown(editedLot);
        lotService.update(editedLot);

        System.out.println("Город изменен.\n");
        MenuUtil.readCommand(EDIT_LOT_MENU);
        return null;
    });

    static final Command DELETE_LOT_COMMAND = new Command("del", "удалить лот.", () -> {

        lotService.deleteLot(editedLot);
        System.out.println("Лот удален.");

        Role role = authentication.getUserRole();
        MenuUtil.getMainMenuByRole(role).open();
        MenuUtil.readCommand(MenuUtil.getMainMenuByRole(role));
        return null;
    });

    static final Command BACK_COMMAND = new Command("back", "назад в главное меню.", () -> {

        Role role = authentication.getUserRole();
        MenuUtil.getMainMenuByRole(role).open();
        MenuUtil.readCommand(MenuUtil.getMainMenuByRole(role));
        return null;
    });

    static final Command REGISTER_COMMAND = new Command("reg", "зарегистрироваться.", () -> {

        String email = MenuUtil.readEmailValue("Введите email:");
        String password = MenuUtil.readPasswordValue("Введите пароль (не короче пяти символов):");
        String username = MenuUtil.readStringValue("Введите имя пользователя:");

        User user = userService.registerUser(new User(email, password, username));
        System.out.println("Пользователь зарегистрирован:\n" + user);

        MenuUtil.readCommand(WELCOME_MENU);
        return null;
    });

    static final Command BID_COMMAND = new Command("bid", "сделать ставку.", () -> {

        Integer lotId = Integer.parseInt(MenuUtil.readNumericValue("Введите id лота:"));
        BigDecimal bidValue = new BigDecimal(MenuUtil.readNumericValue("Введите размер ставки:"));

        Bid bid = bidService.makeBid(new Bid(lotId, bidValue));
        System.out.println("Ставка сделана:\n" + bid);

        MenuUtil.readCommand(MAIN_MENU);
        return null;
    });

    static final Command VIEW_USER_MESSAGES = new Command("mes", "посмотреть сообщения.", () -> {

        Integer userId = authentication.getUserId();

        messageService.prepareUserMessages(userId);

        System.out.println("Ваши сообщения:");
        List<Message> userMessages = messageService.findMessagesByUserId(userId);

        if (userMessages.isEmpty()) {
            System.out.println("У вас пока что нет сообщений.");
        } else {
            Collections.reverse(userMessages);
            for (Message message : userMessages) {
                System.out.println(message);
            }
        }
        MenuUtil.readCommand(MAIN_MENU);
        return null;
    });
}
