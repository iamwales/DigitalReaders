import { Component, OnInit } from '@angular/core';
import { BookService } from '../../../../services/services/book.service';
import { Router } from '@angular/router';
import {
  PageResponseBookResponse,
  BookResponse,
} from '../../../../services/models';

@Component({
  selector: 'app-my-books',
  templateUrl: './my-books.component.html',
  styleUrl: './my-books.component.scss',
})
export class MyBooksComponent implements OnInit {
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
      .findAllBooksByOwner({
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
  editBook(book: BookResponse) {
    throw new Error('Method not implemented.');
  }
  shareBook(book: BookResponse) {
    throw new Error('Method not implemented.');
  }
  archiveBook(book: BookResponse) {
    throw new Error('Method not implemented.');
  }
}
