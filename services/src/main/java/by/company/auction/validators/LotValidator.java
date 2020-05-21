package by.company.auction.validators;

import by.company.auction.common.exceptions.BusinessException;
import by.company.auction.common.exceptions.NoSuchEntityException;
import by.company.auction.model.Lot;
import by.company.auction.services.CategoryService;
import by.company.auction.services.TownService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Log4j2
@Component
public class LotValidator {

    @Autowired
    private TownService townService;
    @Autowired
    private CategoryService categoryService;

    public void validate(Lot lot) {

        log.debug("validate() lot = {}", lot);

        validateStartPrice(lot);
        validateStep(lot);
        validateClosingDate(lot);
        validateCategory(lot);
        validateTown(lot);
    }

    public void validateTown(Lot lot) {
        if (!townService.exists(lot.getTown().getId())) {
            throw new NoSuchEntityException("Ошибка. Города с таким id не существует.");
        }
    }

    public void validateCategory(Lot lot) {
        if (!categoryService.exists(lot.getCategory().getId())) {
            throw new NoSuchEntityException("Ошибка. Категории с таким id не существует.");
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
        if (!lot.getCompany().getId().equals(companyId)) {
            throw new BusinessException("Ошибка. Вы не можете редактировать чужой лот.");
        }
    }

}
