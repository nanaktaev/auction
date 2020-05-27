package by.company.auction.services;

import by.company.auction.AbstractTest;
import by.company.auction.converters.MessageConverter;
import by.company.auction.dto.BidDto;
import by.company.auction.dto.LotDto;
import by.company.auction.dto.MessageDto;
import by.company.auction.dto.UserDto;
import by.company.auction.model.Lot;
import by.company.auction.model.Message;
import by.company.auction.model.MessageType;
import by.company.auction.model.User;
import by.company.auction.repository.MessageRepository;
import by.company.auction.security.SecurityValidator;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;


public class MessageServiceTest extends AbstractTest {

    @Mock
    private MessageConverter messageConverter;
    @Mock
    private SecurityValidator securityValidator;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private LotService lotService;
    @Mock
    private UserService userService;
    @Mock
    private BidService bidService;
    @InjectMocks
    private MessageService messageService;

    private static List<MessageDto> messageDtos;
    private static List<Message> messages;
    private static BidDto bid;
    private static UserDto userDto;
    private final ArgumentCaptor<List> LIST_CAPTOR = ArgumentCaptor.forClass(ArrayList.class);
    private final ArgumentCaptor<MessageDto> DTO_CAPTOR = ArgumentCaptor.forClass(MessageDto.class);

    @BeforeClass
    public static void beforeAllTests() {

        Lot lot = new Lot();
        lot.setId(1);

        LotDto lotDto = new LotDto();
        lotDto.setId(1);

        LotDto lot33 = new LotDto();
        lot33.setId(33);

        bid = new BidDto();
        bid.setTime(LocalDateTime.now());
        bid.setLot(lot33);

        userDto = new UserDto();
        userDto.setId(1);

        User user = new User();
        user.setId(1);

        MessageDto messageDto = new MessageDto();
        messageDto.setId(1);
        messageDto.setType(MessageType.OUTCOME);
        messageDto.setLot(lotDto);

        messageDtos = Collections.singletonList(messageDto);

        Message message = new Message();
        message.setId(1);
        message.setType(MessageType.OUTCOME);
        message.setLot(lot);
        message.setUser(user);

        messages = Collections.singletonList(message);
    }

    @Test
    public void createOutcomeMessageWhileUserIsLeading() {

        messageService.createOutcomeMessage(LocalDateTime.now(), 1, 33, true);

        verify(messageConverter).convertToEntity(DTO_CAPTOR.capture());
        MessageDto createdMessage = DTO_CAPTOR.getValue();

        assertEquals("You've won bidding on a lot №33!", createdMessage.getText());
    }

    @Test
    public void createOutcomeMessageWhileUserIsLosing() {

        messageService.createOutcomeMessage(LocalDateTime.now(), 1, 33, false);

        verify(messageConverter).convertToEntity(DTO_CAPTOR.capture());
        MessageDto createdMessage = DTO_CAPTOR.getValue();

        assertEquals("You've lost bidding on a lot №33.", createdMessage.getText());
    }

    @Test
    public void findMessagesByUserIdNoNewMessages() {

        LotDto lot = new LotDto();
        lot.setId(1);
        lot.setCloses(LocalDateTime.now());

        List<LotDto> lots = new java.util.ArrayList<>(Collections.singletonList(lot));

        when(lotService.findExpiredLotsByUserId(1)).thenReturn(lots);
        when(messageService.findOutcomeMessagesByUserId(1)).thenReturn(messageDtos);
        when(messageRepository.findMessagesByUserId(anyInt())).thenReturn(messages);
        doNothing().when(securityValidator).validateUserAccountOwnership(1);

        messageService = spy(messageService);
        messageService.findMessagesByUserId(1);

        verify(messageConverter, times(2)).convertListToDto(LIST_CAPTOR.capture());
        List<MessageDto> receivedMessages = LIST_CAPTOR.getValue();

        assertFalse(receivedMessages.isEmpty());
        verify(messageService, times(0)).createOutcomeMessage(any(LocalDateTime.class), anyInt(), anyInt(), eq(true));
        verify(messageService, times(0)).createOutcomeMessage(any(LocalDateTime.class), anyInt(), anyInt(), eq(false));
    }

    @Test
    public void findMessagesByUserIdTwoNewMessagesPrepared() {

        LotDto lot = new LotDto();
        lot.setId(1);
        lot.setCloses(LocalDateTime.now());

        LotDto lot2 = new LotDto();
        lot2.setId(2);
        lot2.setCloses(LocalDateTime.now());

        LotDto lot3 = new LotDto();
        lot3.setId(3);
        lot3.setCloses(LocalDateTime.now());

        List<LotDto> lots = new java.util.ArrayList<>(Arrays.asList(lot, lot2, lot3));

        when(lotService.findExpiredLotsByUserId(1)).thenReturn(lots);
        when(bidService.isUserLeading(2, 1)).thenReturn(true);
        when(bidService.isUserLeading(3, 1)).thenReturn(false);
        when(messageService.findOutcomeMessagesByUserId(1)).thenReturn(messageDtos);
        when(messageRepository.findMessagesByUserId(anyInt())).thenReturn(messages);
        doNothing().when(securityValidator).validateUserAccountOwnership(1);

        messageService = spy(messageService);
        messageService.findMessagesByUserId(1);

        verify(messageConverter, times(2)).convertListToDto(LIST_CAPTOR.capture());
        List<MessageDto> receivedMessages = LIST_CAPTOR.getValue();

        assertFalse(receivedMessages.isEmpty());
        verify(messageService, times(1)).createOutcomeMessage(any(LocalDateTime.class), anyInt(), anyInt(), eq(true));
        verify(messageService, times(1)).createOutcomeMessage(any(LocalDateTime.class), anyInt(), anyInt(), eq(false));
    }

    @Test
    public void findMessagesByUserId() {

        when(messageRepository.findMessagesByUserId(anyInt())).thenReturn(messages);
        doNothing().when(securityValidator).validateUserAccountOwnership(1);

        messageService.findMessagesByUserId(1);

        verify(messageConverter, times(2)).convertListToDto(LIST_CAPTOR.capture());
        List<MessageDto> receivedMessages = LIST_CAPTOR.getValue();

        assertFalse(receivedMessages.isEmpty());
    }

    @Test
    public void createWarningMessage() {

        when(userService.findById(1)).thenReturn(userDto);

        messageService.createWarningMessage(1, bid);

        verify(messageConverter).convertToEntity(DTO_CAPTOR.capture());
        MessageDto createdMessage = DTO_CAPTOR.getValue();

        assertEquals("Your bid on a lot №33 has been outbid!", createdMessage.getText());
    }
}