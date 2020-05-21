package by.company.auction.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bids")
@SequenceGenerator(name = "id_generator", sequenceName = "bid_sequence")
@EqualsAndHashCode(callSuper = true)
public class Bid extends BaseEntity {

    @NotNull
    private BigDecimal value;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "lot_id")
    private Lot lot;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public String toString() {
        return "Ставка величиной " + value
                + ". Объявлена " + time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                + " (id пользователя - " + user.getId()
                + ", id лота - " + lot.getId() + ")";
    }

}
