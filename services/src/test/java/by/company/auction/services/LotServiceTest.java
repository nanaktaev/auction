package by.company.auction.services;

import by.company.auction.AbstractTest;
import by.company.auction.common.exceptions.NotYetPopulatedException;
import by.company.auction.converters.LotConverter;
import by.company.auction.dto.BidDto;
import by.company.auction.dto.CompanyDto;
import by.company.auction.dto.LotDto;
import by.company.auction.dto.UserPrincipalAuction;
import by.company.auction.model.Company;
import by.company.auction.model.Lot;
import by.company.auction.model.Role;
import by.company.auction.model.User;
import by.company.auction.repository.LotRepository;
import by.company.auction.security.SecurityValidator;
import by.company.auction.validators.LotValidator;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

public class LotServiceTest extends AbstractTest {

    private static LotDto lotDto;
    private static CompanyDto companyDto;
    private static List<Lot> lots;
    private static List<Lot> emptyLots;
    private static UserPrincipalAuction principal;
    private final ArgumentCaptor<LotDto> DTO_CAPTOR = ArgumentCaptor.forClass(LotDto.class);
    private final ArgumentCaptor<List> LIST_CAPTOR = ArgumentCaptor.forClass(ArrayList.class);

    @SuppressWarnings("unused")
    @Mock
    private LotConverter lotConverter;
    @Mock
    private LotRepository lotRepository;
    @Mock
    private SecurityValidator securityValidator;
    @Mock
    private LotValidator lotValidator;
    @Mock
    private CompanyService companyService;
    @Spy
    @InjectMocks
    private LotService lotService;

    @BeforeClass
    public static void beforeAllTests() {

        Company company = new Company();
        company.setId(1);

        User user = new User();
        user.setId(1);
        user.setCompany(company);
        user.setRole(Role.VENDOR);

        principal = new UserPrincipalAuction(user);

        companyDto = new CompanyDto();
        companyDto.setId(1);

        lotDto = new LotDto();
        lotDto.setId(1);
        lotDto.setTitle("Title");
        lotDto.setCloses(LocalDateTime.now().minusDays(1));
        lotDto.setPriceStart(new BigDecimal(100));
        lotDto.setCompany(companyDto);

        Lot lot = new Lot();
        lot.setId(1);
        lot.setCloses(LocalDateTime.now().minusDays(1));
        lot.setPriceStart(new BigDecimal(100));

        lots = Collections.singletonList(lot);
        emptyLots = Collections.emptyList();
    }

    @Test
    public void createLot() {

        when(companyService.findById(1)).thenReturn(companyDto);
        doNothing().when(lotValidator).validate(lotDto);
        when(securityValidator.getUserPrincipal()).thenReturn(principal);

        lotService.create(lotDto);

        verify(lotConverter).convertToEntity(DTO_CAPTOR.capture());
        LotDto createdLot = DTO_CAPTOR.getValue();

        assertEquals(lotDto.getPriceStart(), createdLot.getPrice());
        assertEquals(
                java.util.Optional.of(principal.getCompanyId()),
                java.util.Optional.of(createdLot.getCompany().getId())
        );
    }

    @Test
    public void editLot() {

        LotDto lotDtoFromRequestBody = new LotDto();
        lotDtoFromRequestBody.setId(1);
        lotDtoFromRequestBody.setTitle("New title.");

        doReturn(lotDto).when(lotService).findById(1);
        doNothing().when(securityValidator).validateCompanyAffiliation(1);
        doNothing().when(lotValidator).validateUpdate(any());

        lotService.editLot(lotDtoFromRequestBody);

        verify(lotConverter).convertToEntity(DTO_CAPTOR.capture());
        LotDto updatedLot = DTO_CAPTOR.getValue();

        Assertions.assertEquals("New title.", updatedLot.getTitle());
    }

    @Test
    public void deleteLot() {

        doReturn(lotDto).when(lotService).findById(1);
        doNothing().when(securityValidator).validateCompanyAffiliation(1);

        lotService.delete(1);

        verify(lotRepository, times(1)).deleteById(1);
    }

    @Test
    public void findExpiredLotsByUserId() {

        when(lotRepository.findExpiredLotsByUserId(1)).thenReturn(lots);

        lotService.findExpiredLotsByUserId(1);

        verify(lotConverter).convertListToDto(LIST_CAPTOR.capture());
        List<BidDto> receivedLots = LIST_CAPTOR.getValue();

        assertFalse(receivedLots.isEmpty());
    }


    @Test
    public void findLotsByTownId() {

        when(lotRepository.findLotsByTownId(1, Pageable.unpaged())).thenReturn(lots);

        lotService.findLotsByTownId(1, Pageable.unpaged());

        verify(lotConverter).convertListToDto(LIST_CAPTOR.capture());
        List<BidDto> receivedLots = LIST_CAPTOR.getValue();

        assertFalse(receivedLots.isEmpty());
    }

    @Test(expected = NotYetPopulatedException.class)
    public void findLotsByTownIdNoLots() {

        when(lotRepository.findLotsByTownId(1, Pageable.unpaged())).thenReturn(emptyLots);

        lotService.findLotsByTownId(1, Pageable.unpaged());
    }

    @Test
    public void findLotsByCategoryId() {

        when(lotRepository.findLotsByCategoryId(1, Pageable.unpaged())).thenReturn(lots);

        lotService.findLotsByCategoryId(1, Pageable.unpaged());

        verify(lotConverter).convertListToDto(LIST_CAPTOR.capture());
        List<BidDto> receivedLots = LIST_CAPTOR.getValue();

        assertFalse(receivedLots.isEmpty());
    }

    @Test(expected = NotYetPopulatedException.class)
    public void findLotsByCategoryIdNoLots() {

        when(lotRepository.findLotsByCategoryId(1, Pageable.unpaged())).thenReturn(emptyLots);

        lotService.findLotsByCategoryId(1, Pageable.unpaged());
    }

    @Test
    public void findLotsByCategoryIdAndTownId() {

        when(lotRepository.findLotsByCategoryIdAndTownId(1, 1, Pageable.unpaged())).thenReturn(lots);

        lotService.findLotsByCategoryIdAndTownId(1, 1, Pageable.unpaged());

        verify(lotConverter).convertListToDto(LIST_CAPTOR.capture());
        List<BidDto> receivedLots = LIST_CAPTOR.getValue();

        assertFalse(receivedLots.isEmpty());
    }

    @Test(expected = NotYetPopulatedException.class)
    public void findLotsByCategoryIdAndTownIdNoLots() {

        when(lotRepository.findLotsByCategoryIdAndTownId(1, 1, Pageable.unpaged())).thenReturn(emptyLots);

        lotService.findLotsByCategoryIdAndTownId(1, 1, Pageable.unpaged());
    }
}