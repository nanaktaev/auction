package by.company.auction.services;

import by.company.auction.common.exceptions.NotYetPopulatedException;
import by.company.auction.model.Lot;
import by.company.auction.repository.LotRepository;
import by.company.auction.validators.LotValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static by.company.auction.common.security.AuthenticationConfig.authentication;

@Log4j2
@Service
@Transactional
public class LotService extends AbstractService<Lot, LotRepository> {

    @Autowired
    private LotValidator lotValidator;
    @Autowired
    private CompanyService companyService;

    protected LotService(LotRepository repository) {
        super(repository);
    }


    public List<Lot> findLotsByTownId(Integer townId) {

        log.debug("findLotsByTownId() townId = {}", townId);

        List<Lot> lots = repository.findLotsByTownId(townId);

        if (lots.isEmpty()) {
            throw new NotYetPopulatedException("В этом городе пока нет лотов.");
        }

        return lots;
    }

    public List<Lot> findLotsByCategoryId(Integer categoryId) {

        log.debug("findLotsByCategoryId() categoryId = {}", categoryId);

        List<Lot> lots = repository.findLotsByCategoryId(categoryId);

        if (lots.isEmpty()) {
            throw new NotYetPopulatedException("В этой категории пока нет лотов.");
        }

        return lots;
    }

    @SuppressWarnings("WeakerAccess")
    public List<Lot> findLotsByUserId(Integer userId) {

        log.debug("findLotsByUserId() userId = {}", userId);
        return repository.findLotsByUserId(userId);

    }

    List<Lot> findExpiredLotsByUserId(Integer userId) {

        log.debug("findExpiredLotsByUserId() userId = {}", userId);
        return repository.findExpiredLotsByUserId(userId);

    }

    public Lot createLot(Lot lot) {

        log.debug("createLot() lot = {}", lot);

        Integer companyId = authentication.getUserCompanyId();

        lotValidator.validate(lot);

        lot.setCompany(companyService.findById(companyId));
        lot.setOpened(LocalDateTime.now());
        lot.setPrice(lot.getPriceStart());

        return create(lot);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteLotWithBidsById(id);
    }

}
