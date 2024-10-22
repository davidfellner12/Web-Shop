import { Injectable } from '@angular/core';
import { ArticleListDto } from "../dto/article";

@Injectable({
  providedIn: 'root'
})
export class CartService {

  constructor() {}

  /**
   * Store a value in session storage.
   * @param key The ArticleListDto under which to store the value.
   * @param value The quantity to store. It will be stringified.
   */
  setItem(key: ArticleListDto, value: number): void {
    const existingQuantity = this.getItem(key);
    if (existingQuantity) {
      // Increment the existing quantity
      this.storeItem(key.id, key, existingQuantity + value);
      console.log(`Incremented quantity for article ID ${key.id}`);
    } else {
      // Store a new article
      this.storeItem(key.id, key, value);
      console.log(`Set new article with ID ${key.id}`);
    }
  }

  /**
   * Store an item in session storage.
   * @param id The unique ID of the article.
   * @param article The ArticleListDto object to store.
   * @param quantity The quantity to store.
   */
  private storeItem(id: number, article: ArticleListDto, quantity: number): void {
    // Store both the article and its quantity as a JSON object
    const item = { article, quantity };
    sessionStorage.setItem(id.toString(), JSON.stringify(item));
  }

  /**
   * Retrieve a value from session storage.
   * @param key The ArticleListDto for which to retrieve the value.
   * @returns The quantity or null if the key does not exist.
   */
  getItem(key: ArticleListDto): number | null {
    const item = sessionStorage.getItem(key.id.toString());
    if (item) {
      const parsedItem = JSON.parse(item);
      return parsedItem.quantity;
    }
    return null;
  }

  /**
   * Remove an item from session storage.
   * @param key The ArticleListDto for which to remove the value.
   */
  removeItem(key: ArticleListDto): void {
    sessionStorage.removeItem(key.id.toString());
  }

  /**
   * Clear all items from session storage.
   */
  clear(): void {
    sessionStorage.clear();
  }

  /**
   * Returns the length of the session storage.
   */
  length(): number {
    return sessionStorage.length;
  }

  /**
   * Retrieve all items from session storage as a Map.
   * @returns A Map of ArticleListDto and their quantities.
   */
  getAllSessionStorageItems(): Map<ArticleListDto, number> {
    const result = new Map<ArticleListDto, number>();
    for (let i = 0; i < sessionStorage.length; i++) {
      const key = sessionStorage.key(i);
      if (key) {
        const valueString = sessionStorage.getItem(key);
        if (valueString) {
          // Parse the stored JSON item
          const parsedItem = JSON.parse(valueString);
          const articleObject: ArticleListDto = parsedItem.article;
          const quantity: number = parsedItem.quantity;
          result.set(articleObject, quantity);
        }
      }
    }
    return result;
  }
}
