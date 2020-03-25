package by.company.auction.console;

import by.company.auction.console.menu.MenuUtil;

import static by.company.auction.console.menu.MenuContainer.WELCOME_MENU;

public class RunAuction {
    public static void main(String[] args) {
        SampleEntitiesCreator.createSampleEntities();
        WELCOME_MENU.open();
        MenuUtil.readCommand(WELCOME_MENU);
    }
}
