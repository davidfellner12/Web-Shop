import {Component, OnInit} from '@angular/core';
import {FormsModule, NgForm} from "@angular/forms";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {Article} from "../../../dto/article";
import {ErrorFormatterService} from "../../../service/error-formatter.service";
import {Location} from "@angular/common";
import {ToastrService} from "ngx-toastr";
import {ArticleService} from "../../../service/article.service";
import {Observable} from "rxjs";

export enum ArticleCreateEditMode{
  edit,
  create
}

@Component({
  selector: 'app-article-create-edit',
  standalone: true,
  imports: [
    FormsModule,
    RouterLink
  ],
  templateUrl: './article-create-edit.component.html',
  styleUrl: './article-create-edit.component.scss'
})

export class ArticleCreateEditComponent implements OnInit{
  mode: ArticleCreateEditMode = ArticleCreateEditMode.create;
  article: Article = {
    name: '',
    description: '',
    price: 0
  };

  constructor(private service: ArticleService,
              private errorFormatter: ErrorFormatterService,
              private router: Router,
              private route: ActivatedRoute,
              private location: Location,
              private notification: ToastrService) {
  }


  public get header(): string {
    switch (this.mode) {
      case ArticleCreateEditMode.create:
        return 'Create New Customer';
      case ArticleCreateEditMode.edit:
        return "Edit The Customer"
      default:
        return '?';
    }
  }

  public get submitButtonText(): string {
    switch (this.mode) {
      case ArticleCreateEditMode.create:
        return 'Create';
      case ArticleCreateEditMode.edit:
        return 'Edit';
      default:
        return '?';
    }
  }

  ngOnInit(): void {
    this.route.data.subscribe(data => {
      this.mode = data['mode'];
      const id = this.route.snapshot.paramMap.get('id');
      console.log('Customer ID:', id);
      if (id){
        this.mode = ArticleCreateEditMode.edit;
        console.log(+id);
        this.loadArticle(+id);
      } else {
        this.mode = ArticleCreateEditMode.create;
      }
    });
  }

  private loadArticle(articleId: number): void{
    console.log("The id should be right")
    console.log("Here is the number" + articleId)
    this.service.getById(articleId).subscribe({
      next: (article) => {
        this.article = article;
      },
      error: (err) => {
        console.error("Error loading customer", err);
        this.notification.error("Failed to load customer details");
      }
    });
  }

  public onSubmit(form: NgForm): void {
    if (form.valid) {
      let observable: Observable<Article>;
      switch (this.mode) {
        case ArticleCreateEditMode.create:
          observable = this.service.create(this.article);
          break;
        case ArticleCreateEditMode.edit:
          observable = this.service.update(this.article);
          break;
        default:
          console.error('Unknown CustomerCreateEditMode', this.mode);
          return;
      }
      observable.subscribe({
        next: () => {
          this.notification.success(`Customer ${this.article.name}  successfully saved!`);
          this.location.back();
        },
        error: error => {
          console.error('Error creating customer', error);
          this.notification.error(this.errorFormatter.format(error), 'Could not save customer');
        }
      });
    }
  }



}
