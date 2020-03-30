package by.company.auction.validators;

import by.company.auction.model.Lot;
import by.company.auction.services.CategoryService;
import by.company.auction.services.TownService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class LotValidator {

    public static void validate(Lot lot) {
        validateStartPrice(lot);
        validateStep(lot);
        validateClosingDate(lot);
        validateCategory(lot);
        validateTown(lot);
    }

    public static void validateTown(Lot lot) {
        if (TownService.getInstance().findById(lot.getTownId()) == null) {
            throw new IllegalStateException("Ошибка. Города с таким id не существует.");
        }
    }

    public static void validateCategory(Lot lot) {
        if (CategoryService.getInstance().findById(lot.getCategoryId()) == null) {
            throw new IllegalStateException("Ошибка. Категории с таким id не существует.");
        }
    }

    public static void validateClosingDate(Lot lot) {
        if (!lot.getCloses().isAfter(LocalDateTime.now().plusMinutes(3))) {
            throw new IllegalStateException("Ошибка. Торги не могут длиться меньше 3 минут (или оканчиваться в прошлом).");
        }
    }

    public static void validateStep(Lot lot) {
        if (lot.getStep().compareTo(new BigDecimal(1)) < 0) {
            throw new IllegalStateException("Ошибка. Минимальный шаг цены лота не может быть меньше 1.");
        }
    }

    private static void validateStartPrice(Lot lot) {
        if (lot.getPriceStart().compareTo(new BigDecimal(1)) < 0) {
            throw new IllegalStateException("Ошибка. Начальная цена лота не может быть ниже 1.");
        }
    }

    public static void validateOwnership(Lot lot, Integer userId) {
        if (lot == null) {
            throw new IllegalStateException("Ошибка. Лота с таким id не существует.");
        }
        if (lot.getVendorId() != userId) {
            throw new IllegalStateException("Ошибка. Вы не можете редактировать чужой лот.");
        }
    }
}
