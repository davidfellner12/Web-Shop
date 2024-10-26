import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import { Customer, CustomerListDto } from 'src/app/dto/customer';
import { environment } from 'src/app/environments/environment';
import {OrderSearch} from "../dto/order";
import {OrderListDto} from "../dto/order";
import {Order} from "../dto/order";

const baseUri = environment.backendUrl + '/orders';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private _loggedInCustomer: CustomerListDto | undefined;

  constructor(private http: HttpClient) {
  }

  /**
   * Setter for the logged in customer
   *
   * @param customer The customer to be logged in
   */
  set loggedInCustomer(customer: CustomerListDto | undefined) {
    this._loggedInCustomer = customer;
  }

  /**
   * Getter for the logged in customer
   */
  get loggedInCustomer(): CustomerListDto | undefined {
    return this._loggedInCustomer;
  }

  /**
   * Find an existing customer by its id
   *
   * @param id
   */
  getById(id: number): Observable<Customer> {
    console.log("Here is some information to observables")
    console.log(`${baseUri}/${id}`);
    return this.http.get<Customer>(`${baseUri}/${id}`);
  }

  /**
   * Find all orders by the given filter criteria
   *
   * @param filter
   */
  getAllByFilter(filter: OrderSearch): Observable<OrderListDto[]> {
    let params = new HttpParams();
    if (filter.name?.trim()) {
      params = params.append('firstName', filter.name as string);
    }
    if (filter.designation?.trim()) {
      params = params.append('lastName', filter.designation as string);
    }
    if (filter.dateOfPurchaseEarliest?.trim()) {
      params = params.append('totalPrice', filter.dateOfPurchaseEarliest as string);
    }
    if (filter.dateOfPurchaseLatest?.trim()) {
      params = params.append('dateOfBirthLatest', filter.dateOfPurchaseLatest as string);
    }
    console.log("Here are the params" + params);
    return this.http.get<OrderListDto[]>(baseUri, { params });

  }

  /**
   * Create a new order in the system.
   *
   * @param orders the data for the order that should be created
   * @return an Observable for the created order
   */
  create(orders: Order): Observable<Order> {
    return this.http.post<Order>(baseUri, orders);
  }
}
