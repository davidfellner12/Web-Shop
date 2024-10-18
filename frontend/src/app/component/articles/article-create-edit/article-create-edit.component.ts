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
    designation: '',
    description: '',
    price: 0,
    image: ''
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
        return 'Create New Article';
      case ArticleCreateEditMode.edit:
        return "Edit The Article"
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
        console.error("Error loading article", err);
        this.notification.error("Failed to load article details");
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
          console.error('Unknown ArticleCreateEditMode', this.mode);
          return;
      }
      observable.subscribe({
        next: () => {
          this.notification.success(`Article ${this.article.designation}  successfully created!`);
          this.location.back();
        },
        error: error => {
          console.error('Error creating article', error);
          this.notification.error(this.errorFormatter.format(error), 'Could not save article');
        }
      });
    }
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      const reader = new FileReader();
      reader.onload = () => {
        this.article.image= reader.result as string;
        console.log('Base64 encoded file:', this.article.image);
      };
      reader.readAsDataURL(file);
    }
  }

  /*
  delete(): void {
    if (!this.article.id){
      console.error("No customer ID provided for deletion.");
      return;
    }
    this.service.delete(this.article.id).subscribe({
      next: () => {
        this.notification.success(`Customer ${this.article.description} successfully deleted!`);
      },
      error: (error) => {
        console.error("Error deleting customer", error);
        this.notification.error(this.errorFormatter.format(error), "Could not delete customer");
      }
    })
  }
*/


}
