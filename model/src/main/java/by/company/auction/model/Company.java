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
@Table(name = "companies")
@SequenceGenerator(name = "id_generator", sequenceName = "company_sequence")
@EqualsAndHashCode(callSuper = true)
public class Company extends BaseEntity {

    @NotEmpty
    private String name;
}
