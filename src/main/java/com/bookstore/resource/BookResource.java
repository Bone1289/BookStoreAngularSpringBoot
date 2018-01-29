package com.bookstore.resource;

import com.bookstore.domain.Book;
import com.bookstore.service.BookService;
import com.bookstore.service.StorageService;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookResource {

    @Autowired
    private BookService bookService;
    @Autowired
    StorageService storageService;

    List<String> files = new ArrayList<>();

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Book addBook(@RequestBody Book book) {
        return bookService.save(book);
    }

//    @RequestMapping(value = "/add/image", method = RequestMethod.POST)
//    public ResponseEntity upload(
//            @RequestParam("id") Long id,
//            HttpServletResponse response,
//            HttpServletRequest request
//    ) {
//        try {
//            Book book = bookService.findOne(id);
//            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
//            Iterator<String> it = multipartHttpServletRequest.getFileNames();
//            MultipartFile multipartFile = multipartHttpServletRequest.getFile(it.next());
//            String fileName = id + ".png";
//
//            byte[] bytes = multipartFile.getBytes();
//            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream("src/main/resources/static/image/book/" + fileName));
//            stream.write(bytes);
//            stream.close();
//            return new ResponseEntity("Upload Success!", HttpStatus.OK);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return new ResponseEntity("Upload failure!", HttpStatus.BAD_REQUEST);
//        }
//    }

    @RequestMapping(value = "/add/image", method = RequestMethod.POST)
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        String message;
        try {
            storageService.store(file);
            files.add(file.getOriginalFilename());
            message = "You successfully uploaded " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            message = "FAIL to upload " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }


    @RequestMapping(value = "/update/image", method = RequestMethod.POST)
    public ResponseEntity updateImage(
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
            if (Files.exists(Paths.get("src/main/resources/static/image/book/" + fileName))) {
                Files.delete(Paths.get("src/main/resources/static/image/book/" + fileName));
            }
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

    @RequestMapping("/bookList")
    public List<Book> getBookById() {
        return bookService.findAll();
    }

    @RequestMapping("/{id}")
    public Book getBookById(@PathVariable("id") Long id) {
        Book book = bookService.findOne(id);
        return book;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Book updateBookPost(@RequestBody Book book) {
        return bookService.save(book);
    }
}
