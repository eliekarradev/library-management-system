package libraryMS.service;

import libraryMS.dao.BookDao;
import libraryMS.domain.Book;
import libraryMS.service.generic.GenericService;
import org.springframework.stereotype.Service;

@Service
public class BookService extends GenericService<BookDao, Book, Integer> {
}
