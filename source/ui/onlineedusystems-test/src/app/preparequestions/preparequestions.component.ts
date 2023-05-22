import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormArray, FormControl, FormGroup } from '@angular/forms';
import { ChatGPTservice } from '../services/chatgpr.service';

@Component({
  selector: 'app-preparequestions',
  templateUrl: './preparequestions.component.html',
  styleUrls: ['./preparequestions.component.css']
})
export class PreparequestionsComponent implements OnInit {
  itemsForm!: FormGroup;
  public items:string[]=[];
  public showpopup=false;
  constructor(public chatService:ChatGPTservice) { }

  ngOnInit(): void {
    this.itemsForm = new FormGroup({
      items: new FormArray([])
    });
  }

  get itemControls():AbstractControl[] {
    return (this.itemsForm.get('items') as FormArray).controls;
  }

  public GetQuestions(sub:string){
    this.chatService.checkChatGPT(sub).subscribe(
      (response: any) => {
        let jsonresponse=JSON.parse(response.choices[0].message.content);
        jsonresponse.forEach((element:any) => {
          console.log(element.question)
          this.items.push(element.question);
        });
        // Handle the API response as needed
        this.updateFormControls();
      },
      (error) => {
       console.error('API call error:', error);
        // Handle error
      }
    );
  }
  getFormControl(control: AbstractControl): FormControl {
    return control as FormControl;
  }
  updateFormControls() {
    // Clear the existing form controls
    (this.itemsForm.get('items') as FormArray).clear();
    
    // Create new form controls based on the updated items array
    const formControls = this.items.map(item => new FormControl(item));

    // Push the new form controls to the FormArray
    formControls.forEach(control => {
      (this.itemsForm.get('items') as FormArray).push(control);
    });
  }
  isDisabled(){
    if(this.items.length>0){
      return false;
    }
    return true;
  }

  onSubmit(){
    this.showpopup=true;
  }

}
