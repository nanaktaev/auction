package by.company.auction.controllers;

import by.company.auction.dto.LotDto;
import by.company.auction.dto.LotDtoUpdate;
import by.company.auction.services.LotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/lots")
public class LotController {

    @Autowired
    private LotService lotService;

    @GetMapping("/{id}")
    public LotDto getLotById(@PathVariable Integer id) {

        log.debug("getLotById() id = {}", id);

        return lotService.findById(id);
    }

    @GetMapping
    public List<LotDto> getLots(@PageableDefault(size=4, sort = "closes") Pageable pageable,
                                @RequestParam(required = false) Integer categoryId,
                                @RequestParam(required = false) Integer townId) {

        log.debug("getLots() categoryId = {}, townId = {}", categoryId, townId);

        List<LotDto> lotDtos;

        if (categoryId == null & townId == null) {
            lotDtos = lotService.findAll(pageable);
        } else if (categoryId != null & townId != null) {
            lotDtos = lotService.findLotsByCategoryIdAndTownId(categoryId, townId, pageable);
        } else if (categoryId != null & townId == null) {
            lotDtos = lotService.findLotsByCategoryId(categoryId, pageable);
        } else {
            lotDtos = lotService.findLotsByTownId(townId, pageable);
        }

        return lotDtos;
    }

    @PostMapping
    public LotDto createLot(@RequestBody @Valid LotDto lotDto) {

        log.debug("createLot() lotDto = {}", lotDto);

        return lotService.create(lotDto);
    }

    @DeleteMapping("/{id}")
    public void deleteLot(@PathVariable Integer id) {

        log.debug("deleteLot() id = {}", id);

        lotService.delete(id);
    }

    @PatchMapping("/{id}")
    public LotDto patchLot(@PathVariable Integer id, @RequestBody @Valid LotDtoUpdate lotDto) {

        lotDto.setId(id);

        log.debug("patchLot() lotDto = {}", lotDto);

        return lotService.editLot(lotDto);
    }

}
