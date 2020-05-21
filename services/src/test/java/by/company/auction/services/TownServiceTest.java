package by.company.auction.services;

import by.company.auction.AbstractTest;
import by.company.auction.model.Town;
import by.company.auction.repository.TownRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class TownServiceTest extends AbstractTest {

    @Mock
    private TownRepository townRepository;
    @InjectMocks
    private TownService townService;

    private Town town;

    @Before
    public void beforeEachTest() {

        town = new Town();
        town.setName("Минск");

    }

    @Test
    public void findTownByName() {

        when(townRepository.findTownByName("Минск")).thenReturn(town);

        Town receivedTown = townService.findTownByName("Минск");

        assertNotNull(receivedTown);

    }

}