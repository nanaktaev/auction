package by.company.auction.validators;

import by.company.auction.AbstractTest;
import by.company.auction.common.exceptions.BusinessException;
import by.company.auction.common.exceptions.NoSuchEntityException;
import by.company.auction.model.Bid;
import by.company.auction.model.Lot;
import by.company.auction.model.User;
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

    private static Bid topBid;
    private static Lot lot;

    @BeforeClass
    public static void beforeEachTest() {

        lot = new Lot();
        lot.setId(1);
        lot.setPrice(new BigDecimal(90));
        lot.setStep(new BigDecimal(10));

        User user = new User();
        user.setId(1);

        topBid = new Bid();
        topBid.setUser(user);

    }

    @Test
    public void validateBidValueSuccess() {

        bidValidator.validateBidValue(lot, new BigDecimal(100));

    }

    @Test(expected = BusinessException.class)
    public void validateBidValueFailure() {

        bidValidator.validateBidValue(lot, new BigDecimal(90));

    }

    @Test
    public void validateTopBidSuccess() {

        when(bidService.findTopBidByLotId(1)).thenReturn(topBid);

        bidValidator.validateTopBid(lot.getId(), 2);

    }

    @Test(expected = BusinessException.class)
    public void validateTopBidFailure() {

        when(bidService.findTopBidByLotId(1)).thenReturn(topBid);

        bidValidator.validateTopBid(lot.getId(), 1);

    }

    @Test
    public void validateLotClosingDateSuccess() {

        lot.setCloses(LocalDateTime.now().plusMinutes(1));
        bidValidator.validateLotClosingDate(lot);

    }

    @Test(expected = BusinessException.class)
    public void validateLotClosingDateFailure() {

        lot.setCloses(LocalDateTime.now().minusMinutes(1));
        bidValidator.validateLotClosingDate(lot);

    }

    @Test
    public void validateLotExistenceSuccess() {

        bidValidator.validateLotExistence(lot);

    }

    @Test(expected = NoSuchEntityException.class)
    public void validateLotExistenceFailure() {

        bidValidator.validateLotExistence(null);

    }

}