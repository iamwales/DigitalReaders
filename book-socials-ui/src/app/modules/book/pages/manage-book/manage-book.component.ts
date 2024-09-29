import { Component, OnInit } from '@angular/core';
import { BookRequest } from '../../../../services/models';
import { BookService } from '../../../../services/services';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-manage-book',
  templateUrl: './manage-book.component.html',
  styleUrl: './manage-book.component.scss',
})
export class ManageBookComponent implements OnInit {
  constructor(
    private bookService: BookService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) {}
  ngOnInit(): void {
    const bookUuid = this.activatedRoute.snapshot.params['bookUuid'];

    if (bookUuid) {
      this.bookService
        .findBookById({
          bookUuid: bookUuid,
        })
        .subscribe({
          next: (book) => {
            this.bookRequest = {
              uuid: book.uuid,
              title: book.title as string,
              authorName: book.authorName as string,
              synopsis: book.synopsis as string,
              shareable: book.shareable,
              isbn: book.isbn as string,
            };

            if (book.cover) {
              this.selecedPicture = 'data:image/jpg;base64,' + book.cover;
            }
          },
        });
    }
  }
  errorMsg: Array<String> = [];
  selectedBookCover: any;
  selecedPicture: string | undefined;
  bookRequest: BookRequest = {
    title: '',
    authorName: '',
    synopsis: '',
    isbn: '',
    shareable: false,
  };
  onFileSelected(event: any) {
    this.selectedBookCover = event.target.files[0];

    if (this.selectedBookCover) {
      const reader = new FileReader();
      reader.onload = () => {
        this.selecedPicture = reader.result as string;
      };
      reader.readAsDataURL(this.selectedBookCover);
    }
  }
  saveBook() {
    this.bookService
      .saveBook({
        body: this.bookRequest,
      })
      .subscribe({
        next: (book) => {
          if (this.selectedBookCover) {
            this.bookService
              .uploadBookCoverPicture({
                bookUuid: book.uuid || '',
                body: {
                  file: this.selectedBookCover,
                },
              })
              .subscribe({
                next: () => {
                  this.router.navigate(['/books/my-books']);
                },
              });
          } else {
            this.router.navigate(['/books/my-books']);
          }
        },
        error: (err) => {
          this.errorMsg = err.error.validationErrors;
        },
      });
  }
}
