import { Injectable } from '@angular/core';
import { ArticleListDto } from "../dto/article";
import { Article } from "../dto/article";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class CartService {
  cartSaving: Map<ArticleListDto, number> = new Map<ArticleListDto, number>();


  constructor(private http: HttpClient) {
  }

  /**
   * Store a value in session storage.
   * @param key The ArticleListDto under which to store the value.
   * @param value The quantity to store. It will be stringified.
   */
  setItem(key: ArticleListDto, value: number): void {
    if (key.id === undefined) {
      console.error('Cannot store item with undefined ID');
      return;
    }

    const existingQuantity = this.getItem(key);
    if (existingQuantity !== null) {
      // Increment the existing quantity
      this.storeItem(key.id, key, existingQuantity + value);
      console.log(`Incremented quantity for article ID ${key.id}`);
    } else {
      // Store a new article
      this.storeItem(key.id, key, value);
      console.log(`Set new article with ID ${key.id}`);
    }
  }

  setItemWithArticleEntity(key: Article, value: number): void {
    if (key.id === undefined) {
      console.error('Cannot store item with undefined ID');
      return;
    }

    const correctKey: ArticleListDto = {
      id: key.id,
      designation: key.designation,
      description: key.description,
      price: key.price,
      image: key.image,
      imageType: key.imageType
    };

    const existingQuantity = this.getItem(correctKey);

    // Increment the existing quantity
    if (correctKey.id != undefined) {
      if (existingQuantity !== null) {
        this.storeItem(correctKey.id, correctKey, existingQuantity + value);
        console.log(`Incremented quantity for article ID ${key.id}`);
      } else {
        // Store a new article
        this.storeItem(correctKey.id, correctKey, value);
        console.log(`Set new article with ID ${key.id}`);
      }
    }
  }

  /**
   * Store an item in session storage.
   * @param id The unique ID of the article.
   * @param article The ArticleListDto object to store.
   * @param quantity The quantity to store.
   */
  private storeItem(id: number, article: ArticleListDto, quantity: number): void {
    if (id === undefined || article === undefined || quantity === undefined) {
      console.error('Cannot store item with undefined values');
      return;
    }

    // Store both the article and its quantity as a JSON object
    const item = {article, quantity};
    sessionStorage.setItem(id.toString(), JSON.stringify(item));
  }

  /**
   * Retrieve a value from session storage.
   * @param key The ArticleListDto for which to retrieve the value.
   * @returns The quantity or null if the key does not exist.
   */
  getItem(key: ArticleListDto): number | null {
    if (key.id === undefined) {
      console.error('Cannot retrieve item with undefined ID');
      return null;
    }

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
    if (key.id === undefined) {
      console.error('Cannot remove item with undefined ID');
      return;
    }

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
          const parsedItem = JSON.parse(valueString);
          const articleObject: ArticleListDto = parsedItem.article;
          const quantity: number = parsedItem.quantity;

          if (articleObject && quantity !== undefined) {
            result.set(articleObject, quantity);
          }
        }
      }
    }
    return result;
  }
}
