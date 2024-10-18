export interface Article {
  id?: number;
  designation: string;
  description: string;
  price: number;
}

export interface ArticleListDto {
  id: number;
  designation: string;
  description: string;
  price: number;
}

export interface ArticleSearch {
  designation?: string;
  description?: string;
  minPrice?: number;
  maxPrice?: number;
}
