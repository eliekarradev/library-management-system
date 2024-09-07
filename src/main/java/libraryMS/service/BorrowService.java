package libraryMS.service;

import jakarta.transaction.Transactional;
import libraryMS.dao.BookDao;
import libraryMS.dao.BorrowingRecordDao;
import libraryMS.dao.PatronDao;
import libraryMS.domain.Book;
import libraryMS.domain.BorrowingRecord;
import libraryMS.domain.Patron;
import libraryMS.exception.exceptions.NotFoundException;
import libraryMS.utils.model.ResponseObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class BorrowService {
    private final BookDao bookDao;
    private final PatronDao patronDao;

    private final BorrowingRecordDao borrowingRecordDao;

    @Transactional
    public ResponseEntity<Object> borrow(Integer bookId, Integer patronId) {
        Book book = bookDao.findById(bookId).orElse(null);
        Patron patron = patronDao.findById(patronId).orElse(null);

        if (book == null || patron == null) {
            return ResponseObject.FAILED_RESPONSE("Book or patron or both not exist, check the id ", HttpStatus.NOT_FOUND);
        }

        BorrowingRecord borrowingRecord = borrowingRecordDao.findByBookAndPatronAndAndReturnDateIsNull(book, patron);

        if (borrowingRecord != null) {
            return ResponseObject.FAILED_RESPONSE("You can't borrow this book for this patron because it is already borrowed to him", HttpStatus.FORBIDDEN);
        }

        borrowingRecord = new BorrowingRecord();

        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowingDate(new Date());
        borrowingRecordDao.save(borrowingRecord);
        return ResponseObject.SUCCESS_RESPONSE("Book with id :" + bookId + " reserved to patron : " + patronId);
    }


    @Transactional
    public ResponseEntity<Object> returnBook(Integer bookId, Integer patronId) {
        Book book = bookDao.findById(bookId).orElse(null);
        Patron patron = patronDao.findById(patronId).orElse(null);

        if (book == null || patron == null) {
            return ResponseObject.FAILED_RESPONSE("Book or patron or both not exist, check the id ", HttpStatus.NOT_FOUND);
        }

        BorrowingRecord borrowingRecord = borrowingRecordDao.findByBookAndPatronAndAndReturnDateIsNull(book, patron);

        if (borrowingRecord == null) {
            return ResponseObject.FAILED_RESPONSE("There is no borrowing record for this book and patron has to be returned", HttpStatus.NOT_FOUND);
        }

        borrowingRecord.setReturnDate(new Date());
        borrowingRecordDao.save(borrowingRecord);

        return ResponseObject.SUCCESS_RESPONSE("Borrowing record for Book with id :" + bookId + " and  patron with id : " + patronId + " has been returned Successfully");

    }

}
