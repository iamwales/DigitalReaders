/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { PageResponseFeedBackResponse } from '../../models/page-response-feed-back-response';

export interface GetAllFeedBacksByBook$Params {
  bookUuid: string;
  page?: number;
  pageSize?: number;
}

export function getAllFeedBacksByBook(http: HttpClient, rootUrl: string, params: GetAllFeedBacksByBook$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseFeedBackResponse>> {
  const rb = new RequestBuilder(rootUrl, getAllFeedBacksByBook.PATH, 'get');
  if (params) {
    rb.path('bookUuid', params.bookUuid, {});
    rb.query('page', params.page, {});
    rb.query('pageSize', params.pageSize, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<PageResponseFeedBackResponse>;
    })
  );
}

getAllFeedBacksByBook.PATH = '/feedbacks/book/{bookUuid}';
