package by.company.auction.console;

import by.company.auction.model.*;
import by.company.auction.services.*;
import by.company.auction.validators.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static by.company.auction.common.security.AuthenticationConfig.authentication;

@Component
class CommandConfig {

    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private LotService lotService;
    @Autowired
    private BidService bidService;
    @Autowired
    private TownService townService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private LotValidator lotValidator;
    @Autowired
    private TownValidator townValidator;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private CategoryValidator categoryValidator;
    @Autowired
    private CompanyValidator companyValidator;
    @Autowired
    private MenuConfig menuConfig;
    @Autowired
    private MenuUtil menuUtil;

    private static Lot editedLot = null;

    Command LOGIN_COMMAND = new Command("login", "войти.", () -> {

        String email = menuUtil.readStringValue("Введите ваш email:");
        String password = menuUtil.readStringValue("Введите ваш пароль:");

        User user = userValidator.validateLogin(email, password);

        authentication.authenticateUser(user);
        Role role = user.getRole();
        menuUtil.getMainMenuByRole(role).open();
        menuUtil.readCommand(menuUtil.getMainMenuByRole(role));

        return null;
    });

    Command QUIT_COMMAND = new Command("quit", "завершить работу.", () -> {
        System.out.println("Работа завершена.");
        System.exit(0);
        return null;
    });

    Command LOGOUT_COMMAND = new Command("out", "выйти из аккаунта.", () -> {
        authentication.clear();
        menuConfig.WELCOME_MENU.open();
        menuUtil.readCommand(menuConfig.WELCOME_MENU);
        return null;
    });

    Command VIEW_ALL_LOTS_COMMAND = new Command("all", "посмотреть все лоты.", () -> {

        System.out.println("Все лоты:");
        lotService.findAll().forEach(System.out::println);

        menuUtil.readCommand(menuUtil.getMainMenuByRole(authentication.getUserRole()));
        return null;
    });

    Command VIEW_BIDS_COMMAND = new Command("bids", "посмотреть ставки на лот.\n", () -> {

        Integer lotId = Integer.parseInt(menuUtil.readNumericValue("Введите id лота:"));

        System.out.println("Ставки на лоте №" + lotId + ":");
        List<Bid> bids = bidService.findBidsByLotId(lotId);

        Collections.reverse(bids);
        bids.forEach(System.out::println);

        menuUtil.readCommand(menuUtil.getMainMenuByRole(authentication.getUserRole()));
        return null;
    });

    Command VIEW_LOTS_BY_TOWN = new Command("town", "посмотреть лоты по городам.", () -> {

        System.out.println("Доступные города:");
        townService.findAll().forEach(System.out::println);

        Integer townId = Integer.parseInt(menuUtil.readNumericValue("\nВведите id города:"));
        lotService.findLotsByTownId(townId).forEach(System.out::println);

        menuUtil.readCommand(menuUtil.getMainMenuByRole(authentication.getUserRole()));
        return null;
    });

    Command VIEW_LOTS_BY_CATEGORY = new Command("cat", "посмотреть лоты по категориям.", () -> {

        System.out.println("Доступные категории:");
        categoryService.findAll().forEach(System.out::println);

        Integer categoryId = Integer.parseInt(menuUtil.readNumericValue("\nВведите id категории:"));
        lotService.findLotsByCategoryId(categoryId).forEach(System.out::println);

        menuUtil.readCommand(menuUtil.getMainMenuByRole(authentication.getUserRole()));
        return null;
    });

    Command SET_ROLE_COMMAND = new Command("role", "управление ролями пользователей.", () -> {

        Integer userId = Integer.parseInt(menuUtil.readNumericValue("Введите id пользователя:"));

        User user = userService.findById(userId);

        System.out.println(user);

        String roleString = menuUtil.readRoleValue("Введите новую роль пользователя (USER, VENDOR, ADMIN):");

        Integer companyId = null;

        if (roleString.equals("VENDOR")) {

            System.out.println("Доступные компании:");
            companyService.findAll().forEach(System.out::println);

            companyId = Integer.parseInt(menuUtil.readNumericValue("\nВведите id компании продавца:"));
        }

        user = userService.updateUserRole(userId, roleString, companyId);
        System.out.println("Роль пользователя изменена:\n" + user);

        menuUtil.readCommand(menuConfig.MAIN_MENU_ADMIN);
        return null;
    });

    Command CREATE_LOT_COMMAND = new Command("clot", "создать новый лот.", () -> {

        String title = menuUtil.readStringValue("Введите название лота:");

        System.out.println("\nДоступные категории:");
        categoryService.findAll().forEach(System.out::println);
        Integer categoryId = Integer.parseInt(menuUtil.readNumericValue("\nВведите id категории лота:"));

        String description = menuUtil.readStringValue("Введите описание лота:");
        BigDecimal priceStart = new BigDecimal(menuUtil.readNumericValue("Введите стартовую цену лота:"));
        BigDecimal step = new BigDecimal(menuUtil.readNumericValue("Введите минимальный шаг цены лота:"));
        LocalDateTime closes = menuUtil.readDateTimeValue("Введите дату и время окончания торгов." +
                "\nКорректный формат: гггг-ММ-дд ЧЧ:мм\nПример: 2020-07-21 16:30");

        System.out.println("\nДоступные города:");
        townService.findAll().forEach(System.out::println);
        Integer townId = Integer.parseInt(menuUtil.readNumericValue("\nВведите id города, где находится лот:"));

        Lot lot = new Lot();
        lot.setTitle(title);
        lot.setDescription(description);
        lot.setPriceStart(priceStart);
        lot.setStep(step);
        lot.setCloses(closes);
        lot.setCategory(categoryService.findById(categoryId));
        lot.setTown(townService.findById(townId));

        lot = lotService.createLot(lot);
        System.out.println("Лот создан:\n" + lot);

        menuUtil.readCommand(menuConfig.MAIN_MENU_VENDOR);
        return null;
    });

    Command CREATE_TOWN_COMMAND = new Command("ctown", "добавить город.", () -> {

        String name = StringUtils.capitalize(menuUtil.readStringValue("Введите название города:").toLowerCase());

        Town town = new Town();
        town.setName(name);

        townValidator.validate(town);
        townService.create(town);

        System.out.println("Город добавлен.\n");
        menuUtil.readCommand(menuConfig.MAIN_MENU_ADMIN);
        return null;
    });

    Command CREATE_CATEGORY_COMMAND = new Command("ccat", "добавить категорию.", () -> {

        String name = StringUtils.capitalize(menuUtil.readStringValue("Введите название категории:").toLowerCase());

        Category category = new Category();
        category.setName(name);

        categoryValidator.validate(category);
        categoryService.create(category);

        System.out.println("Категория добавлена.\n");
        menuUtil.readCommand(menuConfig.MAIN_MENU_ADMIN);
        return null;
    });

    Command CREATE_COMPANY_COMMAND = new Command("ccom", "добавить компанию.", () -> {

        String name = menuUtil.readStringValue("Введите название компании:");

        Company company = new Company();
        company.setName(name);

        companyValidator.validate(company);
        companyService.create(company);

        System.out.println("Компания добавлена.\n");
        menuUtil.readCommand(menuConfig.MAIN_MENU_ADMIN);
        return null;
    });

    Command EDIT_LOT_COMMAND = new Command("elot", "редактировать лот.", () -> {

        Role role = authentication.getUserRole();

        Integer lotId = Integer.parseInt(menuUtil.readNumericValue("Введите id лота:"));
        editedLot = lotService.findById(lotId);

        if (role.equals(Role.VENDOR)) {
            lotValidator.validateOwnership(editedLot, authentication.getUserCompanyId());
        }

        System.out.println("Редактируемый лот:\n" + editedLot);

        menuConfig.EDIT_LOT_MENU.open();
        menuUtil.readCommand(menuConfig.EDIT_LOT_MENU);
        return null;
    });

    Command EDIT_TITLE_COMMAND = new Command("title", "изменить название.", () -> {
        editedLot.setTitle(menuUtil.readStringValue("Введите название лота:"));

        lotService.update(editedLot);

        System.out.println("Название изменено.\n");
        menuUtil.readCommand(menuConfig.EDIT_LOT_MENU);
        return null;
    });

    Command EDIT_DESCRIPTION_COMMAND = new Command("desc", "изменить описание.", () -> {
        editedLot.setDescription(menuUtil.readStringValue("Введите описание лота:"));

        lotService.update(editedLot);

        System.out.println("Описание изменено.\n");
        menuUtil.readCommand(menuConfig.EDIT_LOT_MENU);
        return null;
    });

    Command EDIT_STEP_COMMAND = new Command("step", "изменить минимальный шаг.", () -> {
        editedLot.setStep(new BigDecimal((menuUtil.readNumericValue("Введите минимальный шаг цены лота:"))));

        lotValidator.validateStep(editedLot);
        lotService.update(editedLot);

        System.out.println("Шаг изменен.\n");
        menuUtil.readCommand(menuConfig.EDIT_LOT_MENU);
        return null;
    });

    Command EDIT_CLOSES_COMMAND = new Command("close", "изменить дату окончания торгов.", () -> {
        editedLot.setCloses(menuUtil.readDateTimeValue("Введите дату и время окончания торгов.\nКорректный формат: гггг-ММ-дд ЧЧ:мм\nПример: 2020-07-21 16:30"));

        lotValidator.validateClosingDate(editedLot);
        lotService.update(editedLot);

        System.out.println("Дата оконачания торгов изменена.\n");
        menuUtil.readCommand(menuConfig.EDIT_LOT_MENU);
        return null;
    });

    Command EDIT_CATEGORY_COMMAND = new Command("cat", "изменить категорию.", () -> {

        System.out.println("Доступные категории:");
        categoryService.findAll().forEach(System.out::println);

        editedLot.setCategory(categoryService.findById(Integer.parseInt(menuUtil.readNumericValue("\nВведите id категории лота:"))));

        lotValidator.validateCategory(editedLot);
        lotService.update(editedLot);

        System.out.println("Категория изменена.\n");
        menuUtil.readCommand(menuConfig.EDIT_LOT_MENU);
        return null;
    });

    Command EDIT_TOWN_COMMAND = new Command("town", "изменить город.", () -> {

        System.out.println("Доступные города:");
        townService.findAll().forEach(System.out::println);

        editedLot.setTown(townService.findById(Integer.parseInt(menuUtil.readNumericValue("\nВведите id города, где находится лот:"))));

        lotValidator.validateTown(editedLot);
        lotService.update(editedLot);

        System.out.println("Город изменен.\n");
        menuUtil.readCommand(menuConfig.EDIT_LOT_MENU);
        return null;
    });

    Command DELETE_LOT_COMMAND = new Command("del", "удалить лот.", () -> {

        lotService.delete(editedLot.getId());
        System.out.println("Лот удален.");

        Role role = authentication.getUserRole();
        menuUtil.getMainMenuByRole(role).open();
        menuUtil.readCommand(menuUtil.getMainMenuByRole(role));
        return null;
    });

    Command BACK_COMMAND = new Command("back", "назад в главное меню.", () -> {

        Role role = authentication.getUserRole();
        menuUtil.getMainMenuByRole(role).open();
        menuUtil.readCommand(menuUtil.getMainMenuByRole(role));
        return null;
    });

    Command REGISTER_COMMAND = new Command("reg", "зарегистрироваться.", () -> {

        String email = menuUtil.readEmailValue("Введите email:");
        String password = menuUtil.readPasswordValue("Введите пароль (не короче пяти символов):");
        String username = menuUtil.readStringValue("Введите имя пользователя:");

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setUsername(username);

        user = userService.registerUser(user);
        System.out.println("Пользователь зарегистрирован:\n" + user);

        menuUtil.readCommand(menuConfig.WELCOME_MENU);
        return null;
    });

    Command BID_COMMAND = new Command("bid", "сделать ставку.", () -> {

        Integer lotId = Integer.parseInt(menuUtil.readNumericValue("Введите id лота:"));
        BigDecimal bidValue = new BigDecimal(menuUtil.readNumericValue("Введите размер ставки:"));

        Bid bid = new Bid();
        bid.setLot(lotService.findById(lotId));
        bid.setValue(bidValue);

        bid = bidService.makeBid(bid);
        System.out.println("Ставка сделана:\n" + bid);

        menuUtil.readCommand(menuConfig.MAIN_MENU);
        return null;
    });

    Command VIEW_USER_MESSAGES = new Command("mes", "посмотреть сообщения.", () -> {

        Integer userId = authentication.getUserId();

        messageService.prepareUserMessages(userId);

        System.out.println("Ваши сообщения:");
        List<Message> userMessages = messageService.findMessagesByUserId(userId);
        Collections.reverse(userMessages);
        userMessages.forEach(System.out::println);

        menuUtil.readCommand(menuConfig.MAIN_MENU);
        return null;
    });

}
