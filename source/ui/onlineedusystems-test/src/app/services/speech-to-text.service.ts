import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TextToSpeechService {

  speechSynth: SpeechSynthesis;

  constructor() {
    this.speechSynth = window.speechSynthesis;
  }

  async speak(text: string) {
    const utterance = new SpeechSynthesisUtterance(text);
    await this.speechSynth.speak(utterance);
    console.log('speak')
  }

}
