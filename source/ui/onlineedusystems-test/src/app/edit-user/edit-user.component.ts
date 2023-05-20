import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.css']
})
export class EditUserComponent {
  user: any = {
    name: '',
    role: '',
    status: ''
  };

  onSubmit(form: NgForm) {
    console.log(form);
  }
}
