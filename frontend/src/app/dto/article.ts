export interface Article {
  id?: number;
  designation: string;
  description: string;
  price: number;
  image?: string;
  imageType?: string;
}

export interface ArticleListDto {
  id?: number;
  designation: string;
  description: string;
  price: number;
  image?: string;
  imageType?: string;
}

export interface ArticleSearch {
  designation?: string;
  description?: string;
  minPrice?: number;
  maxPrice?: number;
}
