package by.company.auction.services;

import by.company.auction.common.exceptions.ParentDeletionException;
import by.company.auction.converters.TownConverter;
import by.company.auction.dto.TownDto;
import by.company.auction.model.Town;
import by.company.auction.repository.TownRepository;
import by.company.auction.validators.TownValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class TownService extends AbstractService<Town, TownDto, TownRepository, TownConverter> {

    @Autowired
    private TownValidator townValidator;

    protected TownService(TownRepository repository, TownConverter converter) {
        super(repository, converter);
    }

    public TownDto findTownByName(String name) {

        log.debug("findTownByName() name = {}", name);

        Town town = repository.findTownByName(name);

        if (town == null) {
            return null;
        }

        return converter.convertToDto(town);
    }

    @Override
    public TownDto create(TownDto townDto) {

        log.debug("create() townDto = {}", townDto);

        townValidator.validate(townDto);

        return super.create(townDto);
    }

    @Override
    public void delete(Integer id) {

        log.debug("delete() townId = {}", id);

        if (!repository.isTownEmptyOfLots(id)) {
            throw new ParentDeletionException("This town cannot be deleted because it still has lots.");
        }

        super.delete(id);
    }
}
