import { Component, ElementRef, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-learner-content',
  templateUrl: './learner-content.component.html',
  styleUrls: ['./learner-content.component.css']
})
export class LearnerContentComponent implements OnInit {
  private utterance: SpeechSynthesisUtterance = new SpeechSynthesisUtterance();
  private speechSynth: SpeechSynthesis;
  private isSpeaking = false;
  player: YT.Player | undefined;
  public id1: string = 'IxBMVztdlr4';
  public showpopup=false;
  savePlayer(player1:any) {
    this.player = player1;
    console.log('player instance', player1);
  }
  onStateChange(event:any) {
    console.log('player state', event.data);
  }
  constructor(private elementRef: ElementRef) {
    this.speechSynth = window.speechSynthesis;
    let status= localStorage.getItem('audioSupport');
     console.log(status)
     if(status!=='no')
     {
       this.showpopup=true;
     }
  }
  ngOnInit(): void {
    if(this.content!==undefined)
    {
      this.speak(this.content);
    }

    this.currentIndex = this.subtitles.indexOf(this.subtitle || '');
  }
  @Input() id: string | undefined;
  @Input() title: string | undefined;
  @Input() subtitle: string | undefined;
  @Input() content: string | undefined;
  @Input() imageUrl: string | undefined;
  @Input() items:string[]|undefined;
  @Input() subtitles: string[] =[];

  currentIndex = 0;

  scrollToNext() {
    this.currentIndex++;
    const elements = document.getElementsByClassName(
      'content-section'
    ) as HTMLCollectionOf<HTMLElement>;
    if (elements.length > 0) {
      const element = elements[this.currentIndex];
      if (element) {
        element.scrollIntoView();
      } else {
        this.currentIndex = 0;
      }
    }
  }


  onPlayerReady(event: any) {
    event.target.playVideo(); // Auto-play the video
  }

  onPlayerStateChange(event: any) {
    // Handle player state change events (e.g., play, pause, stop)
  }

  speak(text: string): void {
    if (!this.isSpeaking) {
      this.utterance.text = text;
      this.speechSynth.speak(this.utterance);
    }
  }
}
   

