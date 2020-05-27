package by.company.auction.converters;

import by.company.auction.dto.LotDto;
import by.company.auction.model.Lot;
import by.company.auction.repository.CategoryRepository;
import by.company.auction.repository.CompanyRepository;
import by.company.auction.repository.TownRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LotConverter extends AbstractConverter<Lot, LotDto> {

    @Autowired
    private CategoryConverter categoryConverter;
    @Autowired
    private CompanyConverter companyConverter;
    @Autowired
    private TownConverter townConverter;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private TownRepository townRepository;

    @Override
    public LotDto convertToDto(Lot lot) {

        LotDto lotDto = new LotDto();
        lotDto.setId(lot.getId());
        lotDto.setTitle(lot.getTitle());
        lotDto.setDescription(lot.getDescription());
        lotDto.setPrice(lot.getPrice());
        lotDto.setPriceStart(lot.getPriceStart());
        lotDto.setStep(lot.getStep());
        lotDto.setImage(lot.getImage());
        lotDto.setViews(lot.getViews());
        lotDto.setCloses(lot.getCloses());
        lotDto.setOpened(lot.getOpened());
        if (lot.getCategory() != null) {
            lotDto.setCategory(categoryConverter.convertToDto(lot.getCategory()));
        }
        if (lot.getCompany() != null) {
            lotDto.setCompany(companyConverter.convertToDto(lot.getCompany()));
        }
        if (lot.getTown() != null) {
            lotDto.setTown(townConverter.convertToDto(lot.getTown()));
        }
        return lotDto;
    }

    @Override
    public Lot convertToEntity(LotDto lotDto) {

        Lot lot = new Lot();
        lot.setId(lotDto.getId());
        lot.setTitle(lotDto.getTitle());
        lot.setDescription(lotDto.getDescription());
        lot.setPrice(lotDto.getPrice());
        lot.setPriceStart(lotDto.getPriceStart());
        lot.setStep(lotDto.getStep());
        lot.setImage(lotDto.getImage());
        lot.setViews(lotDto.getViews());
        lot.setCloses(lotDto.getCloses());
        lot.setOpened(lotDto.getOpened());
        if (lotDto.getCategory() != null) {
            lot.setCategory(categoryRepository.findById(lotDto.getCategory().getId()).orElse(null));
        }
        if (lotDto.getCompany() != null) {
            lot.setCompany(companyRepository.findById(lotDto.getCompany().getId()).orElse(null));
        }
        if (lotDto.getTown() != null) {
            lot.setTown(townRepository.findById(lotDto.getTown().getId()).orElse(null));
        }
        return lot;
    }
}
