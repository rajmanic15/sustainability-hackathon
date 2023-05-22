import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { delay, mergeMap, retryWhen, take } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ChatGPTservice {


  constructor(private readonly httpClient:HttpClient) {

  }
   checkChatGPT(sub:string) {
       let jsonresponse:any;
    const API_Key = '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'; // Replace with your OpenAI API key
    const headers = new HttpHeaders({
      Authorization: `Bearer ${API_Key}`,
       'Content-Type': 'application/json'
     });
    const body = {
       model: 'gpt-3.5-turbo',
       messages: [
         {
           role: 'user',
           content: `Please give some sample 10 ${sub} questions in json format. questions:[{question:""}]`
         }
       ]
     };

     return this.httpClient.post('https://api.openai.com/v1/chat/completions', body, { headers });
   }


}
