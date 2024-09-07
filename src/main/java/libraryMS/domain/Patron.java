package libraryMS.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import libraryMS.domain.generic.GenericDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Patrons")
public class Patron extends GenericDomain {

    @NotBlank(message = "name must not be null")
    @Column(name = "Name", nullable = false)
    private String name;

    @NotBlank(message = "contactInformation must not be null")
    @Column(name = "Contact_Information", nullable = false)
    private String contactInformation;

    @JsonIgnoreProperties(value = "patron")
    @OneToMany(mappedBy = "patron", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BorrowingRecord> borrowingRecords;
}
