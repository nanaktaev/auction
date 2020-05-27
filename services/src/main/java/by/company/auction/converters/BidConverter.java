package by.company.auction.converters;

import by.company.auction.dto.BidDto;
import by.company.auction.model.Bid;
import by.company.auction.repository.LotRepository;
import by.company.auction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BidConverter extends AbstractConverter<Bid, BidDto> {

    @Autowired
    private LotConverter lotConverter;
    @Autowired
    private UserConverter userConverter;
    @Autowired
    private LotRepository lotRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public BidDto convertToDto(Bid bid) {

        BidDto bidDto = new BidDto();
        bidDto.setId(bid.getId());
        bidDto.setValue(bid.getValue());
        bidDto.setTime(bid.getTime());
        if (bid.getLot() != null) {
            bidDto.setLot(lotConverter.convertToDto(bid.getLot()));
        }
        if (bid.getUser() != null) {
            bidDto.setUser(userConverter.convertToDto(bid.getUser()));
        }
        return bidDto;
    }

    @Override
    public Bid convertToEntity(BidDto bidDto) {

        Bid bid = new Bid();
        bid.setId(bidDto.getId());
        bid.setValue(bidDto.getValue());
        bid.setTime(bidDto.getTime());
        if (bidDto.getLot() != null) {
            bid.setLot(lotRepository.findById(bidDto.getLot().getId()).orElse(null));
        }
        if (bidDto.getUser() != null) {
            bid.setUser(userRepository.findById(bidDto.getUser().getId()).orElse(null));
        }
        return bid;
    }
}
