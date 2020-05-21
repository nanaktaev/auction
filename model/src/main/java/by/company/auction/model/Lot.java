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
import java.time.format.DateTimeFormatter;

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
    private String description;

    @NotNull
    private BigDecimal price;

    @NotNull
    private BigDecimal priceStart;

    @NotNull
    private BigDecimal step;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime opened;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime closes;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "town_id")
    private Town town;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(closes);
    }

    public boolean isBurning() {
        return !isExpired() && LocalDateTime.now().plusMinutes(3).isAfter(closes);
    }

    public boolean isBidValueEnough(BigDecimal value) {
        return value.compareTo(price.add(step)) >= 0;
    }

    @Override
    public String toString() {
        return "Лот №" + getId() +
                " - " + title +
                "\n" + description +
                "\nЦена - " + price +
                " (начальная - " + priceStart +
                "), мин. повышение - " + step +
                "\nОкончание торгов - " + closes.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) +
                ", начало торгов - " + opened.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) +
                "\nкатегория - " + category.getName() +
                ", компания - " + company.getName() +
                ", город - " + town.getName() +
                ".\n";
    }

}
