import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Barco } from '../model/barco';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BarcoService {
  private baseUrl = environment.apiUrl;
  private http = inject(HttpClient);

  findAll(): Observable<Barco[]> {
    return this.http.get<Barco[]>(`${this.baseUrl}/barco/list`);
  }

  getAllBarcos(): Observable<Barco[]> {
    return this.findAll();
  }

  findById(id: number): Observable<Barco> {
    return this.http.get<Barco>(`${this.baseUrl}/barco/${id}`);
  }

  createBarco(barco: Barco): Observable<Barco> {
    return this.http.post<Barco>(
      `${this.baseUrl}/barco`,
      barco,
      {
        headers: new HttpHeaders({
          'Content-Type': 'application/json'
        })
      }
    );
  }

  updateBarco(barco: Barco): Observable<Barco> {
    return this.http.put<Barco>(
      `${this.baseUrl}/barco`,
      barco,
      {
        headers: new HttpHeaders({
          'Content-Type': 'application/json'
        })
      }
    );
  }

  deleteBarco(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/barco/${id}`);
  }
}
