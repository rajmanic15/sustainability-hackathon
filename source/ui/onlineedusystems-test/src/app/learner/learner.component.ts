import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-learner',
  template: `
    <mat-paginator [pageSize]="1" [pageSizeOptions]="[1, 2, 3, 4]"></mat-paginator>

    <app-learner-content *ngFor="let content of pagedContent" [id]="content.id" [title]="content.title" [subtitle]="content.subtitle" [content]="content.content" [imageUrl]="content.imageUrl" [items]="content.items" [subtitles]="subtitles"></app-learner-content>
   
  `
})
export class LearnerComponent implements OnInit {
  content = [
    { id:'1',title: 'C# Programming', subtitle: 'Variables', 
    content: 'A variable is a name of memory location. It is used to store data. Its value can be changed and it can be reused many times. It is a way to represent memory location through symbol so that it can be easily identified.',
     imageUrl: 'assets/datatypes.png',
    items:['Decimal types','Boolean types','Integral types','Floating point types','Nullable types'] },

    { id:'2',title: 'C# Programming', subtitle: 'Loops', 
    content: 'Looping in a programming language is a way to execute a statement or a set of statements multiple times depending on the result of the condition to be evaluated to execute statements. The result condition should be true to execute statements within loops',
     imageUrl: 'assets/php-while-loop.jpg',
    items:['For Loop','While Loop','Do While Loop','ForEach Loop'] },
    { id:'1',title: 'C# Programming', subtitle: 'Variables', 
    content: 'A variable is a name of memory location. It is used to store data. Its value can be changed and it can be reused many times. It is a way to represent memory location through symbol so that it can be easily identified.',
     imageUrl: 'https://example.com/image1.jpg',
    items:['Decimal types','Boolean types','Integral types','Floating point types','Nullable types'] },
    // ...
  ];
  pageIndex = 0;
  pageSize = 10;
  constructor() { 
   
  }
  ngOnInit(): void {
  }

  get pagedContent() {
    const startIndex = this.pageIndex * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    return this.content.slice(startIndex, endIndex);
  }

  get subtitles(){
    return this.content.map(x=>x.subtitle);
  }

  nextPage() {
    this.pageIndex++;
  }

  prevPage() {
    this.pageIndex--;
  }

  firstPage() {
    this.pageIndex = 0;
  }

  lastPage() {
    const pageCount = Math.ceil(this.content.length / this.pageSize);
    this.pageIndex = pageCount - 1;
  }


  

  
}
