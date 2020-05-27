package by.company.auction.services;

import by.company.auction.common.exceptions.NotYetPopulatedException;
import by.company.auction.converters.LotConverter;
import by.company.auction.dto.LotDto;
import by.company.auction.dto.UserPrincipalAuction;
import by.company.auction.model.Lot;
import by.company.auction.repository.LotRepository;
import by.company.auction.security.SecurityValidator;
import by.company.auction.utils.CustomBeanUtils;
import by.company.auction.validators.LotValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
public class LotService extends AbstractService<Lot, LotDto, LotRepository, LotConverter> {

    @Autowired
    private LotValidator lotValidator;
    @Autowired
    private SecurityValidator securityValidator;
    @Autowired
    private CompanyService companyService;

    protected LotService(LotRepository repository, LotConverter converter) {
        super(repository, converter);
    }

    public List<LotDto> findAll(Pageable pageable) {

        List<Lot> lots = repository.findAll(pageable).getContent();

        if (lots.isEmpty()) {
            throw new NotYetPopulatedException("There is nothing here yet.");
        }

        return converter.convertListToDto(lots);
    }

    public List<LotDto> findLotsByCategoryIdAndTownId(Integer categoryId, Integer townId, Pageable pageable) {

        log.debug("findLotsByCategoryIdAndTownId() categoryId = {}, townId = {}", categoryId, townId);

        List<Lot> lots = repository.findLotsByCategoryIdAndTownId(categoryId, townId, pageable);

        if (lots.isEmpty()) {
            throw new NotYetPopulatedException("In this town there are no lots from this category yet.");
        }

        return converter.convertListToDto(lots);
    }

    public List<LotDto> findLotsByTownId(Integer townId, Pageable pageable) {

        log.debug("findLotsByTownId() townId = {}", townId);

        List<Lot> lots = repository.findLotsByTownId(townId, pageable);

        if (lots.isEmpty()) {
            throw new NotYetPopulatedException("There are no lots in this town yet.");
        }

        return converter.convertListToDto(lots);
    }

    public List<LotDto> findLotsByCategoryId(Integer categoryId, Pageable pageable) {

        log.debug("findLotsByCategoryId() categoryId = {}", categoryId);

        List<Lot> lots = repository.findLotsByCategoryId(categoryId, pageable);

        if (lots.isEmpty()) {
            throw new NotYetPopulatedException("There are no lots in this category yet.");
        }

        return converter.convertListToDto(lots);
    }

    List<LotDto> findExpiredLotsByUserId(Integer userId) {

        log.debug("findExpiredLotsByUserId() userId = {}", userId);

        List<Lot> lots = repository.findExpiredLotsByUserId(userId);

        return converter.convertListToDto(lots);
    }

    @Override
    public LotDto create(LotDto lotDto) {

        log.debug("createLot() lotDto = {}", lotDto);

        UserPrincipalAuction principal = securityValidator.getUserPrincipal();

        lotValidator.validate(lotDto);

        lotDto.setCompany(companyService.findById(principal.getCompanyId()));
        lotDto.setOpened(LocalDateTime.now());
        lotDto.setPrice(lotDto.getPriceStart());

        return super.create(lotDto);
    }

    @Override
    public void delete(Integer id) {

        log.debug("delete() lotId = {}", id);

        LotDto deletedLot = findById(id);

        securityValidator.validateCompanyAffiliation(deletedLot.getCompany().getId());

        repository.deleteById(id);
    }

    public LotDto editLot(LotDto lotDto) {

        log.debug("editLot() lotDto = {}", lotDto);

        LotDto updatedLot = findById(lotDto.getId());

        securityValidator.validateCompanyAffiliation(updatedLot.getCompany().getId());

        CustomBeanUtils.copyNotNullProperties(lotDto, updatedLot);

        lotValidator.validateUpdate(updatedLot);

        return update(updatedLot);
    }

    @Override
    public LotDto findById(Integer id) {

        LotDto lotDto = super.findById(id);

        if (lotDto.getViews() != null)
            lotDto.setViews(lotDto.getViews() + 1);
        else {
            lotDto.setViews(0);
        }

        return update(lotDto);
    }
}
