package by.company.auction.console;

import by.company.auction.console.menu.MenuUtil;

import java.util.TimeZone;

import static by.company.auction.console.menu.MenuConfig.WELCOME_MENU;

public class RunAuction {
    public static void main(String[] args) {

        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"));
        WELCOME_MENU.open();
        MenuUtil.readCommand(WELCOME_MENU);

    }
}