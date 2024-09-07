package libraryMS.dao;


import libraryMS.domain.ApplicationUser;
import libraryMS.dao.generic.GenericDao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApplicationUserDao extends GenericDao<ApplicationUser,Integer> {


    @Query("SELECT C FROM ApplicationUser C " +
            "where C.email LIKE :email AND C.recordStatus != -1")
    ApplicationUser findByEmail(@Param("email")String email);

}


