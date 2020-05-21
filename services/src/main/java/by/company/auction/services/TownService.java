package by.company.auction.services;

import by.company.auction.model.Town;
import by.company.auction.repository.TownRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@Transactional
public class TownService extends AbstractService<Town, TownRepository> {

    protected TownService(TownRepository repository) {
        super(repository);
    }

    public Town findTownByName(String name) {

        log.debug("findTownByName() name = {}", name);
        return repository.findTownByName(name);

    }

}
