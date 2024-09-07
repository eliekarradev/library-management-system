package libraryMS.domain.generic;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import libraryMS.domain.ApplicationUser;
import libraryMS.security.service.ApplicationUserDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;


@MappedSuperclass
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Setter
@Getter
public abstract class GenericDomain implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "Record_Status")
    int recordStatus = 1;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "Creation_Date", updatable = false)
    Date creationDate;


    @Column(name = "CreatorId", updatable = false)
    Integer creatorId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "Modified_Date")
    Date modifiedDate;

    @Column(name = "ModifierId")
    Integer modifierId;

    @PrePersist
    public void initialValuesForInsert() {
        this.creationDate = new Date();
        this.recordStatus = 1;
    }

    @PreUpdate
    public void initialValuesForUpdate() {
        this.modifiedDate = new Date();
        var user = ApplicationUserDetails.getCurrentUser();
        if (user != null)
            this.modifierId = user.getId();
    }
}
