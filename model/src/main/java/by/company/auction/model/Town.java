package by.company.auction.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "towns")
@SequenceGenerator(name = "id_generator", sequenceName = "town_sequence")
@EqualsAndHashCode(callSuper = true)
public class Town extends BaseEntity {

    @NotEmpty
    private String name;
}
