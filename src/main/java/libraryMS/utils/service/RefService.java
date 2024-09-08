package libraryMS.utils.service;

import jakarta.persistence.PersistenceContext;
import libraryMS.domain.generic.GenericDomain;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
//@RequiredArgsConstructor
public class RefService {

    @PersistenceContext
    private  EntityManager em;

    public void linkChildrenWithParents(Object domain, List<Class<?>> parents) {
        try {
            for (PropertyDescriptor pd : Introspector.getBeanInfo(domain.getClass()).getPropertyDescriptors()) {
                Class<?> classType = pd.getPropertyType();
                Object propertyValue = pd.getReadMethod().invoke(domain);

                parents.add(domain.getClass());
                // if the property is list or set
                if (classType.equals(Set.class) || classType.equals(List.class)) {
                    // if the property is null initialize the Set
                    if (propertyValue == null) {
                        pd.getWriteMethod().invoke(domain, classType.equals(Set.class) ? new HashSet() : new ArrayList());
                        continue;
                    }
                    // if the Set is empty,
                    // so there is no children to link with it's parents
                    if (((Set) propertyValue).size() == 0) continue;
                    // if the type of Set elements is not extends generic domain,
                    // so there is no need to link
                    if (!(((Set) propertyValue).toArray()[0] instanceof GenericDomain)) continue;

                    Class<?> childClassType = ((Set) propertyValue).toArray()[0].getClass();
                    Method setParentMethod = getParentSetterMethod(domain.getClass(), childClassType);

                    // link the parents with all it's children by invoke setParentMethod

                    for (Object child : (Set) propertyValue) {
                        parents.add(child.getClass());
                        if (!parents.contains(child.getClass()))
                            linkChildrenWithParents(child, parents);
                        setParentMethod.invoke(child, domain);
                        parents.remove(child.getClass());
                    }


                } else {
                    Session session = em.unwrap(Session.class);
                    // link parents with it's children also for other single type property
                    if (propertyValue instanceof GenericDomain && !session.contains(propertyValue)
                            && !parents.contains(domain.getClass()))
                        linkChildrenWithParents(propertyValue, parents);
                }
                parents.remove(domain.getClass());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Method getParentSetterMethod(Class<?> domain, Class<?> childClassType) throws Exception {
        for (PropertyDescriptor pd : Introspector.getBeanInfo(childClassType).getPropertyDescriptors()) {
            Class<?> classType = pd.getPropertyType();
            if (classType.equals(domain)) {
                return pd.getWriteMethod();
            }
        }

        return null;
    }

}

