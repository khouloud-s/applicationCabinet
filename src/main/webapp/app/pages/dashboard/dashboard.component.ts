import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  isShowFilterPet!: boolean;

 // constructor() { }

  ngOnInit(): void {
    this.isShowFilterPet = false;
  }

}
