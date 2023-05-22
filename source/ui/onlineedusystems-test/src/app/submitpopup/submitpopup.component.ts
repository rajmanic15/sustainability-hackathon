import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-submitpopup',
  template: `
    <div class="popup-container" [class.show]="show">
      <div class="popup">
        <div class="popup-header">
          <h2>{{ title }}</h2>
          <button (click)="close()">X</button>
        </div>
        <div class="popup-content">
          <ng-content></ng-content>
        </div>
      </div>
    </div>
  `,
  styles: [
    `
      .popup-container {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, 0.5);
        display: none;
        justify-content: center;
        align-items: center;
        z-index: 1000;
      }
      .popup-container.show {
        display: flex;
      }
      .popup {
        background-color: white;
        width: 500px;
        padding: 20px;
        border-radius: 10px;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
      }
      .popup-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
      }
      .popup-header h2 {
        margin: 0;
      }
      .popup-header button {
        border: none;
        background-color: transparent;
        font-size: 20px;
        cursor: pointer;
        
      }
      .popup-content {
        margin-top: 20px;
        align-items:left;
        margin-left: 50px;
      }
    `,
  ],
})
export class SubmitpopupComponent {

  @Input() title: string = '';
  @Input() show: boolean = false;
  constructor(){

  }


  close() {
    this.show = false;
    localStorage.setItem('audioSupport','no');
  }
}
