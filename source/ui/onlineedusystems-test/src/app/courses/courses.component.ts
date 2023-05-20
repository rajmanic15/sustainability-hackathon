import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { PopupComponent } from '../popup/popup.component';
import { TextToSpeechService } from '../services/speech-to-text.service';
import { VoiceRecognitionService } from '../services/voice-recognition.service';

@Component({
  selector: 'app-courses',
  templateUrl: './courses.component.html',
  styleUrls: ['./courses.component.css']
})
export class CoursesComponent implements OnInit {

  usersData = [
    {sNo:1,coursename: 'C Programming', coursedescription: 'Basics and Fundamentals', coursestartdate: '05-05-2023',courseenddate:'-',isactive:'active'},
    {sNo:2,coursename: 'C++ Programming', coursedescription: 'Basics and Fundamentals', coursestartdate: '-',courseenddate:'-',isactive:'active'},
    {sNo:3,coursename: 'Java', coursedescription: 'Basics and Fundamentals', coursestartdate: '-',courseenddate:'-',isactive:'active'}
  ];

  displayedColumns: string[] = ['sNo','coursename', 'coursedescription', 'coursestartdate','courseenddate', 'isactive','edit','delete','launch'];
  
  private utterance: SpeechSynthesisUtterance = new SpeechSynthesisUtterance();
  private speechSynth: SpeechSynthesis;
  private isSpeaking = false;
  public showpopup=false;
  constructor(public vservice:VoiceRecognitionService,private router: Router) { 
      this.speechSynth = window.speechSynthesis;
      this.vservice.stop();
     let status= localStorage.getItem('audioSupport');
     console.log(status)
     if(status!=='no')
     {
       this.showpopup=true
      this.utterance.onstart = () => {
        this.isSpeaking = true;
        console.log('speech started');
      };
      this.utterance.onend = () => {
        this.isSpeaking = false;
        console.log('speech completed');
        this.vservice.start();
      };
     }
      vservice.init();
    }


  async ngOnInit(): Promise<void> {
    let status= localStorage.getItem('audioSupport');
    if(status!=='no')
     {
   this.speak('You have Successfully Landed the Courses Module and Which course you would like to start in the below lists. 1.c programming 2.C++ programming and 3.Java');
     }
   this.vservice.speechInput().subscribe((text:string) => {
    if(text.includes('C program'))
    {
      this.router.navigate(['/learner']);
      this.vservice.stop();
    }
    console.log('Speech recognized: ', text);
  });
  }

  speak(text: string): void {
    if (!this.isSpeaking) {
      this.utterance.text = text;
      this.speechSynth.speak(this.utterance);
    }
  }


}
