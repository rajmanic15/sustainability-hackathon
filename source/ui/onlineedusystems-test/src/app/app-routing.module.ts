import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddUserComponent } from './add-user/add-user.component';
import { AttemptTestComponent } from './attempt-test/attempt-test.component';
import { CoursesComponent } from './courses/courses.component';
import { EditUserComponent } from './edit-user/edit-user.component';
import { HomeComponent } from './home/home.component';
import { LearnerComponent } from './learner/learner.component';
import { PreparequestionsComponent } from './preparequestions/preparequestions.component';
import { TestModuleComponent } from './test-module/test-module.component';
import { UsersComponent } from './users/users.component';

const routes: Routes = [
  {path:'',component:HomeComponent},
  { path: 'users', component: UsersComponent },
  {path:'edituser',component:EditUserComponent},
  {path:'adduser',component:AddUserComponent},
  {path:'courses',component:CoursesComponent},
  {path:'learner',component:LearnerComponent},
  {path:'exams',component:TestModuleComponent},
  {path:'attempttest',component:AttemptTestComponent},
  {path:'prepare',component:PreparequestionsComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
