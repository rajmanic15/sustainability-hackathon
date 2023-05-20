import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-attempt-test',
  templateUrl: './attempt-test.component.html',
  styleUrls: ['./attempt-test.component.css']
})
export class AttemptTestComponent implements OnInit {

  constructor() { }

  public questions=[{
    title:'Which of the following is not a data type in C#?',
    options:['int','bool','float','list']
  },{
    title:'What is C#?',
    options:['A programming language','An operating system','A text editor','A web browser']
  },{
    title:'Which of the following is a looping structure in C#?',
    options:['if-else statement','switch statement','for loop','try-catch statement']
  },{
    title:'What is the correct syntax for declaring a variable in C#?',
    options:['int x = 5;','x = 5 : int;','5 = x : int;','x : int = 5;']
  },{
    title:'Which of the following is not a control statement in C#?',
    options:[' if-else statement','switch statement','for loop','int statement']
  }
]
  ngOnInit(): void {
  }

}
