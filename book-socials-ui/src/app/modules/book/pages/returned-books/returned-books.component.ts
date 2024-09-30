import { Component } from '@angular/core';
import {
  BorrowedBookResponse,
  PageResponseBorrowedBookResponse,
} from '../../../../services/models';
import { BookService } from '../../../../services/services/book.service';

@Component({
  selector: 'app-returned-books',
  templateUrl: './returned-books.component.html',
  styleUrl: './returned-books.component.scss',
})
export class ReturnedBooksComponent {
  returnedBooks: PageResponseBorrowedBookResponse = {};
  page: number = 0;
  pageSize: number = 10;
  message: string = '';
  messageType: string = '';
  constructor(private bookService: BookService) {}
  ngOnInit(): void {
    this.findAllReturnedBooks();
  }
  findAllReturnedBooks() {
    this.bookService
      .findAllReturnedBooks({
        page: this.page,
        pageSize: this.pageSize,
      })
      .subscribe({
        next: (response) => {
          this.returnedBooks = response;
        },
      });
  }
  goToLastPage() {
    this.page = (this.returnedBooks.totalPages as number) - 1;
    this.findAllReturnedBooks();
  }
  goToNextPage() {
    this.page++;
    this.findAllReturnedBooks();
  }
  goToPage(pageIndex: number) {
    this.page = pageIndex;
    this.findAllReturnedBooks();
  }
  goToPreviousPage() {
    this.page--;
    this.findAllReturnedBooks();
  }
  goToFirstPage() {
    this.page = 0;
    this.findAllReturnedBooks();
  }
  get isLastPage(): boolean {
    return this.page == (this.returnedBooks.totalPages as number) - 1;
  }
  approveBookReturned(book: BorrowedBookResponse) {
    if (!book.returned) {
      this.message = 'Book is not yet retured!';
      this.messageType = 'error';
      return;
    }
    this.bookService
      .approveReturnedBorrowedBook({
        bookUuid: book.uuid as string,
      })
      .subscribe({
        next: () => {
          this.message = 'Book returned approved';
          this.messageType = 'success';
          this.findAllReturnedBooks();
        },
      });
  }
}
