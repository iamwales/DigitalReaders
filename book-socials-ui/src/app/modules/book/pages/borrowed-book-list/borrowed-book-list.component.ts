import { Component, OnInit } from '@angular/core';
import {
  BorrowedBookResponse,
  FeedBackRequest,
  PageResponseBorrowedBookResponse,
} from '../../../../services/models';
import { BookService, FeedbackService } from '../../../../services/services';

@Component({
  selector: 'app-borrowed-book-list',
  templateUrl: './borrowed-book-list.component.html',
  styleUrl: './borrowed-book-list.component.scss',
})
export class BorrowedBookListComponent implements OnInit {
  borrowedBooks: PageResponseBorrowedBookResponse = {};
  page: number = 0;
  pageSize: number = 10;
  message: string = '';
  messageType: string = '';
  selectedBook: BorrowedBookResponse | undefined = undefined;
  feedBackRequest: FeedBackRequest = { bookUuid: '', comment: '', note: 0 };
  constructor(
    private bookService: BookService,
    private feedBackService: FeedbackService
  ) {}
  ngOnInit(): void {
    this.findAllBorrowedBooks();
  }
  returnBorrowedBook(book: BorrowedBookResponse) {
    this.selectedBook = book;
    this.feedBackRequest.bookUuid = book.uuid as string;
  }
  findAllBorrowedBooks() {
    this.bookService
      .findAllBorrowedBooks({
        page: this.page,
        pageSize: this.pageSize,
      })
      .subscribe({
        next: (response) => {
          this.borrowedBooks = response;
        },
      });
  }
  goToLastPage() {
    this.page = (this.borrowedBooks.totalPages as number) - 1;
    this.findAllBorrowedBooks();
  }
  goToNextPage() {
    this.page++;
    this.findAllBorrowedBooks();
  }
  goToPage(pageIndex: number) {
    this.page = pageIndex;
    this.findAllBorrowedBooks();
  }
  goToPreviousPage() {
    this.page--;
    this.findAllBorrowedBooks();
  }
  goToFirstPage() {
    this.page = 0;
    this.findAllBorrowedBooks();
  }
  get isLastPage(): boolean {
    return this.page == (this.borrowedBooks.totalPages as number) - 1;
  }
  returnBook(withFeedback: boolean) {
    this.bookService
      .returnBorrowedBook({
        bookUuid: this.selectedBook?.uuid as string,
      })
      .subscribe({
        next: () => {
          if (withFeedback) {
            this.giveFeedBack();
          }
          this.selectedBook = undefined;
          this.findAllBorrowedBooks();
        },
        error: (err) => {
          if (err.error.businessErrorDescription) {
            this.message = err.error.businessErrorDescription;
            this.messageType = 'error';
          }
        },
      });
  }
  giveFeedBack() {
    this.feedBackService
      .saveFeedBack({
        body: this.feedBackRequest,
      })
      .subscribe({
        next: () => {},
        error: (err) => {
          if (err.error.businessErrorDescription) {
            this.message = err.error.businessErrorDescription;
            this.messageType = 'error';
          }
        },
      });
  }
}
