package by.company.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CompanyDto extends BaseDto {

    @NotEmpty
    @Pattern(regexp = "[0-9a-zA-Z\u0400-\u04ff -]{3,30}")
    private String name;
}
