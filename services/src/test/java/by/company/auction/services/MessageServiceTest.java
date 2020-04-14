package by.company.auction.services;

import by.company.auction.dao.MessageDao;
import by.company.auction.model.Bid;
import by.company.auction.model.Lot;
import by.company.auction.model.Message;
import by.company.auction.model.MessageType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
public class MessageServiceTest extends AbstractService {

    private Message message;
    private List<Message> messages;
    private Bid bid;
    private MessageService messageService;
    private MessageDao messageDao;
    private LotService lotService;
    private BidService bidService;

    @Before
    public void beforeEachTest() {

        PowerMockito.mockStatic(MessageDao.class);
        PowerMockito.when(MessageDao.getInstance()).thenReturn(mock(MessageDao.class));
        PowerMockito.mockStatic(LotService.class);
        PowerMockito.when(LotService.getInstance()).thenReturn(mock(LotService.class));
        PowerMockito.mockStatic(BidService.class);
        PowerMockito.when(BidService.getInstance()).thenReturn(mock(BidService.class));
        MockitoAnnotations.initMocks(this);

        messageDao = MessageDao.getInstance();
        messageService = MessageService.getInstance();
        lotService = LotService.getInstance();
        bidService = BidService.getInstance();

        message = new Message();
        message.setId(1);
        message.setType(MessageType.OUTCOME);
        message.setLotId(1);

        messages = Collections.singletonList(message);

        bid = new Bid();
        bid.setTime(LocalDateTime.now());
        bid.setLotId(33);
    }

    @Test
    @PrepareForTest({MessageService.class, MessageDao.class, LotService.class, BidService.class})
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

        when(messageService.findOutcomeMessagesByUserId(anyInt())).thenReturn(messages);
        when(lotService.findExpiredLotsByUserId(anyInt())).thenReturn(lots);
        when(bidService.isUserLeading(1, 1)).thenReturn(true);
        when(bidService.isUserLeading(2, 1)).thenReturn(true);
        when(bidService.isUserLeading(3, 1)).thenReturn(false);
        when(messageDao.create(any(Message.class))).thenReturn(message);

        messageService = spy(messageService);
        messageService.prepareUserMessages(1);

        verify(messageService, times(1)).createOutcomeMessage(any(LocalDateTime.class), anyInt(), anyInt(), eq(true));
        verify(messageService, times(1)).createOutcomeMessage(any(LocalDateTime.class), anyInt(), anyInt(), eq(false));
    }

    @Test
    @PrepareForTest({MessageService.class, MessageDao.class, LotService.class, BidService.class})
    public void prepareUserMessagesNoNewMessages() {

        Lot lot = new Lot();
        lot.setId(1);
        lot.setCloses(LocalDateTime.now());

        List<Lot> lots = new java.util.ArrayList<>(Collections.singletonList(lot));

        when(messageService.findOutcomeMessagesByUserId(anyInt())).thenReturn(messages);
        when(lotService.findExpiredLotsByUserId(anyInt())).thenReturn(lots);
        when(bidService.isUserLeading(1, 1)).thenReturn(true);
        when(messageDao.create(any(Message.class))).thenReturn(message);

        messageService = spy(messageService);
        messageService.prepareUserMessages(1);

        verify(messageService, times(0)).createOutcomeMessage(any(LocalDateTime.class), anyInt(), anyInt(), eq(true));
        verify(messageService, times(0)).createOutcomeMessage(any(LocalDateTime.class), anyInt(), anyInt(), eq(false));
    }

    @Test
    @PrepareForTest({MessageService.class, MessageDao.class, LotService.class, BidService.class})
    public void createOutcomeMessageWhileUserIsLeading() {

        final ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);

        messageService.createOutcomeMessage(LocalDateTime.now(), 1, 33, true);
        verify(messageDao).create(captor.capture());
        Message createdMessage = captor.getValue();

        assertEquals("Вы выиграли торги по лоту №33!", createdMessage.getText());

    }

    @Test
    @PrepareForTest({MessageService.class, MessageDao.class, LotService.class, BidService.class})
    public void createOutcomeMessageWhileUserIsLosing() {

        final ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);

        messageService.createOutcomeMessage(LocalDateTime.now(), 1, 33, false);
        verify(messageDao).create(captor.capture());
        Message createdMessage = captor.getValue();

        assertEquals("Вы проиграли торги по лоту №33.", createdMessage.getText());

    }

    @Test
    @PrepareForTest({MessageService.class, MessageDao.class, LotService.class, BidService.class})
    public void createWarningMessage() {

        final ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);

        messageService.createWarningMessage(1, bid);
        verify(messageDao).create(captor.capture());
        Message createdMessage = captor.getValue();

        assertEquals("Ваша ставка по лоту №33 была перебита!", createdMessage.getText());

    }

    @Test
    @PrepareForTest({MessageService.class, MessageDao.class, LotService.class, BidService.class})
    public void findMessagesByUserId() {

        when(messageDao.findMessagesByUserId(anyInt())).thenReturn(messages);

        List<Message> receivedMessages = messageService.findMessagesByUserId(1);

        assertNotNull(receivedMessages);

    }

    @Test
    @PrepareForTest({MessageService.class, MessageDao.class, LotService.class, BidService.class})
    public void findOutcomeMessagesByUserId() {

        when(messageDao.findOutcomeMessagesByUserId(anyInt())).thenReturn(messages);

        List<Message> receivedMessages = messageService.findOutcomeMessagesByUserId(1);

        assertNotNull(receivedMessages);

    }

}