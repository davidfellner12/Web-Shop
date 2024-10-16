import {Routes} from '@angular/router';
import {ArticlesComponent} from 'src/app/component/articles/articles.component';
import {
  CustomerCreateEditComponent,
  CustomerCreateEditMode
} from 'src/app/component/customers/customer-create-edit/customer-create-edit.component';
import {CustomersComponent} from './component/customers/customers.component';
import {
  ArticleCreateEditComponent,
  ArticleCreateEditMode
} from "./component/articles/article-create-edit/article-create-edit.component";

export const routes: Routes = [
  {
    path: 'articles', children: [
      { path: '', component: ArticlesComponent },
    ]
  },
  {
    path: 'customers', children: [
      { path: '', component: CustomersComponent },
      { path: 'create', component: CustomerCreateEditComponent, data: { mode: CustomerCreateEditMode.create } },
      { path: 'edit/:id', component: CustomerCreateEditComponent, data: {mode: CustomerCreateEditMode.edit}},
      { path: 'create', component: ArticleCreateEditComponent, data: {mode: ArticleCreateEditMode.create}},
      { path: 'edit/:id', component: ArticleCreateEditComponent, data: {mode: ArticleCreateEditMode.edit}},

    ]
  },
  // Redirect any non-existing path to 'customers'
  { path: '**', redirectTo: 'customers' }
];
