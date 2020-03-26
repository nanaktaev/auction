package by.company.auction.console.menu;

import by.company.auction.model.Role;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static by.company.auction.console.menu.MenuContainer.*;
import static by.company.auction.console.menu.MenuContainer.MAIN_MENU_ADMIN;

public class MenuUtil {

    private static Scanner scanner = new Scanner(System.in);

    public static void readCommand(Menu menu) {
        String commandName = scanner.nextLine();

        for (Command command : menu.getCommands()) {
            if (command.getName().equals(commandName)) {
                try {
                    command.run();
                } catch (IllegalStateException exception) {
                    System.out.println(exception.getMessage() + '\n');
                    readCommand(menu);
                }
                return;
            }
        }
        System.out.println("Введена несуществующая команда.\n");
        readCommand(menu);
    }

    static String readStringValue(String valueMessage) {
        System.out.println(valueMessage);
        String value = scanner.nextLine();
        if ("cancel".equals(value)) {
            throw new IllegalStateException("Действие отменено.");
        }
        if (StringUtils.isBlank(value)) {
            System.out.println("Ошибка. Введена пустая строка.\nДля отмены ввода введите cancel.\n");
            return readStringValue(valueMessage);
        }
        return value;
    }

    static String readNumericValue(String valueMessage) {
        String value = readStringValue(valueMessage);
        if (!StringUtils.isNumeric(value)) {
            System.out.println("Ошибка. Введены символы, не являющиеся цифрами.\nДля отмены ввода введите cancel.\n");
            return readNumericValue(valueMessage);
        }
        return value;
    }

    static String readRoleValue(String valueMessage) {
        String value = readStringValue(valueMessage);
        if (!EnumUtils.isValidEnum(Role.class, value.toUpperCase())) {
            System.out.println("Ошибка. Роль введена некорректно, доступные роли: USER, VENDOR, ADMIN.\nДля отмены ввода введите cancel.\n");
            return readRoleValue(valueMessage);
        }
        return value.toUpperCase();
    }

    static LocalDateTime readDateTimeValue(String valueMessage) {
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

    static String readEmailValue(String valueMessage) {
        String value = readStringValue(valueMessage);
        if (!EmailValidator.getInstance().isValid(value)) {
            System.out.println("Ошибка. Email введен некорректно.\nДля отмены ввода введите cancel.\n");
            return readEmailValue(valueMessage);
        }
        return value;
    }

    static String readPasswordValue(String valueMessage) {
        String value = readStringValue(valueMessage);
        if (value.length() < 5) {
            System.out.println("Ошибка. Пароль должен быть не короче 5 символов.\nДля отмены ввода введите cancel.\n");
            return readPasswordValue(valueMessage);
        }
        return value;
    }

    static Menu getMainMenuByRole(Role role) {
        if (role.equals(Role.USER)) {
            return MAIN_MENU;
        } else if (role.equals(Role.VENDOR)) {
            return MAIN_MENU_VENDOR;
        } else {
            return MAIN_MENU_ADMIN;
        }
    }
}

