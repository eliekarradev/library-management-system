package libraryMS.dao;

import libraryMS.dao.generic.GenericDao;
import libraryMS.domain.Book;
import libraryMS.domain.BorrowingRecord;
import libraryMS.domain.Patron;

public interface BorrowingRecordDao extends GenericDao<BorrowingRecord, Integer> {

    BorrowingRecord findByBookAndPatronAndAndReturnDateIsNull(Book book, Patron patron);

    BorrowingRecord findByBookAndPatronAndAndReturnDateIsNotNull(Book book, Patron patron);
}
