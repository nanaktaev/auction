package by.company.auction.services;

import by.company.auction.dao.LotDao;
import by.company.auction.model.Lot;
import by.company.auction.validators.LotValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static by.company.auction.secuirty.AuthenticatonConfig.authentication;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
public class LotServiceTest extends AbstractService {

    private Lot lot;
    private List<Lot> lots;
    private LotService lotService;
    private LotDao lotDao;
    private LotValidator lotValidator;

    @Before
    public void beforeEachTest() {

        PowerMockito.mockStatic(LotDao.class);
        PowerMockito.when(LotDao.getInstance()).thenReturn(mock(LotDao.class));
        PowerMockito.mockStatic(LotValidator.class);
        PowerMockito.when(LotValidator.getInstance()).thenReturn(mock(LotValidator.class));
        MockitoAnnotations.initMocks(this);

        lotDao = LotDao.getInstance();
        lotService = LotService.getInstance();
        lotValidator = LotValidator.getInstance();

        lot = new Lot();
        lot.setId(1);
        lot.setCloses(LocalDateTime.now().minusDays(1));
        lot.setPriceStart(new BigDecimal(100));

        lots = Collections.singletonList(lot);

        authentication.setUserCompanyId(1);

    }

    @Test
    @PrepareForTest({LotService.class, LotDao.class, LotValidator.class})
    public void createLot() {

        final ArgumentCaptor<Lot> captor = ArgumentCaptor.forClass(Lot.class);

        doNothing().when(lotValidator).validate(any(Lot.class));

        lotService.createLot(lot);
        verify(lotDao).create(captor.capture());
        Lot createdLot = captor.getValue();

        assertEquals(lot.getPriceStart(), createdLot.getPrice());
        assertEquals(
                java.util.Optional.of(authentication.getUserCompanyId()),
                java.util.Optional.of(createdLot.getCompanyId())
        );

    }

    @Test
    @PrepareForTest({LotService.class, LotDao.class, LotValidator.class})
    public void findExpiredLotsByUserId() {

        when(lotDao.findExpiredLotsByUserId(anyInt())).thenReturn(lots);

        List<Lot> receivedLots = lotService.findExpiredLotsByUserId(1);

        assertNotNull(receivedLots);

    }

    @Test
    @PrepareForTest({LotService.class, LotDao.class, LotValidator.class})
    public void findLotsByUserId() {

        when(lotDao.findLotsByUserId(anyInt())).thenReturn(lots);

        List<Lot> receivedLots = lotService.findLotsByUserId(1);

        assertNotNull(receivedLots);

    }

    @Test
    @PrepareForTest({LotService.class, LotDao.class, LotValidator.class})
    public void findLotsByTownId() {

        when(lotDao.findLotsByTownId(anyInt())).thenReturn(lots);

        List<Lot> receivedLots = lotService.findLotsByTownId(1);

        assertNotNull(receivedLots);

    }

    @Test
    @PrepareForTest({LotService.class, LotDao.class, LotValidator.class})
    public void findLotsByCategoryId() {

        when(lotDao.findLotsByCategoryId(anyInt())).thenReturn(lots);

        List<Lot> receivedLots = lotService.findLotsByCategoryId(1);

        assertNotNull(receivedLots);

    }

}