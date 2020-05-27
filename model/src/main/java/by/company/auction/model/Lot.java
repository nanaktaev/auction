package by.company.auction.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lots")
@SequenceGenerator(name = "id_generator", sequenceName = "lot_sequence")
@EqualsAndHashCode(callSuper = true)
public class Lot extends BaseEntity {

    @NotEmpty
    private String title;

    @NotEmpty
    @Column(columnDefinition = "text")
    private String description;

    @NotNull
    private BigDecimal price;

    @NotNull
    private BigDecimal priceStart;

    @NotNull
    private BigDecimal step;

    private String image;

    private Integer views;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime opened;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime closes;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "town_id")
    private Town town;
}
