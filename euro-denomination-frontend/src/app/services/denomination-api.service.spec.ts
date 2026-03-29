import {TestBed} from '@angular/core/testing';
import {provideHttpClient} from '@angular/common/http';
import {HttpTestingController, provideHttpClientTesting} from '@angular/common/http/testing';

import {DenominationApiService} from './denomination-api.service';

describe('DenominationApiService', () => {
  let service: DenominationApiService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });

    service = TestBed.inject(DenominationApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should send amount as GET query param with two decimals', () => {
    service.calculate(12.3).subscribe();

    const req = httpMock.expectOne((request) => request.url === '/api/denomination' && request.method === 'GET');
    expect(req.request.params.get('amount')).toBe('12.30');

    req.flush({10: 1, 2: 1, 0.2: 1, 0.1: 1});
  });

  it('should return backend response', () => {
    const response = {100: 1, 10: 1, 1: 1};
    let actual: Record<number, number> | undefined;

    service.calculate(111).subscribe((result) => {
      actual = result;
    });

    const req = httpMock.expectOne('/api/denomination?amount=111.00');
    expect(req.request.method).toBe('GET');
    req.flush(response);

    expect(actual).toEqual(response);
  });
});

