import {CurrencyPipe, NgIf} from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { debounceTime, Subject } from 'rxjs';
import { ArticleListDto, ArticleSearch } from 'src/app/dto/article';
import { ArticleService } from 'src/app/service/article.service';
import { ErrorFormatterService } from 'src/app/service/error-formatter.service';


@Component({
  selector: 'app-articles',
  standalone: true,
  imports: [
    RouterLink,
    FormsModule,
    CurrencyPipe,
    NgIf
  ],
  templateUrl: './articles.component.html',
  styleUrl: './articles.component.scss'
})
export class ArticlesComponent implements OnInit {
  articles: ArticleListDto[] = [];
  searchParams: ArticleSearch = {};
  searchChangedObservable = new Subject<void>();


  constructor(private articleService: ArticleService,
              private errorFormatter: ErrorFormatterService,
              private notification: ToastrService) {
  }

  ngOnInit(): void {
    this.getArticles();
    this.searchChangedObservable
      .pipe(debounceTime(300))
      .subscribe({ next: () => this.getArticles() });
  }

  getArticles() {
    this.articleService.getAllByFilter(this.searchParams)
      .subscribe({
        next: articles => {
          this.articles = articles;
        },
        error: error => {
          console.error('Error loading articles', error);
          this.notification.error(this.errorFormatter.format(error), 'Could not load articles');
        }
      });
  }

  searchChanged(): void {
    this.searchChangedObservable.next();
  }

  setImage(article: ArticleListDto): string{
    //this.articleService.getById(article.id);
    console.log("Here is the 64base encoding from the database" + article.image );
    if (article.image != null){
      return "data:image/" + article.imageType + ";base64," + article.image;
    }
    return '';
  }

}
