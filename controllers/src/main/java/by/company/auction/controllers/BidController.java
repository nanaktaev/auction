package by.company.auction.controllers;

import by.company.auction.dto.BidDto;
import by.company.auction.services.BidService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bids")
public class BidController {

    @Autowired
    BidService bidService;

    @GetMapping
    public List<BidDto> getBids(@RequestParam(required = false) Integer lotId) {

        log.debug("findBids() lotId = {}", lotId);

        List<BidDto> bidDtos;

        if (lotId == null) {
            bidDtos = bidService.findAll();
        } else {
            bidDtos = bidService.findBidsByLotId(lotId);
        }

        Collections.reverse(bidDtos);

        return bidDtos;
    }

    @PostMapping
    public BidDto makeBid(@Valid @RequestBody BidDto bidDto) {

        log.debug("create() bidDto = {}", bidDto);

        return bidService.makeBid(bidDto);
    }

    @DeleteMapping("/{id}")
    public void deleteBid(@PathVariable Integer id) {

        log.debug("deleteBid() id = {}", id);

        bidService.delete(id);
    }

}
