package by.company.auction.validators;

import by.company.auction.AbstractTest;
import by.company.auction.common.exceptions.BusinessException;
import by.company.auction.common.exceptions.NotFoundException;
import by.company.auction.model.Bid;
import by.company.auction.model.Lot;
import by.company.auction.services.BidService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BidValidatorTest extends AbstractTest {

    private BidService bidService;
    private BidValidator bidValidator;
    private Bid topBid;
    private Lot lot;

    @Before
    public void beforeEachTest() {

        PowerMockito.mockStatic(BidService.class);
        PowerMockito.when(BidService.getInstance()).thenReturn(mock(BidService.class));
        MockitoAnnotations.initMocks(this);

        bidValidator = BidValidator.getInstance();
        bidService = BidService.getInstance();

        lot = new Lot();
        lot.setId(1);
        lot.setPrice(new BigDecimal(90));
        lot.setStep(new BigDecimal(10));

        topBid = new Bid();
        topBid.setUserId(1);

    }

    @Test
    @PrepareForTest({BidService.class, BidValidator.class})
    public void validateBidValueSuccess() {

        bidValidator.validateBidValue(lot, new BigDecimal(100));

    }

    @Test(expected = BusinessException.class)
    @PrepareForTest({BidService.class, BidValidator.class})
    public void validateBidValueFailure() {

        bidValidator.validateBidValue(lot, new BigDecimal(90));

    }

    @Test
    @PrepareForTest({BidService.class, BidValidator.class})
    public void validateTopBidSuccess() {

        when(bidService.findTopBidByLotId(anyInt())).thenReturn(topBid);

        bidValidator.validateTopBid(lot.getId(), 2);

    }

    @Test(expected = BusinessException.class)
    @PrepareForTest({BidService.class, BidValidator.class})
    public void validateTopBidFailure() {

        when(bidService.findTopBidByLotId(anyInt())).thenReturn(topBid);

        bidValidator.validateTopBid(lot.getId(), 1);

    }

    @Test
    @PrepareForTest({BidService.class, BidValidator.class})
    public void validateLotClosingDateSuccess() {

        lot.setCloses(LocalDateTime.now().plusMinutes(1));
        bidValidator.validateLotClosingDate(lot);

    }

    @Test(expected = BusinessException.class)
    @PrepareForTest({BidService.class, BidValidator.class})
    public void validateLotClosingDateFailure() {

        lot.setCloses(LocalDateTime.now().minusMinutes(1));
        bidValidator.validateLotClosingDate(lot);

    }

    @Test
    @PrepareForTest({BidService.class, BidValidator.class})
    public void validateLotExistenceSuccess() {

        bidValidator.validateLotExistence(lot);

    }

    @Test(expected = NotFoundException.class)
    @PrepareForTest({BidService.class, BidValidator.class})
    public void validateLotExistenceFailure() {

        bidValidator.validateLotExistence(null);

    }

}