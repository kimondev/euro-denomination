import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DenominationApiService {
  private readonly http = inject(HttpClient);
  private readonly endpoint = '/api/denomination';

  calculate(amount: number): Observable<Record<number, number>> {
    return this.http.get<Record<number, number>>(this.endpoint, {
      params: { amount: amount.toFixed(2) }
    });
  }
}

