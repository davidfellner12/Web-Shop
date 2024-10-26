import { CurrencyPipe } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { RouterLink } from "@angular/router";
import { Component } from '@angular/core';
import { ArticleService } from "../../service/article.service";
import { CartService } from "../../service/cart.service";
import { CommonModule } from '@angular/common';
import { ArticleListDto } from "../../dto/article";
import {ToastrService} from "ngx-toastr";
import {CustomerService} from "../../service/customer.service";
import {Order} from "../../dto/order";
import {OrderService} from "../../service/order.service"
//This is the interface for the customerCart for saving in orders
interface CustomerCart{
  customerId: number,
  cart: Map<ArticleListDto, number>;
}

export interface OrderArticle {
  orderId: number,
  articleId: number,
  quantity: number,
  totalPrice: number
}

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [
    CurrencyPipe,
    FormsModule,
    RouterLink,
    CommonModule
  ],
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})

export class CartComponent {
  cart: Map<ArticleListDto, number> = new Map<ArticleListDto, number>();
  customerCarts: CustomerCart[] = [];
  order: Order = {
    name: '',
    customerId: 0,
    dateOfPurchase: '',
    totalPrice: 0
  };

  constructor(
    private cartService: CartService,
    private customerService: CustomerService,
    private orderService: OrderService,
    private articleService: ArticleService,
    private notification: ToastrService
  ) {
    this.loadCart();
  }

  removeFromCart(article: ArticleListDto): void {
    this.cart.delete(article);
    // Update session storage as well when an item is removed
    this.cartService.removeItem(article);
  }

  clearCart(): void {
    this.cart.clear();
    this.cartService.clear();
  }

  loadCart(): void {
    this.cart = this.cartService.getAllSessionStorageItems();
  }

  getCurrentDate() {
    const currentDate = new Date();
    const formattedDate = `${currentDate.getFullYear()}-${String(currentDate.getMonth() + 1).padStart(2, '0')}-${String(currentDate.getDate()).padStart(2, '0')}`;
    const formattedTime = `${String(currentDate.getHours()).padStart(2, '0')}:${String(currentDate.getMinutes()).padStart(2, '0')}:${String(currentDate.getSeconds()).padStart(2, '0')}`;
    return `${formattedDate} ${formattedTime}`;
  }

  getCartContents(): { article: ArticleListDto, quantity: number }[] {
    return Array.from(this.cart, ([article, quantity]) => ({ article, quantity }));
  }

  updateQuantity(article: ArticleListDto, event: Event): void {
    const inputElement = event.target as HTMLInputElement;
    const newQuantity = parseInt(inputElement.value, 10);
    if (!isNaN(newQuantity) && newQuantity > 0) {
      this.cart.set(article, newQuantity);
      this.cartService.setItem(article, newQuantity);
    }
  }

  getOverallPrice(price:number, quantity: number): number{
      return price*quantity;
  }

  calculateTotal(): number {
    let total = 0;
    this.getCartContents().forEach(item => {
      total += this.getOverallPrice(item.article.price, item.quantity);
    });
    return total;
  }

  purchaseCart(): void{
    if (this.customerService.loggedInCustomer == undefined){
      this.notification.error("No customer is logged in for purchase")
    } else {
      this.cartService.cartSaving = new Map<ArticleListDto, number>(this.cart);
      const customerId: number = this.customerService.loggedInCustomer.id;

      const customerCartToSafe: CustomerCart = {
        customerId,
        cart: this.cartService.cartSaving,
      };
      this.customerCarts.push(customerCartToSafe);
      this.clearCart();
      this.notification.success('Basket was successfully bought!');
    }
  }
  protected readonly Object = Object;
}
