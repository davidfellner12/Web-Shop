import { CurrencyPipe } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { RouterLink } from "@angular/router";
import { Component } from '@angular/core';
import { ArticleService } from "../../service/article.service";
import { CartService } from "../../service/cart.service";
import { CommonModule } from '@angular/common';
import { ArticleListDto } from "../../dto/article";

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

  constructor(
    private cartService: CartService,
    private articleService: ArticleService
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
    // Clear session storage
    this.cartService.clear();
  }

  loadCart(): void {
    this.cart = this.cartService.getAllSessionStorageItems();
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

  }

  protected readonly Object = Object;
}
