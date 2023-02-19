import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  isShowFilterPet!: boolean;

  //constructor() { }

  ngOnInit(): void {
    this.isShowFilterPet = false;
  }

}
