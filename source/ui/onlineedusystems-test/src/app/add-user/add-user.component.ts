import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.css']
})
export class AddUserComponent  {

  user: any = {
    name: '',
    role: '',
    status: ''
  };

  onSubmit(form: NgForm) {
    console.log(form);
  }
}
