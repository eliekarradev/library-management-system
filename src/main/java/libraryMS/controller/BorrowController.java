package libraryMS.controller;

import libraryMS.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BorrowController {

    private final BorrowService borrowService;


    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping(value = "/borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<Object> borrow(@PathVariable Integer bookId,@PathVariable Integer patronId){
        return borrowService.borrow(bookId,patronId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping(value = "/return/{bookId}/patron/{patronId}")
    public ResponseEntity<Object> returnBook(@PathVariable Integer bookId,@PathVariable Integer patronId){
        return borrowService.returnBook(bookId,patronId);
    }


}
