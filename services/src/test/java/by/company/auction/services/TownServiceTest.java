package by.company.auction.services;

import by.company.auction.AbstractTest;
import by.company.auction.common.exceptions.ParentDeletionException;
import by.company.auction.converters.TownConverter;
import by.company.auction.dto.TownDto;
import by.company.auction.model.Town;
import by.company.auction.repository.TownRepository;
import by.company.auction.validators.TownValidator;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TownServiceTest extends AbstractTest {

    @SuppressWarnings("unused")
    @Spy
    private TownConverter townConverter;
    @Mock
    private TownRepository townRepository;
    @Mock
    private TownValidator townValidator;
    @InjectMocks
    private TownService townService;

    private static Town town;
    private static TownDto townDto;

    @BeforeClass
    public static void beforeAllTests() {

        town = new Town();
        town.setName("Minsk");

        townDto = new TownDto();
        townDto.setName("Minsk");
    }

    @Test
    public void findTownByName() {

        when(townRepository.findTownByName("Minsk")).thenReturn(town);

        TownDto receivedTown = townService.findTownByName("Minsk");

        assertEquals(receivedTown.getName(), "Minsk");
    }

    @Test
    public void create() {

        doNothing().when(townValidator).validate(townDto);
        when(townRepository.save(any())).thenReturn(town);

        TownDto createdTown = townService.create(townDto);

        assertEquals(createdTown.getName(), "Minsk");
    }

    @Test
    public void deleteEmptyTown() {

        when(townRepository.isTownEmptyOfLots(1)).thenReturn(true);
        doNothing().when(townRepository).deleteById(1);

        townService.delete(1);

        verify(townRepository, times(1)).deleteById(1);
    }

    @Test(expected = ParentDeletionException.class)
    public void deleteTownWithLots() {

        when(townRepository.isTownEmptyOfLots(1)).thenReturn(false);

        townService.delete(1);

        verify(townRepository, times(0)).deleteById(1);
    }
}