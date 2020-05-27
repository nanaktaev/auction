package by.company.auction.services;

import by.company.auction.AbstractTest;
import by.company.auction.common.exceptions.NotYetPopulatedException;
import by.company.auction.converters.BidConverter;
import by.company.auction.dto.BidDto;
import by.company.auction.dto.LotDto;
import by.company.auction.dto.UserDto;
import by.company.auction.dto.UserPrincipalAuction;
import by.company.auction.model.Bid;
import by.company.auction.model.User;
import by.company.auction.repository.BidRepository;
import by.company.auction.security.SecurityValidator;
import by.company.auction.validators.BidValidator;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

public class BidServiceTest extends AbstractTest {

    @Mock
    private SecurityValidator securityValidator;
    @Mock
    private BidConverter bidConverter;
    @Mock
    private LotService lotService;
    @Mock
    private MessageService messageService;
    @Mock
    private UserService userService;
    @SuppressWarnings("unused")
    @Mock
    private BidValidator bidValidator;
    @Mock
    private BidRepository bidRepository;
    @InjectMocks
    @Spy
    private BidService bidService;

    private static BidDto newBid;
    private static BidDto topBidDto;
    private static UserDto userDto;
    private static LotDto lot;
    private static LotDto burningLot;
    private static List<Bid> bids;
    private static List<Bid> emptyBids;
    private static UserPrincipalAuction principal;
    private final ArgumentCaptor<BidDto> DTO_CAPTOR = ArgumentCaptor.forClass(BidDto.class);
    private final ArgumentCaptor<List> LIST_CAPTOR = ArgumentCaptor.forClass(ArrayList.class);

    @BeforeClass
    public static void beforeAllTests() {

        userDto = new UserDto();
        userDto.setId(2);

        User user = new User();
        user.setId(2);

        lot = new LotDto();
        lot.setId(1);
        lot.setCloses(LocalDateTime.now().minusDays(1));

        newBid = new BidDto();
        newBid.setValue(new BigDecimal(110));
        newBid.setLot(lot);

        topBidDto = new BidDto();
        topBidDto.setUser(userDto);

        burningLot = new LotDto();
        burningLot.setId(1);
        burningLot.setCloses(LocalDateTime.now().minusSeconds(30));

        principal = new UserPrincipalAuction(user);

        Bid topBid = new Bid();
        topBid.setUser(user);

        bids = Collections.singletonList(topBid);
        emptyBids = Collections.EMPTY_LIST;
    }

    @Test
    public void makeBid() {

        when(lotService.findById(1)).thenReturn(lot);
        doReturn(topBidDto).when(bidService).findTopBidByLotId(1);
        when(userService.findById(2)).thenReturn(userDto);
        when(lotService.update(lot)).thenReturn(lot);
        doNothing().when(messageService).createWarningMessage(2, newBid);
        when(securityValidator.getUserPrincipal()).thenReturn(principal);

        bidService.makeBid(newBid);

        verify(bidConverter).convertToEntity(DTO_CAPTOR.capture());
        BidDto createdBid = DTO_CAPTOR.getValue();

        verify(messageService, times(1)).createWarningMessage(anyInt(), any());
        assertEquals(Optional.of(2), Optional.of(createdBid.getUser().getId()));
        assertEquals(createdBid.getValue(), lot.getPrice());
    }

    @Test
    public void makeBidOnLotWhileTopBidIsAbsent() {

        when(lotService.findById(1)).thenReturn(burningLot);

        doReturn(null).when(bidService).findTopBidByLotId(1);
        when(userService.findById(2)).thenReturn(userDto);
        when(lotService.update(burningLot)).thenReturn(burningLot);
        when(securityValidator.getUserPrincipal()).thenReturn(principal);

        bidService.makeBid(newBid);

        verify(bidConverter).convertToEntity(DTO_CAPTOR.capture());
        BidDto createdBid = DTO_CAPTOR.getValue();

        verify(messageService, times(0)).createWarningMessage(2, createdBid);
        assertEquals(java.util.Optional.of(2), java.util.Optional.of(createdBid.getUser().getId()));
    }

    @Test
    public void makeBidOnBurningLot() {

        when(lotService.findById(1)).thenReturn(burningLot);

        doReturn(topBidDto).when(bidService).findTopBidByLotId(1);
        when(userService.findById(2)).thenReturn(userDto);
        when(lotService.update(burningLot)).thenReturn(burningLot);
        doNothing().when(messageService).createWarningMessage(2, newBid);
        when(securityValidator.getUserPrincipal()).thenReturn(principal);

        bidService.makeBid(newBid);

        verify(bidConverter).convertToEntity(DTO_CAPTOR.capture());
        BidDto createdBid = DTO_CAPTOR.getValue();

        verify(messageService, times(1)).createWarningMessage(2, createdBid);
        assertEquals(java.util.Optional.of(2), java.util.Optional.of(createdBid.getUser().getId()));
        assertTrue(LocalDateTime.now().plusMinutes(2).isAfter(burningLot.getCloses()));
    }

    @Test
    public void isUserLeadingTrue() {

        doReturn(topBidDto).when(bidService).findTopBidByLotId(1);

        boolean isUserLeading = bidService.isUserLeading(1, 2);

        assertTrue(isUserLeading);
    }

    @Test
    public void isUserLeadingFalse() {

        doReturn(topBidDto).when(bidService).findTopBidByLotId(1);

        boolean isUserLeading = bidService.isUserLeading(1, 1);

        assertFalse(isUserLeading);
    }

    @Test
    public void findBidsByLotId() {

        when(bidRepository.findBidsByLotId(1)).thenReturn(bids);

        bidService.findBidsByLotId(1);

        verify(bidConverter).convertListToDto(LIST_CAPTOR.capture());
        List<BidDto> receivedBids = LIST_CAPTOR.getValue();

        assertFalse(receivedBids.isEmpty());
    }

    @Test(expected = NotYetPopulatedException.class)
    public void findBidsByLotIdWhileLotIsAbsent() {

        when(bidRepository.findBidsByLotId(1)).thenReturn(emptyBids);

        bidService.findBidsByLotId(1);

        verify(bidConverter).convertListToDto(LIST_CAPTOR.capture());
        List<BidDto> receivedBids = LIST_CAPTOR.getValue();

        assertTrue(receivedBids.isEmpty());
    }
}