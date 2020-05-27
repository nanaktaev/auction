package by.company.auction.converters;

import by.company.auction.dto.CompanyDto;
import by.company.auction.model.Company;
import org.springframework.stereotype.Component;

@Component
public class CompanyConverter extends AbstractConverter<Company, CompanyDto> {

    @Override
    public CompanyDto convertToDto(Company company) {

        CompanyDto companyDto = new CompanyDto();
        companyDto.setId(company.getId());
        companyDto.setName(company.getName());

        return companyDto;
    }

    @Override
    public Company convertToEntity(CompanyDto companyDto) {

        Company company = new Company();
        company.setId(companyDto.getId());
        company.setName(companyDto.getName());

        return company;

    }
}