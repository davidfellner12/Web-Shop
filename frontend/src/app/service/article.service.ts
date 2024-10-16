import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Article, ArticleListDto, ArticleSearch } from 'src/app/dto/article';
import { environment } from 'src/app/environments/environment';

const baseUri = environment.backendUrl + '/articles';

@Injectable({
  providedIn: 'root'
})
export class ArticleService {

  constructor(private http: HttpClient) {
  }

  /**
   * Find all articles by the given filter criteria
   *
   * @param filter
   */
  getAllByFilter(filter: ArticleSearch): Observable<ArticleListDto[]> {
    let params = new HttpParams();
    if (filter.name?.trim()) {
      params = params.append('name', filter.name as string);
    }
    if (filter.description?.trim()) {
      params = params.append('description', filter.description as string);
    }
    if (filter.minPrice != null) {
      params = params.append('minPrice', filter.minPrice as number);
    }
    if (filter.maxPrice != null) {
      params = params.append('maxPrice', filter.maxPrice as number);
    }
    return this.http.get<ArticleListDto[]>(baseUri, { params });
  }
}
