import { Component, EventEmitter, Input, Output } from '@angular/core';
import { BookResponse } from '../../../../services/models';

@Component({
  selector: 'app-book-card',
  templateUrl: './book-card.component.html',
  styleUrl: './book-card.component.scss',
})
export class BookCardComponent {
  private _book: BookResponse = {};
  private _bookCover: string | undefined;
  private _manage: boolean = false;

  @Output() private share: EventEmitter<BookResponse> =
    new EventEmitter<BookResponse>();
  @Output() private archive: EventEmitter<BookResponse> =
    new EventEmitter<BookResponse>();
  @Output() private addToWaitingList: EventEmitter<BookResponse> =
    new EventEmitter<BookResponse>();
  @Output() private borrow: EventEmitter<BookResponse> =
    new EventEmitter<BookResponse>();
  @Output() private showDetails: EventEmitter<BookResponse> =
    new EventEmitter<BookResponse>();
  @Output() private edit: EventEmitter<BookResponse> =
    new EventEmitter<BookResponse>();

  onAddToWaitList() {
    this.addToWaitingList.emit(this._book);
  }
  onArchive() {
    this.archive.emit(this._book);
  }
  onShare() {
    this.share.emit(this._book);
  }
  onEdit() {
    this.edit.emit(this._book);
  }
  onBorrow() {
    this.borrow.emit(this._book);
  }
  onShowDetails() {
    this.showDetails.emit(this._book);
  }

  get book() {
    return this._book;
  }
  @Input()
  set book(value: BookResponse) {
    this._book = value;
  }

  get bookCover() {
    if (this._book.cover) {
      return 'data:image/jpg;base64, ' + this._book.cover;
    }
    return 'https://randomwordgenerator.com/img/picture-generator/idea-3085367_640.jpg';
  }

  @Input()
  set manage(value: boolean) {
    this._manage = value;
  }

  get manage(): boolean {
    return this._manage;
  }
}
