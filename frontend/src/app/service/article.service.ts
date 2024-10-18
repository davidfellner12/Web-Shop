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
   * Find an existing article by its id
   *
   * @param id
   */
  getById(id: number): Observable<Article> {
    console.log("Here is some information to observables")
    console.log(`${baseUri}/${id}`);
    return this.http.get<Article>(`${baseUri}/${id}`);
  }

  /**
   * Find all articles by the given filter criteria
   *
   * @param filter
   */
  getAllByFilter(filter: ArticleSearch): Observable<ArticleListDto[]> {
    let params = new HttpParams();
    if (filter.designation?.trim()) {
      params = params.append('name', filter.designation as string);
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

  /**
   * Create a new article in the system.
   *
   * @param article the data for the article that should be created
   * @return an Observable for the created article
   */
  create(article: Article): Observable<Article> {

    return this.http.post<Article>(baseUri, article);
  }

  /**
   * Updates a specific article in the system
   *
   * @param article the data for the article that should be updated
   * @return an Observable for the updating of an article
   */

  update(article: Article): Observable<Article> {
    return this.http.patch<Article>(baseUri, article);
  }
}
