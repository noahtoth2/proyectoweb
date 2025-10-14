import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Barco } from '../../model/barco';
import { BarcoService } from '../../shared/barco.service';

@Component({ 
  selector: 'app-barco-list',
  imports: [CommonModule],
  templateUrl: './barco-list.component.html',
  styleUrl: './barco-list.component.css'
})
export class BarcoListComponent implements OnInit {
  barcos = signal<Barco[]>([]);
  barcoService = inject(BarcoService);
  private router = inject(Router);

  ngOnInit(): void {
    this.barcoService.findAll().subscribe(
      
      data=> this.barcos.set(data)
    
    
    );
  }

  editarBarco(id: number): void {
    this.router.navigate(['/barco', id, 'edit']);
  }

  verDetalle(id: number): void {
    this.router.navigate(['/barco', id]);
  }
}
