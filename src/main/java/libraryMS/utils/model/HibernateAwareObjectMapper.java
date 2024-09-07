package libraryMS.utils.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import java.util.TimeZone;


public class HibernateAwareObjectMapper extends ObjectMapper {
    /**
     * This function response for mapping all object when spring return it to the response
     * The function mapping all the keys and try to make Serialize the values
     * */
    public HibernateAwareObjectMapper() {
        Hibernate5Module hm = new Hibernate5Module();
        hm.disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);
        registerModule(hm).setTimeZone(TimeZone.getDefault());
    }
}
