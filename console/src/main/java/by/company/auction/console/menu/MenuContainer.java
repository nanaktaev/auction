package by.company.auction.console.menu;

import static by.company.auction.console.menu.CommandContainer.*;
import static by.company.auction.console.menu.HeaderContainer.WELCOME_MENU_HEADER;

public class MenuContainer {

    public static final Menu WELCOME_MENU = new Menu(WELCOME_MENU_HEADER, LOGIN_COMMAND, REGISTER_COMMAND, QUIT_COMMAND);
    static final Menu MAIN_MENU = new Menu("\n*** Главное меню ***", VIEW_ALL_LOTS_COMMAND, VIEW_LOTS_BY_TOWN, VIEW_LOTS_BY_CATEGORY, VIEW_BIDS_COMMAND, BID_COMMAND, VIEW_USER_MESSAGES, LOGOUT_COMMAND);
    static final Menu MAIN_MENU_ADMIN = new Menu("\n*** Главное меню администратора ***", VIEW_ALL_LOTS_COMMAND, VIEW_LOTS_BY_TOWN, VIEW_LOTS_BY_CATEGORY, VIEW_BIDS_COMMAND, EDIT_LOT_COMMAND, SET_ROLE_COMMAND, LOGOUT_COMMAND);
    static final Menu MAIN_MENU_VENDOR = new Menu("\n*** Главное меню продавца ***", VIEW_ALL_LOTS_COMMAND, VIEW_LOTS_BY_TOWN, VIEW_LOTS_BY_CATEGORY, VIEW_BIDS_COMMAND, EDIT_LOT_COMMAND, CREATE_LOT_COMMAND, LOGOUT_COMMAND);
    static final Menu EDIT_LOT_MENU = new Menu("*** Редактирование лота ***", EDIT_TITLE_COMMAND, EDIT_DESCRIPTION_COMMAND, EDIT_STEP_COMMAND, EDIT_CLOSES_COMMAND, EDIT_CATEGORY_COMMAND, EDIT_TOWN_COMMAND, DELETE_LOT_COMMAND, BACK_COMMAND);
}

