package ru.otus.akutsev.books.shell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.akutsev.books.dao.AuthorDao;
import ru.otus.akutsev.books.dao.GenreDao;
import ru.otus.akutsev.books.model.Author;
import ru.otus.akutsev.books.model.Book;
import ru.otus.akutsev.books.model.Genre;
import ru.otus.akutsev.books.service.BookService;

@ShellComponent
public class ApplicationShellCommands {

	private final BookService bookService;
	private final AuthorDao authorDao;
	private final GenreDao genreDao;

	@Autowired
	public ApplicationShellCommands(BookService bookService, AuthorDao authorDao, GenreDao genreDao) {
		this.bookService = bookService;
		this.authorDao = authorDao;
		this.genreDao = genreDao;
	}

	@ShellMethod(value = "Add book", key = {"add"})
	@Transactional
	public void addBook(String name, int authorId, int genreId) {
		Book book = new Book();

		Author author = authorDao.findById(authorId).get();
		Genre genre = genreDao.findById(genreId).get();
		book.setName(name);
		book.setGenre(genre);
		book.setAuthor(author);

		bookService.save(book);
		System.out.println("Book added");
	}

	@ShellMethod(value = "Show all books", key = {"all"})
	public void showAllBooks() {
		System.out.println("All stored books:");
		bookService.getAll().forEach(System.out::println);
	}

	@ShellMethod(value = "Update Book", key = {"update"})
	@Transactional
	public void updateBook(int id, String newName, int newAuthorId, int newGenreId) {
		bookService.updateBook(bookService.getAById(id).get(), newName, authorDao.findById(newAuthorId).get(),
				genreDao.findById(newGenreId).get());
		System.out.println(String.format("Book with id %s changed", id));
	}

	@ShellMethod(value = "Delete book", key = {"delete"})
	public void deleteBook(int id) {
		Book bookToDelete = bookService.getAById(id).get();
		bookService.delete(bookToDelete);
		System.out.println(String.format("Book with id = %s deleted", id));
	}

	@ShellMethod(value = "Show all book's comments", key = {"comments"})
	public void showAllComments(int id) {
		Book bookToShowComments = bookService.getAById(id).get();
		bookService.getAllComments(bookToShowComments).forEach(System.out::println);
	}
}
