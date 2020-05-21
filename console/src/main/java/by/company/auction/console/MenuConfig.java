package by.company.auction.console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static by.company.auction.console.HeaderConfig.WELCOME_MENU_HEADER;

@Component
public class MenuConfig {

    @Autowired
    private CommandConfig commandConfig;

    public Menu WELCOME_MENU;
    Menu MAIN_MENU;
    Menu MAIN_MENU_ADMIN;
    Menu MAIN_MENU_VENDOR;
    Menu EDIT_LOT_MENU;

    @PostConstruct
    public void menuInit() {
        WELCOME_MENU = new Menu(
                WELCOME_MENU_HEADER,
                commandConfig.LOGIN_COMMAND,
                commandConfig.REGISTER_COMMAND,
                commandConfig.QUIT_COMMAND);
        MAIN_MENU = new Menu("\n*** Главное меню ***",
                commandConfig.VIEW_ALL_LOTS_COMMAND,
                commandConfig.VIEW_LOTS_BY_TOWN,
                commandConfig.VIEW_LOTS_BY_CATEGORY,
                commandConfig.VIEW_BIDS_COMMAND,
                commandConfig.BID_COMMAND,
                commandConfig.VIEW_USER_MESSAGES,
                commandConfig.LOGOUT_COMMAND);
        MAIN_MENU_ADMIN = new Menu("\n*** Главное меню администратора ***",
                commandConfig.VIEW_ALL_LOTS_COMMAND,
                commandConfig.VIEW_LOTS_BY_TOWN,
                commandConfig.VIEW_LOTS_BY_CATEGORY,
                commandConfig.VIEW_BIDS_COMMAND,
                commandConfig.EDIT_LOT_COMMAND,
                commandConfig.SET_ROLE_COMMAND,
                commandConfig.CREATE_TOWN_COMMAND,
                commandConfig.CREATE_CATEGORY_COMMAND,
                commandConfig.CREATE_COMPANY_COMMAND,
                commandConfig.LOGOUT_COMMAND);
        MAIN_MENU_VENDOR = new Menu("\n*** Главное меню продавца ***",
                commandConfig.VIEW_ALL_LOTS_COMMAND,
                commandConfig.VIEW_LOTS_BY_TOWN,
                commandConfig.VIEW_LOTS_BY_CATEGORY,
                commandConfig.VIEW_BIDS_COMMAND,
                commandConfig.EDIT_LOT_COMMAND,
                commandConfig.CREATE_LOT_COMMAND,
                commandConfig.LOGOUT_COMMAND);
        EDIT_LOT_MENU = new Menu("*** Редактирование лота ***",
                commandConfig.EDIT_TITLE_COMMAND,
                commandConfig.EDIT_DESCRIPTION_COMMAND,
                commandConfig.EDIT_STEP_COMMAND,
                commandConfig.EDIT_CLOSES_COMMAND,
                commandConfig.EDIT_CATEGORY_COMMAND,
                commandConfig.EDIT_TOWN_COMMAND,
                commandConfig.DELETE_LOT_COMMAND,
                commandConfig.BACK_COMMAND);
    }

}

