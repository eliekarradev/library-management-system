package libraryMS.service;


import libraryMS.dao.PatronDao;
import libraryMS.domain.Patron;
import libraryMS.service.generic.GenericService;
import org.springframework.stereotype.Service;

@Service
public class PatronService extends GenericService<PatronDao, Patron, Integer> {
}
