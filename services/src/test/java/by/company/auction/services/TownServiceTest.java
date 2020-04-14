package by.company.auction.services;

import by.company.auction.dao.TownDao;
import by.company.auction.model.Town;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class TownServiceTest extends AbstractService {

    private Town town;
    private TownService townService;
    private TownDao townDao;

    @Before
    public void beforeEachTest() {

        PowerMockito.mockStatic(TownDao.class);
        PowerMockito.when(TownDao.getInstance()).thenReturn(mock(TownDao.class));
        MockitoAnnotations.initMocks(this);

        townDao = TownDao.getInstance();
        townService = TownService.getInstance();

        town = new Town();
        town.setName("Минск");

    }

    @Test
    @PrepareForTest({TownService.class, TownDao.class})
    public void findTownByName() {

        when(townDao.findTownByName(anyString())).thenReturn(town);

        Town receivedTown = townService.findTownByName(anyString());

        assertNotNull(receivedTown);

    }

}