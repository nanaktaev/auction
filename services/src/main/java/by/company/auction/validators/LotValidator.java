package by.company.auction.validators;

import by.company.auction.common.exceptions.BusinessException;
import by.company.auction.common.exceptions.NotFoundException;
import by.company.auction.model.Lot;
import by.company.auction.services.CategoryService;
import by.company.auction.services.TownService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class LotValidator {

    private final Logger LOGGER = LogManager.getLogger(LotValidator.class);

    public void validate(Lot lot) {

        LOGGER.debug("validate() lot = {}", lot);

        validateStartPrice(lot);
        validateStep(lot);
        validateClosingDate(lot);
        validateCategory(lot);
        validateTown(lot);
    }

    public void validateTown(Lot lot) {
        if (TownService.getInstance().findById(lot.getTownId()) == null) {
            throw new NotFoundException("Ошибка. Города с таким id не существует.");
        }
    }

    public void validateCategory(Lot lot) {
        if (CategoryService.getInstance().findById(lot.getCategoryId()) == null) {
            throw new NotFoundException("Ошибка. Категории с таким id не существует.");
        }
    }

    public void validateClosingDate(Lot lot) {
        if (!lot.getCloses().isAfter(LocalDateTime.now().plusMinutes(3))) {
            throw new BusinessException("Ошибка. Торги не могут длиться меньше 3 минут (или оканчиваться в прошлом).");
        }
    }

    public void validateStep(Lot lot) {
        if (lot.getStep().compareTo(new BigDecimal(1)) < 0) {
            throw new BusinessException("Ошибка. Минимальный шаг цены лота не может быть меньше 1.");
        }
    }

    @SuppressWarnings("WeakerAccess")
    public void validateStartPrice(Lot lot) {
        if (lot.getPriceStart().compareTo(new BigDecimal(1)) < 0) {
            throw new BusinessException("Ошибка. Начальная цена лота не может быть ниже 1.");
        }
    }

    public void validateOwnership(Lot lot, Integer companyId) {
        if (lot == null) {
            throw new NotFoundException("Ошибка. Лота с таким id не существует.");
        }
        if (lot.getCompanyId() != companyId) {
            throw new BusinessException("Ошибка. Вы не можете редактировать чужой лот.");
        }
    }

    private static LotValidator lotValidatorInstance;

    public static LotValidator getInstance() {
        if (lotValidatorInstance != null) {
            return lotValidatorInstance;
        }
        lotValidatorInstance = new LotValidator();
        return lotValidatorInstance;
    }

}
