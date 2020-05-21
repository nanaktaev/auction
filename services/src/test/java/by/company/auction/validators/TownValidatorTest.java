package by.company.auction.validators;

import by.company.auction.AbstractTest;
import by.company.auction.common.exceptions.AlreadyExistsException;
import by.company.auction.model.Town;
import by.company.auction.services.TownService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.when;

public class TownValidatorTest extends AbstractTest {

    @Mock
    private TownService townService;
    @InjectMocks
    private TownValidator townValidator;

    private static Town town;

    @BeforeClass
    public static void beforeAllTests() {

        town = new Town();
        town.setName("Minsk");

    }

    @Test
    public void validateSuccess() {

        when(townService.findTownByName("Minsk")).thenReturn(null);

        townValidator.validate(town);

    }

    @Test(expected = AlreadyExistsException.class)
    public void validateAlreadyExists() {

        when(townService.findTownByName("Minsk")).thenReturn(town);

        townValidator.validate(town);

    }

}