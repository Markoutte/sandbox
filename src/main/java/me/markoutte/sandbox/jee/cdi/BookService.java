package me.markoutte.sandbox.jee.cdi;

import javax.inject.Inject;

@Loggable
public class BookService {

    @Inject
    private NumberGenerator numberGenerator;

    public Book createBook(String title, String author, Float price, String description) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setPrice(price);
        book.setDescription(description);
        book.setNumber(numberGenerator.generateNumber());
        return book;
    }

}
