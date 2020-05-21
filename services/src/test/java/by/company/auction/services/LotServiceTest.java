package by.company.auction.services;

import by.company.auction.AbstractTest;
import by.company.auction.model.Company;
import by.company.auction.model.Lot;
import by.company.auction.repository.LotRepository;
import by.company.auction.validators.LotValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static by.company.auction.common.security.AuthenticationConfig.authentication;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class LotServiceTest extends AbstractTest {

    private Lot lot;
    private Company company;
    private List<Lot> lots;

    @Mock
    private LotRepository lotRepository;
    @Mock
    private LotValidator lotValidator;
    @Mock
    private CompanyService companyService;
    @InjectMocks
    private LotService lotService;

    @Before
    public void beforeEachTest() {

        company = new Company();
        company.setId(1);

        lot = new Lot();
        lot.setId(1);
        lot.setCloses(LocalDateTime.now().minusDays(1));
        lot.setPriceStart(new BigDecimal(100));

        lots = Collections.singletonList(lot);

        authentication.setUserCompanyId(1);

    }

    @Test
    public void createLot() {

        final ArgumentCaptor<Lot> CAPTOR = ArgumentCaptor.forClass(Lot.class);

        when(companyService.findById(1)).thenReturn(company);
        doNothing().when(lotValidator).validate(lot);

        lotService.createLot(lot);
        verify(lotRepository).save(CAPTOR.capture());
        Lot createdLot = CAPTOR.getValue();

        assertEquals(lot.getPriceStart(), createdLot.getPrice());
        assertEquals(
                java.util.Optional.of(authentication.getUserCompanyId()),
                java.util.Optional.of(createdLot.getCompany().getId())
        );

    }

    @Test
    public void findExpiredLotsByUserId() {

        when(lotRepository.findExpiredLotsByUserId(anyInt())).thenReturn(lots);

        List<Lot> receivedLots = lotService.findExpiredLotsByUserId(1);

        assertNotNull(receivedLots);

    }

    @Test
    public void findLotsByUserId() {

        when(lotRepository.findLotsByUserId(anyInt())).thenReturn(lots);

        List<Lot> receivedLots = lotService.findLotsByUserId(1);

        assertNotNull(receivedLots);

    }

    @Test
    public void findLotsByTownId() {

        when(lotRepository.findLotsByTownId(anyInt())).thenReturn(lots);

        List<Lot> receivedLots = lotService.findLotsByTownId(1);

        assertNotNull(receivedLots);

    }

    @Test
    public void findLotsByCategoryId() {

        when(lotRepository.findLotsByCategoryId(anyInt())).thenReturn(lots);

        List<Lot> receivedLots = lotService.findLotsByCategoryId(1);

        assertNotNull(receivedLots);

    }

}