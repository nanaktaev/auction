package by.company.auction.validators;

import by.company.auction.common.exceptions.BusinessException;
import by.company.auction.common.exceptions.NoSuchEntityException;
import by.company.auction.dto.BidDto;
import by.company.auction.dto.LotDto;
import by.company.auction.services.BidService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Component
public class BidValidator {

    @Autowired
    private BidService bidService;

    public void validate(LotDto lotDto, BidDto bidDto, Integer userId) {

        log.debug("validate() lotDto = {}, bidDto = {}, userId = {}", lotDto, bidDto, userId);

        validateLotExistence(lotDto);
        validateLotClosingDate(lotDto);
        validateTopBid(lotDto.getId(), userId);
        validateBidValue(lotDto, bidDto.getValue());
    }

    @SuppressWarnings("WeakerAccess")
    public void validateBidValue(LotDto lotDto, BigDecimal value) {

        if (!isBidValueEnough(lotDto, value)) {
            throw new BusinessException("Your bid is not high enough.");
        }
    }

    @SuppressWarnings("WeakerAccess")
    public void validateTopBid(Integer lotId, Integer userId) {

        BidDto topBid = bidService.findTopBidByLotId(lotId);

        if (topBid != null && userId.equals(topBid.getUser().getId())) {
            throw new BusinessException("Your bid is already at the top.");
        }
    }

    @SuppressWarnings("WeakerAccess")
    public void validateLotClosingDate(LotDto lotDto) {

        if (isLotExpired(lotDto)) {
            throw new BusinessException("Bidding on this lot has already been finished.");
        }
    }

    @SuppressWarnings("WeakerAccess")
    public void validateLotExistence(LotDto lotDto) {

        if (lotDto == null) {
            throw new NoSuchEntityException("The lot was not found by this id.");
        }
    }

    private boolean isLotExpired(LotDto lotDto) {
        return LocalDateTime.now().isAfter(lotDto.getCloses());
    }

    public boolean isLotBurning(LotDto lotDto) {
        return !isLotExpired(lotDto) && LocalDateTime.now().plusMinutes(3).isAfter(lotDto.getCloses());
    }

    private boolean isBidValueEnough(LotDto lotDto, BigDecimal value) {
        return value.compareTo(lotDto.getPrice().add(lotDto.getStep())) >= 0;
    }
}
