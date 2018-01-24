package com.bookstore.resource;

import com.bookstore.domain.Book;
import com.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.Iterator;

@RestController
@RequestMapping("/book")
public class BookResource {

    @Autowired
    private BookService bookService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Book addBook(@RequestBody Book book) {
        return bookService.save(book);
    }

    @RequestMapping(value = "/add/image", method = RequestMethod.POST)
    public ResponseEntity upload(
            @RequestParam("id") Long id,
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        try {
            Book book = bookService.findOne(id);
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            Iterator<String> it = multipartHttpServletRequest.getFileNames();
            MultipartFile multipartFile = multipartHttpServletRequest.getFile(it.next());
            String fileName = id + ".png";

            byte[] bytes = multipartFile.getBytes();
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream("src/main/resources/static/image/book/" + fileName));
            stream.write(bytes);
            stream.close();
            return new ResponseEntity("Upload Success!", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity("Upload failure!", HttpStatus.BAD_REQUEST);
        }
    }
}
