package libraryMS.controller;

import libraryMS.controller.generic.GenericController;
import libraryMS.dao.BookDao;
import libraryMS.domain.Book;
import libraryMS.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/books")
@RequiredArgsConstructor
public class BookController extends GenericController<BookService, BookDao, Book, Integer> {

}
