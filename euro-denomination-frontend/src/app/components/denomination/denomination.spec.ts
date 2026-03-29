import {ComponentFixture, TestBed} from '@angular/core/testing';
import {of} from 'rxjs';

import {Denomination} from './denomination';
import {DenominationApiService} from '../../services/denomination-api.service';

describe('Denomination', () => {
  let component: Denomination;
  let fixture: ComponentFixture<Denomination>;
  let denominationApiServiceSpy: jasmine.SpyObj<DenominationApiService>;

  beforeEach(async () => {
    denominationApiServiceSpy = jasmine.createSpyObj<DenominationApiService>('DenominationApiService', ['calculate']);

    await TestBed.configureTestingModule({
      imports: [Denomination],
      providers: [
        {provide: DenominationApiService, useValue: denominationApiServiceSpy}
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(Denomination);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should calculate locally when toggle is active', () => {
    component.useLocalCalculation = true;
    component.form.patchValue({amount: 12.38});

    component.calculate();

    expect(denominationApiServiceSpy.calculate).not.toHaveBeenCalled();
    expect(component.result).toEqual({
      10: 1,
      2: 1,
      0.2: 1,
      0.1: 1,
      0.05: 1,
      0.02: 1,
      0.01: 1
    });
  });

  it('should call backend when toggle is inactive', () => {
    const backendResult = {100: 1, 10: 1, 1: 1};
    denominationApiServiceSpy.calculate.and.returnValue(of(backendResult));
    component.useLocalCalculation = false;
    component.form.patchValue({amount: 111});

    component.calculate();

    expect(denominationApiServiceSpy.calculate).toHaveBeenCalledWith(111);
    expect(component.result).toEqual(backendResult);
  });

  it('should build diff result starting with the second calculation', () => {
    component.useLocalCalculation = true;

    component.form.patchValue({amount: 10});
    component.calculate();

    expect(component.hasMultipleCalculations).toBeFalse();
    expect(component.diffResult).toBeNull();

    component.form.patchValue({amount: 12});
    component.calculate();

    expect(component.hasMultipleCalculations).toBeTrue();
    expect(component.previousResult).toEqual({10: 1});
    expect(component.diffResult).toEqual({10: 0, 2: 1});
  });

  it('should show difference card only after second calculation', () => {
    component.useLocalCalculation = true;

    component.form.patchValue({amount: 10});
    component.calculate();
    fixture.detectChanges();

    expect(fixture.nativeElement.textContent).not.toContain('Differenz');

    component.form.patchValue({amount: 12});
    component.calculate();
    fixture.detectChanges();

    expect(fixture.nativeElement.textContent).toContain('Differenz');
  });
});
