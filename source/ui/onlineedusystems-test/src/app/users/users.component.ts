import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {

  usersData = [
    {sNo:1,username: 'User1', status: 'Active', userRole: 'Admin'},
    {sNo:2,username: 'User2', status: 'Inactive', userRole: 'User'},
    {sNo:3,username: 'User3', status: 'Active', userRole: 'User'}
  ];

  displayedColumns: string[] = ['sNo','username', 'status', 'userRole','edit', 'delete'];
  constructor() { }

  ngOnInit(): void {
  }

}
