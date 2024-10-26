export interface Order {
  id?: number;
  customerId: number;
  name: string;
  dateOfPurchase: string;
  totalPrice: number;
}

export interface OrderListDto {
  id: number;
  customerId: number;
  name: string;
  dateOfPurchase: string;
  totalPrice: number;
}

export class OrderSearch {
  name?: string;
  designation?: string;
  dateOfPurchaseEarliest?: string;
  dateOfPurchaseLatest?: string;
}
