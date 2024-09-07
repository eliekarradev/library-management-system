package libraryMS.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import libraryMS.domain.generic.GenericDomain;
import libraryMS.log.Loggable;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Nationalized;
import org.hibernate.validator.constraints.Length;

import java.util.Set;


@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "Books")
public class Book extends GenericDomain {
    @NotBlank(message = "Title must not be null")
    @Column(name = "Title", nullable = false)
    private String title;

    @NotBlank(message = "Author must not be null")
    @Column(name = "Author", nullable = false)
    private String author;



    @Min(value = 1800,message = "publicationYear must be more than 1800")
    @Column(name = "Publication_Year", nullable = false)
    private int publicationYear;

    @JsonProperty("ISBN")
    @NotBlank
    @Length(message = "ISBN cannot be more than 13 character",max = 13)
    @Column(name = "ISBN", nullable = false, unique = true, length = 13)
    private String ISBN;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = "book")
    private Set<BorrowingRecord> borrowingRecords;
}
