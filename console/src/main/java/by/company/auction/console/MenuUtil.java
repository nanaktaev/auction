package by.company.auction.console;

import by.company.auction.common.exceptions.AuctionException;
import by.company.auction.common.exceptions.BusinessException;
import by.company.auction.model.Role;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

@Component
public class MenuUtil {

    @Autowired
    private MenuConfig menuConfig;

    private static Scanner scanner = new Scanner(System.in);

    @SuppressWarnings("InfiniteRecursion")
    public void readCommand(Menu menu) {

        String commandName = scanner.nextLine();

        menu.getCommands().stream()
                .filter(command -> command.getName().equals(commandName))
                .findFirst().ifPresent(command -> {
            try {
                command.run();
            } catch (AuctionException e) {
                System.out.println(e.getMessage());
                readCommand(menu);
            }
        });

        System.out.println("Введена несуществующая команда.\n");
        readCommand(menu);

    }

    String readStringValue(String valueMessage) {
        System.out.println(valueMessage);
        String value = scanner.nextLine();
        if ("cancel".equals(value)) {
            throw new BusinessException("Действие отменено.");
        }
        if (StringUtils.isBlank(value)) {
            System.out.println("Ошибка. Введена пустая строка.\nДля отмены ввода введите cancel.\n");
            return readStringValue(valueMessage);
        }
        return value;
    }

    String readNumericValue(String valueMessage) {
        String value = readStringValue(valueMessage);
        if (!StringUtils.isNumeric(value)) {
            System.out.println("Ошибка. Введены символы, не являющиеся цифрами.\nДля отмены ввода введите cancel.\n");
            return readNumericValue(valueMessage);
        }
        return value;
    }

    String readRoleValue(String valueMessage) {
        String value = readStringValue(valueMessage);
        if (!EnumUtils.isValidEnum(Role.class, value.toUpperCase())) {
            System.out.println("Ошибка. Роль введена некорректно, доступные роли: USER, VENDOR, ADMIN.\nДля отмены ввода введите cancel.\n");
            return readRoleValue(valueMessage);
        }
        return value.toUpperCase();
    }

    LocalDateTime readDateTimeValue(String valueMessage) {
        String value = readStringValue(valueMessage);
        LocalDateTime dateTimeValue;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            dateTimeValue = LocalDateTime.parse(value, formatter);
        } catch (Exception exception) {
            System.out.println("Ошибка. Дата введена некорректно.\nДля отмены ввода введите cancel.\n");
            return readDateTimeValue(valueMessage);
        }
        return dateTimeValue;
    }

    String readEmailValue(String valueMessage) {
        String value = readStringValue(valueMessage);
        if (!EmailValidator.getInstance().isValid(value)) {
            System.out.println("Ошибка. Email введен некорректно.\nДля отмены ввода введите cancel.\n");
            return readEmailValue(valueMessage);
        }
        return value;
    }

    String readPasswordValue(String valueMessage) {
        String value = readStringValue(valueMessage);
        if (value.length() < 5) {
            System.out.println("Ошибка. Пароль должен быть не короче 5 символов.\nДля отмены ввода введите cancel.\n");
            return readPasswordValue(valueMessage);
        }
        return value;
    }

    Menu getMainMenuByRole(Role role) {
        if (role.equals(Role.USER)) {
            return menuConfig.MAIN_MENU;
        } else if (role.equals(Role.VENDOR)) {
            return menuConfig.MAIN_MENU_VENDOR;
        } else {
            return menuConfig.MAIN_MENU_ADMIN;
        }
    }
}

