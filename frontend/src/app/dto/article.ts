export interface Article {
  id?: number;
  name: string;
  description: string;
  price: number;
}

export interface ArticleListDto {
  id: number;
  name: string;
  description: string;
  price: number;
}

export interface ArticleSearch {
  name?: string;
  description?: string;
  minPrice?: number;
  maxPrice?: number;
}
