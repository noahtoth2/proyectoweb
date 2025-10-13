import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Barco } from '../../model/barco';
import { BarcoService } from '../../shared/barco.service';


 
 @Component({ 
  selector: 'app-barco-list',
  imports: [],
templateUrl: './barco-list.component.html',
  styleUrl: './barco-list.component.css'
})
export class BarcoListComponent  {
  barcos=signal<Barco[]>([]);
  barcoService=inject(BarcoService);

  ngOnInit(): void {
    this.barcoService.findAll().subscribe(
      data=> this.barcos.set(data)
    
    );
}
}




