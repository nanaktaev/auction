package by.company.auction.services;

import by.company.auction.AbstractTest;
import by.company.auction.common.exceptions.NotFoundException;
import by.company.auction.common.security.Authentication;
import by.company.auction.dao.BidDao;
import by.company.auction.model.Bid;
import by.company.auction.model.Lot;
import by.company.auction.validators.BidValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static by.company.auction.common.security.AuthenticationConfig.authentication;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class BidServiceTest extends AbstractTest {

    private Bid newBid;
    private Bid topBid;
    private Lot lot;
    private Lot burningLot;
    private List<Bid> bids;

    private BidService bidService;
    private LotService lotService;
    private MessageService messageService;
    private BidValidator bidValidator;
    private BidDao bidDao;

    @Before
    public void beforeEachTest() {

        PowerMockito.mockStatic(LotService.class);
        PowerMockito.mockStatic(BidValidator.class);
        PowerMockito.mockStatic(BidDao.class);
        PowerMockito.mockStatic(MessageService.class);
        PowerMockito.when(LotService.getInstance()).thenReturn(mock(LotService.class));
        PowerMockito.when(BidValidator.getInstance()).thenReturn(mock(BidValidator.class));
        PowerMockito.when(BidDao.getInstance()).thenReturn(mock(BidDao.class));
        PowerMockito.when(MessageService.getInstance()).thenReturn(mock(MessageService.class));
        MockitoAnnotations.initMocks(this);

        bidService = BidService.getInstance();
        lotService = LotService.getInstance();
        messageService = MessageService.getInstance();
        bidValidator = BidValidator.getInstance();
        bidDao = BidDao.getInstance();

        newBid = new Bid();
        newBid.setValue(new BigDecimal(110));
        newBid.setLotId(1);

        topBid = new Bid();
        topBid.setUserId(2);

        bids = Collections.singletonList(topBid);

        lot = new Lot();
        lot.setId(1);
        lot.setCloses(LocalDateTime.now().minusDays(1));

        burningLot = new Lot();
        burningLot.setId(1);
        burningLot.setCloses(LocalDateTime.now().minusSeconds(30));

        authentication.setUserId(1);

    }

    @Test
    @PrepareForTest({BidService.class, BidValidator.class, BidDao.class, LotService.class, MessageService.class, Authentication.class})
    public void makeBid() {

        when(lotService.findById(anyInt())).thenReturn(lot);
        doNothing().when(bidValidator).validate(any(Lot.class), any(Bid.class), anyInt());
        when(bidDao.findTopBidByLotId(anyInt())).thenReturn(topBid);
        when(bidDao.create(newBid)).thenReturn(newBid);
        when(lotService.update(lot)).thenReturn(lot);
        doNothing().when(messageService).createWarningMessage(anyInt(), any(Bid.class));

        newBid = bidService.makeBid(newBid);

        verify(messageService, times(1)).createWarningMessage(anyInt(), any(Bid.class));
        assertEquals(java.util.Optional.of(1), java.util.Optional.of(newBid.getUserId()));
        assertEquals(newBid.getValue(), lot.getPrice());
    }

    @Test
    @PrepareForTest({BidService.class, BidValidator.class, BidDao.class, LotService.class, MessageService.class, Authentication.class})
    public void makeBidOnLotWhileTopBidIsAbsent() {

        when(lotService.findById(anyInt())).thenReturn(burningLot);
        doNothing().when(bidValidator).validate(any(Lot.class), any(Bid.class), anyInt());
        when(bidDao.findTopBidByLotId(anyInt())).thenReturn(null);
        when(bidDao.create(newBid)).thenReturn(newBid);
        when(lotService.update(burningLot)).thenReturn(burningLot);
        doNothing().when(messageService).createWarningMessage(anyInt(), any(Bid.class));

        newBid = bidService.makeBid(newBid);

        verify(messageService, times(0)).createWarningMessage(anyInt(), any(Bid.class));
        assertEquals(java.util.Optional.of(1), java.util.Optional.of(newBid.getUserId()));

    }

    @Test
    @PrepareForTest({BidService.class, BidValidator.class, BidDao.class, LotService.class, MessageService.class, Authentication.class})
    public void makeBidOnBurningLot() {

        when(lotService.findById(anyInt())).thenReturn(burningLot);
        doNothing().when(bidValidator).validate(any(Lot.class), any(Bid.class), anyInt());
        when(bidDao.findTopBidByLotId(anyInt())).thenReturn(topBid);
        when(bidDao.create(newBid)).thenReturn(newBid);
        when(lotService.update(burningLot)).thenReturn(burningLot);
        doNothing().when(messageService).createWarningMessage(anyInt(), any(Bid.class));

        newBid = bidService.makeBid(newBid);

        verify(messageService, times(1)).createWarningMessage(anyInt(), any(Bid.class));
        assertEquals(java.util.Optional.of(1), java.util.Optional.of(newBid.getUserId()));
        assertTrue(LocalDateTime.now().plusMinutes(2).isAfter(burningLot.getCloses()));

    }

    @Test
    @PrepareForTest({BidService.class, BidValidator.class, BidDao.class, LotService.class, MessageService.class, Authentication.class})
    public void isUserLeadingTrue() {

        when(bidService.findTopBidByLotId(lot.getId())).thenReturn(topBid);

        boolean isUserLeading = bidService.isUserLeading(lot.getId(), 2);

        assertTrue(isUserLeading);

    }

    @Test
    @PrepareForTest({BidService.class, BidValidator.class, BidDao.class, LotService.class, MessageService.class, Authentication.class})
    public void isUserLeadingFalse() {

        when(bidService.findTopBidByLotId(lot.getId())).thenReturn(topBid);

        boolean isUserLeading = bidService.isUserLeading(lot.getId(), 1);

        assertFalse(isUserLeading);

    }

    @Test
    @PrepareForTest({BidService.class, BidValidator.class, BidDao.class, LotService.class, MessageService.class, Authentication.class})
    public void findBidsByLotId() {

        when(lotService.exists(anyInt())).thenReturn(true);
        when(bidDao.findBidsByLotId(anyInt())).thenReturn(bids);

        List<Bid> receivedBids = bidService.findBidsByLotId(lot.getId());

        assertFalse(receivedBids.isEmpty());
    }

    @Test(expected = NotFoundException.class)
    @PrepareForTest({BidService.class, BidValidator.class, BidDao.class, LotService.class, MessageService.class, Authentication.class})
    public void findBidsByLotIdWhileLotIsAbsent() {

        when(lotService.findById(anyInt())).thenReturn(null);

        List<Bid> receivedBids = bidService.findBidsByLotId(lot.getId());
    }
}