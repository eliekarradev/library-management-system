package libraryMS.service;


import libraryMS.dao.PatronDao;
import libraryMS.domain.Patron;
import libraryMS.service.generic.GenericService;
import libraryMS.utils.service.PaginationService;
import libraryMS.utils.service.RefService;
import org.springframework.stereotype.Service;

@Service
public class PatronService extends GenericService<PatronDao, Patron, Integer> {
    public PatronService(PatronDao dao, RefService refService, PaginationService paginationService) {
        super(dao, refService, paginationService);
    }
}
