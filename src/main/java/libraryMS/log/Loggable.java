package libraryMS.log;

import javax.persistence.Transient;

public interface Loggable {
    @Transient
    String fetchId();
    @Transient
    String getRelatedEntity();
    @Transient
    void setQuery(String query);
    @Transient
    String getQuery();

}
