import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { TextToSpeechService } from '../services/speech-to-text.service';
import { VoiceRecognitionService } from '../services/voice-recognition.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  @Input() data: any[] = [
    { name: 'C# Programming', value: 40 },
    { name: 'Java', value: 30 },
    { name: 'C++ Programming', value: 20 },
    { name: 'Data Structures', value: 10 }
  ];
  public customColors = ['#ccccff', '#ffccb3', '#ccffcc', '#FF9800']; // Add more colors as needed

  legendItems = [
    { label: 'C++ Programming', color: '#ccccff' },
    { label: 'Subject B', color: '#ffccb3' },
    { label: 'Subject C', color: '#ccffcc' },
    // Add more items as needed
  ];
  
  @Input() view: [number, number] = [500, 300];
  @Input() showLegend = true;
  @Input() legendPosition = 'right';
  @Input() explodeSlices = false;
  @Input() doughnut = false;
  @Input() arcWidth = 0.3;
  gradient = false;

  onSelect(event:any) {
    console.log(event);
  }
  
  private utterance: SpeechSynthesisUtterance = new SpeechSynthesisUtterance();
  private speechSynth: SpeechSynthesis;
  private isSpeaking = false;
  public progress=100;
  constructor(public vservice:VoiceRecognitionService,private router: Router) { 
    localStorage.setItem('audioSupport','yes');
      this.speechSynth = window.speechSynthesis;
    this.utterance.onstart = () => {
      this.isSpeaking = true;
      console.log('speech started');
    };
    this.utterance.onend = () => {
      this.isSpeaking = false;
      console.log('speech completed');
      this.vservice.start();
    };
      vservice.init();}

  async ngOnInit(): Promise<void> {
  this.speak('Welcome to Online learning Platform. and this is applicable for all learners including those with disablities Which module do you want to land from the below list. 1.Courses and 2.Exams, If you dont want the audio support, feel free to close the popup, start speaking')

    this.vservice.speechInput().subscribe((text:string) => {
        if(text.includes('courses'))
        {
          this.router.navigate(['/courses']);
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
