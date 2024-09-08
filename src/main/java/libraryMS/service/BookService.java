package libraryMS.service;

import libraryMS.dao.BookDao;
import libraryMS.domain.Book;
import libraryMS.service.generic.GenericService;
import libraryMS.utils.service.PaginationService;
import libraryMS.utils.service.RefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService extends GenericService<BookDao, Book, Integer> {
    @Autowired
    public BookService(BookDao dao, RefService refService, PaginationService paginationService) {
        super(dao, refService, paginationService);
    }
}
