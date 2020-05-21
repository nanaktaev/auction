package by.company.auction.services;

import by.company.auction.AbstractTest;
import by.company.auction.common.exceptions.NotYetPopulatedException;
import by.company.auction.model.Bid;
import by.company.auction.model.Lot;
import by.company.auction.model.User;
import by.company.auction.repository.BidRepository;
import by.company.auction.validators.BidValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static by.company.auction.common.security.AuthenticationConfig.authentication;
import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

public class BidServiceTest extends AbstractTest {

    @Mock
    private LotService lotService;
    @Mock
    private MessageService messageService;
    @Mock
    private UserService userService;
    @Mock
    private BidValidator bidValidator;
    @Mock
    private BidRepository bidRepository;
    @InjectMocks
    private BidService bidService;

    private Bid newBid;
    private Bid topBid;
    private User authenticatedUser;
    private Lot lot;
    private Lot burningLot;
    private List<Bid> bids;
    private List<Bid> emptyBids;

    @Before
    public void beforeEachTest() {

        authenticatedUser = new User();
        authenticatedUser.setId(1);

        User user = new User();
        user.setId(2);

        lot = new Lot();
        lot.setId(1);
        lot.setCloses(LocalDateTime.now().minusDays(1));

        newBid = new Bid();
        newBid.setValue(new BigDecimal(110));
        newBid.setLot(lot);

        topBid = new Bid();
        topBid.setUser(user);

        bids = Collections.singletonList(topBid);
        emptyBids = Collections.EMPTY_LIST;

        burningLot = new Lot();
        burningLot.setId(1);
        burningLot.setCloses(LocalDateTime.now().minusSeconds(30));

        authentication.setUserId(1);

    }

    @Test
    public void makeBid() {

        when(lotService.findById(1)).thenReturn(lot);
        doNothing().when(bidValidator).validate(lot, newBid, 1);
        when(bidRepository.findTopBidByLotId(1)).thenReturn(topBid);
        when(userService.findById(1)).thenReturn(authenticatedUser);
        when(bidRepository.save(newBid)).thenReturn(newBid);
        when(lotService.update(lot)).thenReturn(lot);
        doNothing().when(messageService).createWarningMessage(2, newBid);

        newBid = bidService.makeBid(newBid);

        verify(messageService, times(1)).createWarningMessage(2, newBid);
        assertEquals(Optional.of(1), Optional.of(newBid.getUser().getId()));
        assertEquals(newBid.getValue(), lot.getPrice());
    }

    @Test
    public void makeBidOnLotWhileTopBidIsAbsent() {

        when(lotService.findById(1)).thenReturn(burningLot);
        doNothing().when(bidValidator).validate(burningLot, newBid, 1);
        when(bidRepository.findTopBidByLotId(1)).thenReturn(null);
        when(userService.findById(1)).thenReturn(authenticatedUser);
        when(bidRepository.save(newBid)).thenReturn(newBid);
        when(lotService.update(burningLot)).thenReturn(burningLot);

        newBid = bidService.makeBid(newBid);

        verify(messageService, times(0)).createWarningMessage(2, newBid);
        assertEquals(java.util.Optional.of(1), java.util.Optional.of(newBid.getUser().getId()));

    }

    @Test
    public void makeBidOnBurningLot() {

        when(lotService.findById(1)).thenReturn(burningLot);
        doNothing().when(bidValidator).validate(burningLot,newBid, 1);
        when(bidRepository.findTopBidByLotId(1)).thenReturn(topBid);
        when(userService.findById(1)).thenReturn(authenticatedUser);
        when(bidRepository.save(newBid)).thenReturn(newBid);
        when(lotService.update(burningLot)).thenReturn(burningLot);
        doNothing().when(messageService).createWarningMessage(2, newBid);

        newBid = bidService.makeBid(newBid);

        verify(messageService, times(1)).createWarningMessage(2, newBid);
        assertEquals(java.util.Optional.of(1), java.util.Optional.of(newBid.getUser().getId()));
        assertTrue(LocalDateTime.now().plusMinutes(2).isAfter(burningLot.getCloses()));

    }

    @Test
    public void isUserLeadingTrue() {

        when(bidService.findTopBidByLotId(1)).thenReturn(topBid);

        boolean isUserLeading = bidService.isUserLeading(1, 2);

        assertTrue(isUserLeading);

    }

    @Test
    public void isUserLeadingFalse() {

        when(bidService.findTopBidByLotId(1)).thenReturn(topBid);

        boolean isUserLeading = bidService.isUserLeading(1, 1);

        assertFalse(isUserLeading);

    }

    @Test
    public void findBidsByLotId() {

        when(bidRepository.findBidsByLotId(1)).thenReturn(bids);

        List<Bid> receivedBids = bidService.findBidsByLotId(1);

        assertFalse(receivedBids.isEmpty());
    }

    @Test(expected = NotYetPopulatedException.class)
    public void findBidsByLotIdWhileLotIsAbsent() {

        when(bidRepository.findBidsByLotId(1)).thenReturn(emptyBids);

        List<Bid> receivedBids = bidService.findBidsByLotId(1);

        assertTrue(receivedBids.isEmpty());

    }
}