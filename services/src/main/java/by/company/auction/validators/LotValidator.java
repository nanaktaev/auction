package by.company.auction.validators;

import by.company.auction.common.exceptions.BusinessException;
import by.company.auction.common.exceptions.NoSuchEntityException;
import by.company.auction.dto.LotDto;
import by.company.auction.services.CategoryService;
import by.company.auction.services.TownService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Component
public class LotValidator {

    @Autowired
    private TownService townService;
    @Autowired
    private CategoryService categoryService;

    public void validate(LotDto lotDto) {

        log.debug("validate() lotDto = {}", lotDto);

        validateNullProperties(lotDto);
        validateStartPrice(lotDto);
        validateStep(lotDto);
        validateClosingDate(lotDto);
        validateCategory(lotDto);
        validateTown(lotDto);
    }

    public void validateUpdate(LotDto lotDto) {

        log.debug("validateUpdate() lotDto = {}", lotDto);

        validateClosingDate(lotDto);
        validateCategory(lotDto);
        validateTown(lotDto);
    }


    private void validateTown(LotDto lotDto) {
        if (!townService.exists(lotDto.getTown().getId())) {
            throw new NoSuchEntityException("The town was not found by this id.");
        }
    }

    private void validateCategory(LotDto lotDto) {
        if (!categoryService.exists(lotDto.getCategory().getId())) {
            throw new NoSuchEntityException("The category was not found by this id.");
        }
    }

    private void validateClosingDate(LotDto lotDto) {
        if (!lotDto.getCloses().isAfter(LocalDateTime.now().plusMinutes(3))) {
            throw new BusinessException("Bidding cannot last less than 3 minutes.");
        }
    }

    private void validateStep(LotDto lotDto) {
        if (lotDto.getStep().compareTo(new BigDecimal(1)) < 0) {
            throw new BusinessException("Minimal step cannot be less than 1.");
        }
    }

    private void validateStartPrice(LotDto lotDto) {
        if (lotDto.getPriceStart().compareTo(new BigDecimal(1)) < 0) {
            throw new BusinessException("Minimal starting price cannot be less than 1.");
        }
    }

    private void validateNullProperties(LotDto lotDto) {

        if (lotDto.getPriceStart() == null) {
            throw new NullPointerException("Starting price is necessary.");
        }
        if (lotDto.getCloses() == null) {
            throw new NullPointerException("Closing date is necessary.");
        }
        if (lotDto.getStep() == null) {
            throw new NullPointerException("Step is necessary.");
        }
        if (lotDto.getTown() == null) {
            throw new NullPointerException("Town is necessary.");
        }
        if (lotDto.getTitle() == null) {
            throw new NullPointerException("Title is necessary.");
        }
        if (lotDto.getDescription() == null) {
            throw new NullPointerException("Description is necessary.");
        }
        if (lotDto.getCategory() == null) {
            throw new NullPointerException("Category is necessary.");
        }
    }
}
