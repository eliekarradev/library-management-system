package libraryMS.controller;

import libraryMS.controller.generic.GenericController;
import libraryMS.dao.PatronDao;
import libraryMS.domain.Patron;
import libraryMS.service.PatronService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/patrons")
public class PatronController extends GenericController<PatronService, PatronDao, Patron, Integer> {
}
