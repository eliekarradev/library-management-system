package libraryMS.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import libraryMS.domain.generic.GenericDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Logs_Audit")
public class LogDomain extends GenericDomain {

    @ManyToOne
    @JoinColumn(name = "Application_User_Id", referencedColumnName = "id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    ApplicationUser applicationUser;

    @Column(name = "Entity_Name")
    private String entityName;

    @Column(name = "Entity_Id")
    private String entityId;

    @Column(name = "New_Value")
    private String newValue;

    @Column(name = "Old_Value")
    private String oldValue;

    @Column(name = "Amount")
    private String amount;
}
