package libraryMS.utils.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import libraryMS.utils.model.HibernateAwareObjectMapper;

import java.util.HashMap;

public class ObjectConverter {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static HibernateAwareObjectMapper hibernateAwareObjectMapper=new HibernateAwareObjectMapper();

    public static <T> T convertToObject(Object domain, Class<T> clazz){
        try {
            if(domain instanceof String){
                return objectMapper.readValue((String)domain,clazz);
            }
            return objectMapper.convertValue(domain,clazz);
        }catch (Exception e){
            return null;
        }
        
    }

    public static HashMap convertObjectToMap(Object domain){

        HashMap map = hibernateAwareObjectMapper.convertValue(domain, HashMap.class);
        return map;
    }


}
