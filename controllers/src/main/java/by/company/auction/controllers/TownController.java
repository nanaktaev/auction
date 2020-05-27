package by.company.auction.controllers;

import by.company.auction.dto.TownDto;
import by.company.auction.services.TownService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/towns")
public class TownController {

    @Autowired
    private TownService townService;

    @GetMapping
    public List<TownDto> getAllTowns() {

        log.debug("getAllTowns()");

        return townService.findAll();
    }

    @GetMapping("/{id}")
    public TownDto getTownById(@PathVariable Integer id) {

        log.debug("getTownById() id = {}", id);

        return townService.findById(id);
    }

    @PostMapping
    public TownDto registerTown(@RequestBody @Valid TownDto townDto) {

        log.debug("registerTown() townDto = {}", townDto);

        return townService.create(townDto);
    }

    @DeleteMapping("/{id}")
    public void deleteTown(@PathVariable Integer id) {

        log.debug("deleteTown() id = {}", id);

        townService.delete(id);
    }

    @PutMapping("/{id}")
    public TownDto updateTown(@PathVariable Integer id, @RequestBody @Valid TownDto townDto) {

        townDto.setId(id);

        log.debug("updateTown() townDto = {}", townDto);

        return townService.update(townDto);
    }

}
