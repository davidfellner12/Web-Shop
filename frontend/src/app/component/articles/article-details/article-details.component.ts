import {NgIf} from "@angular/common";
import {RouterLink} from "@angular/router";
import {Component} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {Article} from "../../../dto/article";
import {ErrorFormatterService} from "../../../service/error-formatter.service";
import {Location} from "@angular/common";
import {ToastrService} from "ngx-toastr";
import {ArticleService} from "../../../service/article.service";


@Component({
  selector: 'app-article-details',
  standalone: true,
  imports: [
    FormsModule,
    NgIf,
    RouterLink
  ],
  templateUrl: './article-details.component.html',
  styleUrl: './article-details.component.scss'
})
export class ArticleDetailsComponent {
  article: Article = {
    designation: '',
    description: '',
    price: 0,
    image: '',
    imageType: ''
  };

  constructor(private service: ArticleService,
              private errorFormatter: ErrorFormatterService,
              private router: Router,
              private route: ActivatedRoute,
              private location: Location,
              private notification: ToastrService) {
  }

  ngOnInit(): void {
    this.route.data.subscribe(data => {
      const id = this.route.snapshot.paramMap.get('id');
      console.log('Customer ID:', id);
      if (id){
        console.log(+id);
        this.loadArticle(+id);
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

  goBack() {
    this.router.navigate(['/articles']);
  }

  setImage(article: Article): string{
    //this.articleService.getById(article.id);
    console.log("Here is the 64base encoding from the database" + article.image );
    if (article.image != null){
      return  "data:image/" + article.imageType + ";base64," + article.image;
    }
    return '';
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
