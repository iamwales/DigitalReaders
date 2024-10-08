import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BookService } from '../../../../services/services';
import {
  BookResponse,
  PageResponseBookResponse,
} from '../../../../services/models';

@Component({
  selector: 'app-book-list',
  templateUrl: './book-list.component.html',
  styleUrl: './book-list.component.scss',
})
export class BookListComponent implements OnInit {
  bookResponse: PageResponseBookResponse = {};
  page = 0;
  pageSize = 10;
  message: string = '';
  messageType: string = 'success';
  constructor(private bookService: BookService, private router: Router) {}
  ngOnInit(): void {
    this.findAllBooks();
  }
  findAllBooks() {
    this.bookService
      .findAllBooks({
        page: this.page,
        pageSize: this.pageSize,
      })
      .subscribe({
        next: (books) => {
          this.bookResponse = books;
        },
      });
  }
  goToPage(pageIndex: number) {
    this.page = pageIndex;
    this.findAllBooks();
  }
  goToFirstPage() {
    this.page = 0;
    this.findAllBooks();
  }
  goToPreviousPage() {
    this.page--;
    this.findAllBooks();
  }
  goToLastPage() {
    this.page = (this.bookResponse.totalPages as number) - 1;
    this.findAllBooks();
  }
  goToNextPage() {
    this.page++;
    this.findAllBooks();
  }
  get isLastPage(): boolean {
    return this.page == (this.bookResponse.totalPages as number) - 1;
  }
  borrowBook(book: BookResponse) {
    this.message = '';
    this.bookService
      .borrowBook({
        bookUuid: book.uuid as string,
      })
      .subscribe({
        next: () => {
          this.messageType = 'success';
          this.message = 'Book successfully added to your list';
        },
        error: (err) => {
          this.messageType = 'error';
          this.message = err.error.error;
        },
      });
  }
}
