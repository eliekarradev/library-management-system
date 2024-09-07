package libraryMS.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import libraryMS.domain.generic.GenericDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Borrowing_Records")
public class BorrowingRecord extends GenericDomain {

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "Borrowing_Date")
    Date borrowingDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "Return_Date")
    Date returnDate;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "patron_id")
    private Patron patron;
}
