package by.company.auction.controllers;

import by.company.auction.dto.CompanyDto;
import by.company.auction.services.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping
    public List<CompanyDto> getAllCompanies() {

        log.debug("getAllCompanies()");

        return companyService.findAll();
    }

    @GetMapping("/{id}")
    public CompanyDto getCompanyById(@PathVariable Integer id) {

        log.debug("getCompanyById() id = {}", id);

        return companyService.findById(id);
    }

    @PostMapping
    public CompanyDto registerCompany(@RequestBody @Valid CompanyDto companyDto) {

        log.debug("registerCompany() companyDto = {}", companyDto);

        return companyService.create(companyDto);
    }

    @DeleteMapping("/{id}")
    public void deleteCompany(@PathVariable Integer id) {

        log.debug("deleteCompany() id = {}", id);

        companyService.delete(id);
    }

    @PutMapping("/{id}")
    public CompanyDto updateCompany(@PathVariable Integer id, @RequestBody @Valid CompanyDto companyDto) {

        companyDto.setId(id);

        log.debug("updateCompany() companyDto = {}", companyDto);

        return companyService.update(companyDto);
    }

}
