package by.company.auction.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SuppressWarnings("unused")
public class LotDtoUpdate extends LotDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal price;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal priceStart;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal step;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime opened;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private CompanyDto company;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer views;
}
