package by.company.auction.converters;

import by.company.auction.dto.TownDto;
import by.company.auction.model.Town;
import org.springframework.stereotype.Component;

@Component
public class TownConverter extends AbstractConverter<Town, TownDto> {

    @Override
    public TownDto convertToDto(Town town) {

        TownDto townDto = new TownDto();
        townDto.setId(town.getId());
        townDto.setName(town.getName());

        return townDto;
    }

    @Override
    public Town convertToEntity(TownDto townDto) {

        Town town = new Town();
        town.setId(townDto.getId());
        town.setName(townDto.getName());

        return town;

    }
}
