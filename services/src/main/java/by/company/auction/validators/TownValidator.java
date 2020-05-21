package by.company.auction.validators;

import by.company.auction.common.exceptions.AlreadyExistsException;
import by.company.auction.model.Town;
import by.company.auction.services.TownService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class TownValidator {

    @Autowired
    private TownService townService;

    public void validate(Town town) {

        log.debug("validate() town = {}", town);

        if (townService.findTownByName(town.getName()) != null) {
            throw new AlreadyExistsException("Город с таким именем уже был добавлен.");
        }
    }

}
