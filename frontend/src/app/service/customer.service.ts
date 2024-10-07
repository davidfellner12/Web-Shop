import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Customer, CustomerListDto, CustomerSearch } from 'src/app/dto/customer';
import { environment } from 'src/app/environments/environment';

const baseUri = environment.backendUrl + '/customers';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private _loggedInCustomer: CustomerListDto | undefined;

  constructor(private http: HttpClient) {
  }

  /**
   * Setter for the logged in customer
   *
   * @param customer The customer to be logged in
   */
  set loggedInCustomer(customer: CustomerListDto) {
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
    return this.http.get<Customer>(`${baseUri}/${id}`);
  }

  /**
   * Find all customers by the given filter criteria
   *
   * @param filter
   */
  getAllByFilter(filter: CustomerSearch): Observable<CustomerListDto[]> {
    let params = new HttpParams();
    if (filter.firstName?.trim()) {
      params = params.append('firstName', filter.firstName as string);
    }
    if (filter.lastName?.trim()) {
      params = params.append('lastName', filter.lastName as string);
    }
    if (filter.email?.trim()) {
      params = params.append('email', filter.email as string);
    }
    if (filter.dateOfBirthEarliest?.trim()) {
      params = params.append('dateOfBirthEarliest', filter.dateOfBirthEarliest as string);
    }
    if (filter.dateOfBirthLatest?.trim()) {
      params = params.append('dateOfBirthLatest', filter.dateOfBirthLatest as string);
    }
    if (filter.minAge != null) {
      params = params.append('minAge', filter.minAge as number);
    }
    if (filter.maxAge != null) {
      params = params.append('maxAge', filter.maxAge as number);
    }
    return this.http.get<CustomerListDto[]>(baseUri, { params });
  }


  /**
   * Create a new customer in the system.
   *
   * @param customer the data for the customer that should be created
   * @return an Observable for the created customer
   */
  create(customer: Customer): Observable<Customer> {
    return this.http.post<Customer>(baseUri, customer);
  }

  /**
   * Updates a specific customer in the system
   *
   * @param customer the data for the customer that should be updated
   * @return an Observable for the updating of a customer
   */

  update(customer: Customer): Observable<Customer> {
    return this.http.put<Customer>(baseUri, customer);
  }

}
