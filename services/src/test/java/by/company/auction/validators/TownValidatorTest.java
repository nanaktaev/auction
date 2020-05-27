package by.company.auction.validators;

import by.company.auction.AbstractTest;
import by.company.auction.common.exceptions.EntityAlreadyExistsException;
import by.company.auction.dto.TownDto;
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

    private static TownDto townDto;

    @BeforeClass
    public static void beforeAllTests() {

        townDto = new TownDto();
        townDto.setName("Minsk");
    }

    @Test
    public void validateSuccess() {

        when(townService.findTownByName("Minsk")).thenReturn(null);

        townValidator.validate(townDto);
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void validateAlreadyExists() {

        when(townService.findTownByName("Minsk")).thenReturn(townDto);

        townValidator.validate(townDto);
    }
}