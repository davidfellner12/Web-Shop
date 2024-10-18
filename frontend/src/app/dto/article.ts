export interface Article {
  id?: number;
  designation: string;
  description: string;
  price: number;
  image?: string;
}

export interface ArticleListDto {
  id: number;
  designation: string;
  description: string;
  price: number;
  image?: File;
}

export interface ArticleSearch {
  designation?: string;
  description?: string;
  minPrice?: number;
  maxPrice?: number;
}
