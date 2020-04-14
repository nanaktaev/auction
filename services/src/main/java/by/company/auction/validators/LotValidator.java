package by.company.auction.validators;

import by.company.auction.exceptions.AuctionException;
import by.company.auction.model.Lot;
import by.company.auction.services.CategoryService;
import by.company.auction.services.TownService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class LotValidator {

    public void validate(Lot lot) {
        validateStartPrice(lot);
        validateStep(lot);
        validateClosingDate(lot);
        validateCategory(lot);
        validateTown(lot);
    }

    public void validateTown(Lot lot) {
        if (TownService.getInstance().findById(lot.getTownId()) == null) {
            throw new AuctionException("Ошибка. Города с таким id не существует.");
        }
    }

    public void validateCategory(Lot lot) {
        if (CategoryService.getInstance().findById(lot.getCategoryId()) == null) {
            throw new AuctionException("Ошибка. Категории с таким id не существует.");
        }
    }

    public void validateClosingDate(Lot lot) {
        if (!lot.getCloses().isAfter(LocalDateTime.now().plusMinutes(3))) {
            throw new AuctionException("Ошибка. Торги не могут длиться меньше 3 минут (или оканчиваться в прошлом).");
        }
    }

    public void validateStep(Lot lot) {
        if (lot.getStep().compareTo(new BigDecimal(1)) < 0) {
            throw new AuctionException("Ошибка. Минимальный шаг цены лота не может быть меньше 1.");
        }
    }

    @SuppressWarnings("WeakerAccess")
    public void validateStartPrice(Lot lot) {
        if (lot.getPriceStart().compareTo(new BigDecimal(1)) < 0) {
            throw new AuctionException("Ошибка. Начальная цена лота не может быть ниже 1.");
        }
    }

    public void validateOwnership(Lot lot, Integer companyId) {
        if (lot == null) {
            throw new AuctionException("Ошибка. Лота с таким id не существует.");
        }
        if (lot.getCompanyId() != companyId) {
            throw new AuctionException("Ошибка. Вы не можете редактировать чужой лот.");
        }
    }

    private static LotValidator lotValidatorInstance;

    private LotValidator() {
    }

    public static LotValidator getInstance() {
        if (lotValidatorInstance != null) {
            return lotValidatorInstance;
        }
        lotValidatorInstance = new LotValidator();
        return lotValidatorInstance;
    }

}
