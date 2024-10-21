import {DatePipe, NgClass} from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { debounceTime, Subject } from 'rxjs';
import {CustomerListDto, CustomerSearch} from 'src/app/dto/customer';
import { CustomerService } from 'src/app/service/customer.service';
import { ErrorFormatterService } from 'src/app/service/error-formatter.service';
import { ChangeDetectorRef } from '@angular/core'

@Component({
  selector: 'app-customers',
  standalone: true,
  imports: [
    RouterModule,
    DatePipe,
    FormsModule,
    NgClass
  ],
  templateUrl: './customers.component.html',
  styleUrl: './customers.component.scss'
})
export class CustomersComponent implements OnInit {
  customers: CustomerListDto[] = [];
  searchParams: CustomerSearch = {};
  searchChangedObservable = new Subject<void>();
  bannerError = false; /* For the setup sanity check banner. Should be removed with banner. */
  toggle = true;
  status = 'Enable';

  selectedCustomer: CustomerListDto | undefined;

  constructor(private service: CustomerService,
              private errorFormatter: ErrorFormatterService,
              private router: Router,
              private notification: ToastrService,
              private cdr: ChangeDetectorRef) {
  }

  ngOnInit(): void {
    this.getCustomers();
    this.searchChangedObservable
      .pipe(debounceTime(300))
      .subscribe({ next: () => this.getCustomers() });
  }

  getCustomers() {
    this.service.getAllByFilter(this.searchParams)
      .subscribe({
        next: customers => {
          this.customers = customers;
          this.bannerError = false;
        },
        error: error => {
          console.error('Error loading customers', error);
          this.notification.error(this.errorFormatter.format(error), 'Could not load customers');
          this.bannerError = true;
        }
      });
  }

  deleteCustomer(dto: CustomerListDto){
    console.log("The button is really clicked")
    this.service.delete(dto.id).subscribe({
      next: () => {
        this.customers = this.customers.filter(customer => customer.id != dto.id);
        console.log("Customer successfully deleted");
      },
      error: err => {
        console.error("Error deleting customer:", err);
      }
    });
  }

  searchChanged(): void {
    this.searchChangedObservable.next();
  }

  toggleLoginStatus(customer: CustomerListDto) : void{
    if (this.service.loggedInCustomer === customer){
      this.service.loggedInCustomer = undefined;
    } else {
      this.service.loggedInCustomer = customer;
    }
  }

  isLoggedIn(customer: CustomerListDto) : boolean{
    return this.service.loggedInCustomer == customer;
  }

}
