import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {AuthGuardServiceService} from './auth-guard-service.service';
import {LoginComponent} from './login/login.component';
import {BookComponent} from './book/book.component';
import {FavoriteComponent} from './favorite/favorite.component';

const routes: Routes = [
  {path:"main",component:BookComponent,canActivate:[AuthGuardServiceService]},
  {path:"fav",component:FavoriteComponent,canActivate:[AuthGuardServiceService]},
  {path:"login",component:LoginComponent},
  {path:'',redirectTo:'main',pathMatch:'full'}

];

@NgModule({
  imports: [RouterModule.forRoot(routes,{useHash:true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
