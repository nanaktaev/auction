package by.company.auction.services;

import by.company.auction.AbstractTest;
import by.company.auction.model.*;
import by.company.auction.repository.MessageRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class MessageServiceTest extends AbstractTest {

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

    private Message message;
    private List<Message> messages;
    private Bid bid;
    private User user;

    @Before
    public void beforeEachTest() {

        Lot lot = new Lot();
        lot.setId(1);

        Lot lot33 = new Lot();
        lot33.setId(33);

        message = new Message();
        message.setId(1);
        message.setType(MessageType.OUTCOME);
        message.setLot(lot);

        messages = Collections.singletonList(message);

        bid = new Bid();
        bid.setTime(LocalDateTime.now());
        bid.setLot(lot33);

        user = new User();
        user.setId(1);
    }

    @Test
    public void prepareUserMessagesOneLoseOneWin() {

        Lot lot = new Lot();
        lot.setId(1);
        lot.setCloses(LocalDateTime.now());

        Lot lot2 = new Lot();
        lot2.setId(2);
        lot2.setCloses(LocalDateTime.now());

        Lot lot3 = new Lot();
        lot3.setId(3);
        lot3.setCloses(LocalDateTime.now());

        List<Lot> lots = new java.util.ArrayList<>(Arrays.asList(lot, lot2, lot3));

        when(messageService.findOutcomeMessagesByUserId(1)).thenReturn(messages);
        when(lotService.findExpiredLotsByUserId(1)).thenReturn(lots);
        when(bidService.isUserLeading(2, 1)).thenReturn(true);
        when(bidService.isUserLeading(3, 1)).thenReturn(false);
        when(messageRepository.save(any(Message.class))).thenReturn(message);
        when(userService.findById(1)).thenReturn(user);

        messageService = spy(messageService);
        messageService.prepareUserMessages(1);

        verify(messageService, times(1)).createOutcomeMessage(any(LocalDateTime.class), anyInt(), anyInt(), eq(true));
        verify(messageService, times(1)).createOutcomeMessage(any(LocalDateTime.class), anyInt(), anyInt(), eq(false));
    }

    @Test
    public void prepareUserMessagesNoNewMessages() {

        Lot lot = new Lot();
        lot.setId(1);
        lot.setCloses(LocalDateTime.now());

        List<Lot> lots = new java.util.ArrayList<>(Collections.singletonList(lot));

        when(messageService.findOutcomeMessagesByUserId(1)).thenReturn(messages);
        when(lotService.findExpiredLotsByUserId(1)).thenReturn(lots);

        messageService = spy(messageService);
        messageService.prepareUserMessages(1);

        verify(messageService, times(0)).createOutcomeMessage(any(LocalDateTime.class), anyInt(), anyInt(), eq(true));
        verify(messageService, times(0)).createOutcomeMessage(any(LocalDateTime.class), anyInt(), anyInt(), eq(false));
    }

    @Test
    public void createOutcomeMessageWhileUserIsLeading() {

        final ArgumentCaptor<Message> CAPTOR = ArgumentCaptor.forClass(Message.class);

        messageService.createOutcomeMessage(LocalDateTime.now(), 1, 33, true);
        verify(messageRepository).save(CAPTOR.capture());
        Message createdMessage = CAPTOR.getValue();

        assertEquals("Вы выиграли торги по лоту №33!", createdMessage.getText());

    }

    @Test
    public void createOutcomeMessageWhileUserIsLosing() {

        final ArgumentCaptor<Message> CAPTOR = ArgumentCaptor.forClass(Message.class);

        messageService.createOutcomeMessage(LocalDateTime.now(), 1, 33, false);
        verify(messageRepository).save(CAPTOR.capture());
        Message createdMessage = CAPTOR.getValue();

        assertEquals("Вы проиграли торги по лоту №33.", createdMessage.getText());

    }

    @Test
    public void createWarningMessage() {

        final ArgumentCaptor<Message> CAPTOR = ArgumentCaptor.forClass(Message.class);

        messageService.createWarningMessage(1, bid);
        verify(messageRepository).save(CAPTOR.capture());
        Message createdMessage = CAPTOR.getValue();

        assertEquals("Ваша ставка по лоту №33 была перебита!", createdMessage.getText());

    }

    @Test
    public void findMessagesByUserId() {

        when(messageRepository.findMessagesByUserId(anyInt())).thenReturn(messages);

        List<Message> receivedMessages = messageService.findMessagesByUserId(1);

        assertNotNull(receivedMessages);

    }

    @Test
    public void findOutcomeMessagesByUserId() {

        when(messageRepository.findOutcomeMessagesByUserId(anyInt())).thenReturn(messages);

        List<Message> receivedMessages = messageService.findOutcomeMessagesByUserId(1);

        assertNotNull(receivedMessages);

    }

}