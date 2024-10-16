import { DatePipe, Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs';
import {
  ConfirmationDialogMode,
  ConfirmDialogComponent
} from 'src/app/component/confirm-dialog/confirm-dialog.component';
import { Customer } from 'src/app/dto/customer';
import { CustomerService } from 'src/app/service/customer.service';
import { ErrorFormatterService } from 'src/app/service/error-formatter.service';
import { formatIsoDate } from 'src/app/util/date-helper';

export enum CustomerCreateEditMode {
  create,
  edit
}

@Component({
  selector: 'app-customer-create-edit',
  standalone: true,
  imports: [
    FormsModule,
    RouterModule,
    DatePipe,
    ConfirmDialogComponent
  ],
  templateUrl: './customer-create-edit.component.html',
  styleUrl: './customer-create-edit.component.scss'
})
export class CustomerCreateEditComponent implements OnInit {
  ConfirmationDialogMode = ConfirmationDialogMode;

  mode: CustomerCreateEditMode = CustomerCreateEditMode.create;
  customer: Customer = {
    firstName: '',
    lastName: '',
    email: '',
    dateOfBirth: new Date()
  };
  showConfirmDeletionDialog = false;
  deleteMessage = '?';
  dateOfBirthSet = false;


  constructor(private service: CustomerService,
              private errorFormatter: ErrorFormatterService,
              private router: Router,
              private route: ActivatedRoute,
              private location: Location,
              private notification: ToastrService) {
  }

  get dateOfBirth(): string | null {
    return this.dateOfBirthSet
      ? formatIsoDate(this.customer.dateOfBirth)
      : null;
  }

  set dateOfBirth(value: string | null) {
    if (!value) {
      this.dateOfBirthSet = false;
    } else {
      this.dateOfBirthSet = true;
      this.customer.dateOfBirth = new Date(value);
    }
  }

  public get header(): string {
    switch (this.mode) {
      case CustomerCreateEditMode.create:
        return 'Create New Customer';
      case CustomerCreateEditMode.edit:
        return "Edit The Customer"
      default:
        return '?';
    }
  }

  public get submitButtonText(): string {
    switch (this.mode) {
      case CustomerCreateEditMode.create:
        return 'Create';
      case CustomerCreateEditMode.edit:
        return 'Edit';
      default:
        return '?';
    }
  }

  get modeIsCreate(): boolean {
    return this.mode === CustomerCreateEditMode.create;
  }

//TODO: has to be changed, ngOnInit lifecycle hook is only called once
  ngOnInit(): void {
    this.route.data.subscribe(data => {
      this.mode = data['mode'];
       const id = this.route.snapshot.paramMap.get('id');
       console.log('Customer ID:', id);
      if (id){
        this.mode = CustomerCreateEditMode.edit;
        console.log(+id);
        this.loadCustomer(+id);
      } else {
        this.mode = CustomerCreateEditMode.create;
      }
    });
  }

  private loadCustomer(customerId: number): void{
    console.log("The id should be right")
    console.log("Here is the number" + customerId)
     this.service.getById(customerId).subscribe({
       next: (customer) => {
         this.customer = customer;
       },
       error: (err) => {
         console.error("Error loading customer", err);
         this.notification.error("Failed to load customer details");
       }
     });
  }

  public onSubmit(form: NgForm): void {
    if (form.valid) {
      let observable: Observable<Customer>;
      switch (this.mode) {
        case CustomerCreateEditMode.create:
          observable = this.service.create(this.customer);
          break;
        case CustomerCreateEditMode.edit:
          observable = this.service.update(this.customer);
          break;
        default:
          console.error('Unknown CustomerCreateEditMode', this.mode);
          return;
      }
      observable.subscribe({
        next: () => {
          this.notification.success(`Customer ${this.customer.firstName} ${this.customer.lastName}  successfully saved!`);
          this.location.back();
        },
        error: error => {
          console.error('Error creating customer', error);
          this.notification.error(this.errorFormatter.format(error), 'Could not save customer');
        }
      });
    }
  }

  delete(): void {
    // TODO this must be implemented
  }

}
