import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { debounceTime, Subject } from 'rxjs';
import { CustomerListDto, CustomerSearch } from 'src/app/dto/customer';
import { CustomerService } from 'src/app/service/customer.service';
import { ErrorFormatterService } from 'src/app/service/error-formatter.service';

@Component({
  selector: 'app-customers',
  standalone: true,
  imports: [
    RouterModule,
    DatePipe,
    FormsModule
  ],
  templateUrl: './customers.component.html',
  styleUrl: './customers.component.scss'
})
export class CustomersComponent implements OnInit {
  customers: CustomerListDto[] = [];
  searchParams: CustomerSearch = {};
  searchChangedObservable = new Subject<void>();
  bannerError = false; /* For the setup sanity check banner. Should be removed with banner. */

  selectedCustomer: CustomerListDto | undefined;


  constructor(private service: CustomerService,
              private errorFormatter: ErrorFormatterService,
              private router: Router,
              private notification: ToastrService) {
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


  searchChanged(): void {
    this.searchChangedObservable.next();
  }

}
