import {DatePipe, NgClass} from '@angular/common';
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

  markUnmarkAsRegistered(dto: CustomerListDto): void {
    if (!dto.isRegistered) {
      this.toggle = !this.toggle;
      this.registerCustomer(dto);
    } else {
      this.toggle = !this.toggle;
      this.unregisterCustomer(dto);
    }
    this.status = this.toggle ? 'Enable' : 'Disable';
  }

  private registerCustomer(dto: CustomerListDto): void {
    dto.isRegistered = true;
    console.log(`Registered customer: ${dto.firstName} ${dto.lastName}`);
  }


  private unregisterCustomer(dto: CustomerListDto): void {
    dto.isRegistered = false;
    console.log(`Unregistered customer: ${dto.firstName} ${dto.lastName}`);
  }

  searchChanged(): void {
    this.searchChangedObservable.next();
  }
}
