package com.project.springbookhub.controller;

import com.project.springbookhub.payload.ResponseMessage;
import com.project.springbookhub.payload.request.BookRequest;
import com.project.springbookhub.payload.response.BookResponse;
import com.project.springbookhub.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;


    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseMessage<BookResponse> createBook (@RequestBody @Valid BookRequest bookRequest) {
        return bookService.createBook(bookRequest);
    }


    @GetMapping("/getBookById/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT','ROLE_ADMIN')")
    public ResponseMessage<BookResponse> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @GetMapping("/getAllBook")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<BookResponse> getAllBooks(@RequestParam(required = false) String author,
                                          @RequestParam(required = false) String title) {
        return bookService.getAllBooksFilteredByAuthorAndTitle(author, title);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT','ROLE_ADMIN')")
    public Page<BookResponse> search(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "sort") String sort,
            @RequestParam(value = "type") String type){
        return bookService.findBookByPage(page,size,sort,type);
    }


    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseMessage<BookResponse> updateBook(@PathVariable Long id,
                                                             @RequestBody @Valid BookRequest bookRequest) {
        return bookService.updateBook(id, bookRequest);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseMessage<?> deleteBook (@PathVariable Long id) {
        return bookService.deleteBook(id);
    }

    @PutMapping("/updateStock/{Id}")
    public ResponseMessage<BookResponse> updateBookStock(@PathVariable Long id, @RequestParam int quantity) {
        return bookService.updateBookStock(id, quantity);

    }
}
