import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { VoiceRecognitionService } from './services/voice-recognition.service';
import { Router } from '@angular/router';
import { TextToSpeechService } from './services/speech-to-text.service';
import { ChatGPTservice } from './services/chatgpr.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  isCollapsed = false;
  isSidenavOpen = true;
  title = 'eduOnlineSystems';
  @ViewChild('sidenav') sidenav: MatSidenav | undefined;

  constructor(public service:VoiceRecognitionService,public vservice:TextToSpeechService,
    private router: Router,public chatService:ChatGPTservice){
    

  }
  async ngOnInit() {
    // this.chatService.checkChatGPT().subscribe(
    //   (response: any) => {
    //     let jsonresponse=JSON.parse(response.choices[0].message.content);
    //     jsonresponse.forEach((element:any) => {
    //       console.log(element.question)
    //     });
    //     // Handle the API response as needed
    //   },
    //   (error) => {
    //    console.error('API call error:', error);
    //     // Handle error
    //   }
    // );;
  }
  
  toggleSidenav() {
  
    this.isCollapsed = !this.isCollapsed;
    if (this.isCollapsed) {
    //  this.sidenav?.close();
      this.isSidenavOpen = false; // set to false to hide sidenav
    } else {
    //  this.sidenav?.open();
      this.isSidenavOpen = true; // set to true to show sidenav
    }
  }
  
}

declare global {
  interface Window {
    SpeechRecognition: any;
  }
}

