package libraryMS.utils.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pagination {
    private int page;
    @Nullable
    private Integer size;
    @Nullable
    private Integer start;
    private String orderBy;
    private String orderType;
    private Boolean returnCount;
    private Integer maxResult;
}
