package by.company.auction.validators;

import by.company.auction.common.exceptions.EntityAlreadyExistsException;
import by.company.auction.dto.TownDto;
import by.company.auction.services.TownService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TownValidator {

    @Autowired
    private TownService townService;

    public void validate(TownDto townDto) {

        log.debug("validate() townDto = {}", townDto);

        if (townService.findTownByName(townDto.getName()) != null) {
            throw new EntityAlreadyExistsException("This town has already been added.");
        }
    }
}
