
import {CurrencyPipe} from "@angular/common";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {RouterLink} from "@angular/router";
import {ArticleService} from "../../service/article.service";
import {ErrorFormatterService} from "../../service/error-formatter.service";
import {ToastrService} from "ngx-toastr";
import {CustomerService} from "../../service/customer.service";
import {CartService} from "../../service/cart.service";
import {debounceTime, Subject} from "rxjs";
import {OrderService} from "../../service/order.service";
import { Component, OnInit } from '@angular/core';
import {OrderListDto, OrderSearch} from 'src/app/dto/order';


@Component({
  selector: 'app-orders',
  standalone: true,
    imports: [
        CurrencyPipe,
        FormsModule,
        ReactiveFormsModule,
        RouterLink
    ],
  templateUrl: './orders.component.html',
  styleUrl: './orders.component.scss'
})
export class OrdersComponent implements OnInit{
  orders: OrderListDto[] = [];
  searchParams: OrderSearch = {};
  searchChangedObservable = new Subject<void>();
  bannerError = false;

  buttonClicked = false;

  constructor(private service: OrderService,
              private articleService: ArticleService,
              private customerService: CustomerService,
              private cartService: CartService,
              private errorFormatter: ErrorFormatterService,
              private notification: ToastrService) {
  }

  ngOnInit(): void {
    this.getOrders();
    this.searchChangedObservable
      .pipe(debounceTime(300))
      .subscribe({ next: () => this.getOrders() });
  }

  getOrders() {
    this.service.getAllByFilter(this.searchParams)
      .subscribe({
        next: orders => {
          this.orders = orders;
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

  handleButtonClick(): void {
    if (!this.buttonClicked) {
      this.buttonClicked = true;
      const savedCart = this.cartService.cartSaving;
      const contentDisplay: HTMLElement | null = document.getElementById("contentDisplay");

      /*
      let observable: Observable<Customer>;
      observable = this.service.create(this.orders);
      observable.subscribe({
        next: () => {

        },
        error: error => {
          console.error('Error creating article', error);
          this.notification.error(this.errorFormatter.format(error), 'Could not save article');
        }
      });*/


      if (contentDisplay) {
        contentDisplay.innerHTML = '';
        if (savedCart && savedCart.size > 0) {
          let htmlContent = '<h4>Cart Saving Details:</h4><ul>';
          savedCart.forEach((quantity, article) => {
            htmlContent += `<li>${article.designation}: Quantity = ${quantity}, Totalprice = $${article.price}</li>`;
          });
          htmlContent += '</ul>';
          contentDisplay.innerHTML = htmlContent;
        } else {
          contentDisplay.innerHTML = '<p>No items saved in the cart for ordering.</p>';
        }
        contentDisplay.style.display = "block";
      }
    } else {
      alert("Button has already been clicked.");
    }
  }
}
