package by.company.auction.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LotDto extends BaseDto {

    private String title;

    private String description;

    private BigDecimal price;

    private BigDecimal priceStart;

    private BigDecimal step;

    private String image;

    private Integer views;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime opened;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime closes;

    private CategoryDto category;

    private CompanyDto company;

    private TownDto town;
}
