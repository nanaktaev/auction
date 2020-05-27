package by.company.auction.validators;

import by.company.auction.AbstractTest;
import by.company.auction.common.exceptions.BusinessException;
import by.company.auction.common.exceptions.NoSuchEntityException;
import by.company.auction.dto.BidDto;
import by.company.auction.dto.LotDto;
import by.company.auction.dto.UserDto;
import by.company.auction.services.BidService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

public class BidValidatorTest extends AbstractTest {

    @Mock
    private BidService bidService;
    @InjectMocks
    private BidValidator bidValidator;

    private static BidDto topBidDto;
    private static LotDto lotDto;

    @BeforeClass
    public static void beforeAllTests() {

        lotDto = new LotDto();
        lotDto.setId(1);
        lotDto.setPrice(new BigDecimal(90));
        lotDto.setStep(new BigDecimal(10));

        UserDto userDto = new UserDto();
        userDto.setId(1);

        topBidDto = new BidDto();
        topBidDto.setUser(userDto);
    }

    @Test
    public void validateBidValueSuccess() {

        bidValidator.validateBidValue(lotDto, new BigDecimal(100));
    }

    @Test(expected = BusinessException.class)
    public void validateBidValueFailure() {

        bidValidator.validateBidValue(lotDto, new BigDecimal(90));
    }

    @Test
    public void validateTopBidSuccess() {

        when(bidService.findTopBidByLotId(1)).thenReturn(topBidDto);

        bidValidator.validateTopBid(lotDto.getId(), 2);
    }

    @Test(expected = BusinessException.class)
    public void validateTopBidFailure() {

        when(bidService.findTopBidByLotId(1)).thenReturn(topBidDto);

        bidValidator.validateTopBid(lotDto.getId(), 1);
    }

    @Test
    public void validateLotClosingDateSuccess() {

        lotDto.setCloses(LocalDateTime.now().plusMinutes(1));
        bidValidator.validateLotClosingDate(lotDto);
    }

    @Test(expected = BusinessException.class)
    public void validateLotClosingDateFailure() {

        lotDto.setCloses(LocalDateTime.now().minusMinutes(1));
        bidValidator.validateLotClosingDate(lotDto);
    }

    @Test
    public void validateLotExistenceSuccess() {

        bidValidator.validateLotExistence(lotDto);
    }

    @Test(expected = NoSuchEntityException.class)
    public void validateLotExistenceFailure() {

        bidValidator.validateLotExistence(null);
    }
}