package by.company.auction.console.menu;

import by.company.auction.exceptions.AuctionException;
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
    private static final LotValidator lotValidator = LotValidator.getInstance();

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
            throw new AuctionException("Введенные данные не верны.");
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
            throw new AuctionException("Пока что аукцион пуст.");
        }

        System.out.println("Все лоты:");
        lots.forEach(System.out::println);

        MenuUtil.readCommand(MenuUtil.getMainMenuByRole(authentication.getUserRole()));
        return null;
    });

    static final Command VIEW_BIDS_COMMAND = new Command("bids", "посмотреть ставки на лот.\n", () -> {

        Integer lotId = Integer.parseInt(MenuUtil.readNumericValue("Введите id лота:"));

        List<Bid> bids = bidService.findBidsByLotId(lotId);

        System.out.println("Ставки на лоте №" + lotId + ":");
        if (bids.isEmpty()) {
            throw new AuctionException("Пока что на этом лоте нет ставок.");
        } else {
            Collections.reverse(bids);
            bids.forEach(System.out::println);
        }

        MenuUtil.readCommand(MenuUtil.getMainMenuByRole(authentication.getUserRole()));
        return null;
    });

    static final Command VIEW_LOTS_BY_TOWN = new Command("town", "посмотреть лоты по городам.", () -> {

        List<Town> towns = townService.findAll();

        if (towns.isEmpty()) {
            throw new AuctionException("Пока не было зарегистрировано ни одного города.");
        }

        System.out.println("Доступные города:");
        towns.forEach(System.out::println);

        Integer townId = Integer.parseInt(MenuUtil.readNumericValue("\nВведите id города:"));

        List<Lot> lots = lotService.findLotsByTownId(townId);

        if (lots.isEmpty()) {
            throw new AuctionException("Пока что в этом городе нет лотов.");
        } else {
            lots.forEach(System.out::println);
        }

        MenuUtil.readCommand(MenuUtil.getMainMenuByRole(authentication.getUserRole()));
        return null;
    });

    static final Command VIEW_LOTS_BY_CATEGORY = new Command("cat", "посмотреть лоты по категориям.", () -> {

        List<Category> categories = categoryService.findAll();

        if (categories.isEmpty()) {
            throw new AuctionException("Пока не было зарегистрировано ни одной категории.");
        }

        System.out.println("Доступные категории:");
        categories.forEach(System.out::println);

        Integer categoryId = Integer.parseInt(MenuUtil.readNumericValue("\nВведите id категории:"));

        List<Lot> lots = lotService.findLotsByCategoryId(categoryId);

        if (lots.isEmpty()) {
            throw new AuctionException("Пока что в этой категории нет лотов.");
        } else {
            lots.forEach(System.out::println);
        }

        MenuUtil.readCommand(MenuUtil.getMainMenuByRole(authentication.getUserRole()));
        return null;
    });

    static final Command SET_ROLE_COMMAND = new Command("role", "управление ролями пользователей.", () -> {

        Integer userId = Integer.parseInt(MenuUtil.readNumericValue("Введите id пользователя:"));
        User user = userService.findById(userId);
        if (user == null) {
            throw new AuctionException("По данному id пользователь не найден.");
        }
        System.out.println(user + "\n");

        String roleString = MenuUtil.readRoleValue("Введите новую роль пользователя (USER, VENDOR, ADMIN):");

        Integer companyId = null;
        if (roleString.equals("VENDOR")) {
            if (companyService.findAll().isEmpty()) {
                throw new AuctionException("Прежде чем назначать роль VENDOR необходимо зарегистрировать хотя бы одну компанию.");
            }
            System.out.println("Доступные компании:");
            companyService.findAll().forEach(System.out::println);

            companyId = Integer.parseInt(MenuUtil.readNumericValue("\nВведите id компании продавца:"));
        }

        user = userService.updateUserRole(userId, roleString, companyId);
        System.out.println("Роль пользователя изменена:\n" + user);

        MenuUtil.readCommand(MAIN_MENU_ADMIN);
        return null;
    });

    static final Command CREATE_LOT_COMMAND = new Command("clot", "создать новый лот.", () -> {

        if (categoryService.findAll().isEmpty() || townService.findAll().isEmpty()) {
            throw new AuctionException("Ошибка. Администратору необходимо добавить хотя бы одну категорию и один город," +
                    " прежде чем появится возможность добавлять лоты.");
        }

        String title = MenuUtil.readStringValue("Введите название лота:");

        System.out.println();
        categoryService.findAll().forEach(System.out::println);

        Integer categoryId = Integer.parseInt(MenuUtil.readNumericValue("\nВведите id категории лота:"));
        String description = MenuUtil.readStringValue("Введите описание лота:");
        BigDecimal priceStart = new BigDecimal(MenuUtil.readNumericValue("Введите стартовую цену лота:"));
        BigDecimal step = new BigDecimal(MenuUtil.readNumericValue("Введите минимальный шаг цены лота:"));
        LocalDateTime closes = MenuUtil.readDateTimeValue("Введите дату и время окончания торгов." +
                "\nКорректный формат: гггг-ММ-дд ЧЧ:мм\nПример: 2020-07-21 16:30");

        System.out.println();
        townService.findAll().forEach(System.out::println);

        Integer townId = Integer.parseInt(MenuUtil.readNumericValue("\nВведите id города, где находится лот:"));

        Lot lot = new Lot();
        lot.setTitle(title);
        lot.setDescription(description);
        lot.setPriceStart(priceStart);
        lot.setStep(step);
        lot.setCloses(closes);
        lot.setCategoryId(categoryId);
        lot.setTownId(townId);

        lot = lotService.createLot(lot);
        System.out.println("Лот создан:\n" + lot);

        MenuUtil.readCommand(MAIN_MENU_VENDOR);
        return null;
    });

    static final Command CREATE_TOWN_COMMAND = new Command("ctown", "добавить город.", () -> {

        String name = StringUtils.capitalize(MenuUtil.readStringValue("Введите название города:").toLowerCase());
        if (!(townService.findTownByName(name) == null)) {
            throw new AuctionException("Данный город уже был добавлен.");
        }

        Town town = new Town();
        town.setName(name);

        townService.create(town);
        System.out.println("Город добавлен.");
        MenuUtil.readCommand(MAIN_MENU_ADMIN);
        return null;
    });

    static final Command CREATE_CATEGORY_COMMAND = new Command("ccat", "добавить категорию.", () -> {

        String name = StringUtils.capitalize(MenuUtil.readStringValue("Введите название категории:").toLowerCase());
        if (!(categoryService.findCategoryByName(name) == null)) {
            throw new AuctionException("Данная категория уже существует.");
        }

        Category category = new Category();
        category.setName(name);

        categoryService.create(category);
        System.out.println("Категория добавлена.");
        MenuUtil.readCommand(MAIN_MENU_ADMIN);
        return null;
    });

    static final Command CREATE_COMPANY_COMMAND = new Command("ccom", "добавить компанию.", () -> {

        String name = MenuUtil.readStringValue("Введите название компании:");
        if (!(companyService.findCompanyByName(name) == null)) {
            throw new AuctionException("Данная компания уже зарегистрирована.");
        }

        Company company = new Company();
        company.setName(name);

        companyService.create(company);
        System.out.println("Компания добавлена.");
        MenuUtil.readCommand(MAIN_MENU_ADMIN);
        return null;
    });

    static final Command EDIT_LOT_COMMAND = new Command("elot", "редактировать лот.", () -> {

        Role role = authentication.getUserRole();

        Integer lotId = Integer.parseInt(MenuUtil.readNumericValue("Введите id лота:"));
        editedLot = lotService.findById(lotId);

        if (lotService.findById(lotId) == null) {
            throw new AuctionException("По данному id лот не найден.");
        }

        if (role.equals(Role.VENDOR)) {
            lotValidator.validateOwnership(editedLot, authentication.getUserCompanyId());
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

        lotValidator.validateStep(editedLot);
        lotService.update(editedLot);

        System.out.println("Шаг изменен.\n");
        MenuUtil.readCommand(EDIT_LOT_MENU);
        return null;
    });

    static final Command EDIT_CLOSES_COMMAND = new Command("close", "изменить дату окончания торгов.", () -> {
        editedLot.setCloses(MenuUtil.readDateTimeValue("Введите дату и время окончания торгов.\nКорректный формат: гггг-ММ-дд ЧЧ:мм\nПример: 2020-07-21 16:30"));

        lotValidator.validateClosingDate(editedLot);
        lotService.update(editedLot);

        System.out.println("Дата оконачания торгов изменена.\n");
        MenuUtil.readCommand(EDIT_LOT_MENU);
        return null;
    });

    static final Command EDIT_CATEGORY_COMMAND = new Command("cat", "изменить категорию.", () -> {

        categoryService.findAll().forEach(System.out::println);

        editedLot.setCategoryId(Integer.parseInt(MenuUtil.readNumericValue("\nВведите id категории лота:")));

        lotValidator.validateCategory(editedLot);
        lotService.update(editedLot);

        System.out.println("Категория изменена.\n");
        MenuUtil.readCommand(EDIT_LOT_MENU);
        return null;
    });

    static final Command EDIT_TOWN_COMMAND = new Command("town", "изменить город.", () -> {

        townService.findAll().forEach(System.out::println);

        editedLot.setTownId(Integer.parseInt(MenuUtil.readNumericValue("\nВведите id города, где находится лот:")));

        lotValidator.validateTown(editedLot);
        lotService.update(editedLot);

        System.out.println("Город изменен.\n");
        MenuUtil.readCommand(EDIT_LOT_MENU);
        return null;
    });

    static final Command DELETE_LOT_COMMAND = new Command("del", "удалить лот.", () -> {

        lotService.delete(editedLot.getId());
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

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setUsername(username);

        user = userService.registerUser(user);
        System.out.println("Пользователь зарегистрирован:\n" + user);

        MenuUtil.readCommand(WELCOME_MENU);
        return null;
    });

    static final Command BID_COMMAND = new Command("bid", "сделать ставку.", () -> {

        Integer lotId = Integer.parseInt(MenuUtil.readNumericValue("Введите id лота:"));
        BigDecimal bidValue = new BigDecimal(MenuUtil.readNumericValue("Введите размер ставки:"));

        Bid bid = new Bid();
        bid.setLotId(lotId);
        bid.setValue(bidValue);

        bid = bidService.makeBid(bid);
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
            userMessages.forEach(System.out::println);
        }
        MenuUtil.readCommand(MAIN_MENU);
        return null;
    });
}
