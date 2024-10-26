//import { HttpClient, HttpParams } from '@angular/common/http';
import {HttpClient} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/app/environments/environment';
import {OrderArticle} from "../dto/order-article";


const baseUri = environment.backendUrl + '/order-articles';

@Injectable({
  providedIn: 'root'
})
export class OrderArticlesService {

  constructor(private http: HttpClient) {
  }

  /** TODO change doc !!!!!!!!!!!!!!!!!!
   * Find an existing order by its id
   *
   * @param id
   * @return an Observable for the searched order
   */
  getById(orderId: number, articleId: number): Observable<OrderArticle> {
    return this.http.get<OrderArticle>(`${baseUri}/${orderId}/${articleId}`);
  }

  /** TODO change doc!!!!!!!!!!!!!!!!!!!!!!!!!!!!
   * Create a new order in the system.
   *
   * @param orderArticle the data for the order that should be created
   * @return an Observable for the created order
   */
  create(orderArticle: OrderArticle): Observable<OrderArticle> {
    return this.http.post<OrderArticle>(baseUri, orderArticle);
  }

}
