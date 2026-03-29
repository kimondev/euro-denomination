import {Component, inject} from '@angular/core';
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {IftaLabel} from 'primeng/iftalabel';
import {InputText} from 'primeng/inputtext';
import {ButtonDirective, ButtonIcon, ButtonLabel} from 'primeng/button';
import {TableModule} from 'primeng/table';
import {Card} from 'primeng/card';
import {ToggleButton} from 'primeng/togglebutton';
import {FormsModule} from '@angular/forms';
import {DenominationApiService} from '../../services/denomination-api.service';

interface DenominationRow {
  denomination: number;
  count: number;
}

type DenominationResult = Record<number, number>;

@Component({
  selector: 'app-denomination',
  imports: [CommonModule, ReactiveFormsModule, FormsModule, IftaLabel, InputText, ButtonDirective, ButtonIcon, ButtonLabel, TableModule, Card, ToggleButton],
  templateUrl: './denomination.html',
  styleUrl: './denomination.scss',
})
export class Denomination {
  private readonly ALL_DENOMINATIONS = [200, 100, 50, 20, 10, 5, 2, 1, 0.50, 0.20, 0.10, 0.05, 0.02, 0.01];

  private readonly EURO_BANKNOTES = new Set(this.ALL_DENOMINATIONS.filter(d => d >= 5));

  private readonly EURO_COINS = new Set(this.ALL_DENOMINATIONS.filter(d => d < 5));

  private readonly fb = inject(FormBuilder);
  private readonly denominationApi = inject(DenominationApiService);

  /** Toggle für lokale Berechnung (true) oder Backend-Berechnung (false) */
  useLocalCalculation = true;

  form = this.fb.nonNullable.group({
    amount: [0, [Validators.required, Validators.min(0.01)]]
  });

  result: DenominationResult | null = null;
  previousResult: DenominationResult | null = null;
  diffResult: DenominationResult | null = null;

  /**
   * Gibt true zurück wenn mindestens zwei Berechnungen durchgeführt wurden
   */
  get hasMultipleCalculations(): boolean {
    return this.previousResult !== null;
  }

  calculate() {
    const amount = this.form.controls.amount.value;

    if (amount <= 0) {
      this.resetResults();
      return;
    }

    if (this.useLocalCalculation) {
      this.applyCalculation(this.calculateDenomination(amount));
    } else {
      this.calculateViaBackend(amount);
    }
  }

  /**
   * Speichert das aktuelle Ergebnis und berechnet die Differenzen zur vorherigen Berechnung
   * @param newResult Das neue Berechnungsergebnis
   */
  private applyCalculation(newResult: DenominationResult): void {
    const previous = this.result;
    this.previousResult = previous;
    this.result = newResult;
    this.diffResult = previous ? this.calculateDifference(newResult, previous) : null;
  }

  /**
   * Berechnet die Differenzen zwischen aktuellem und vorherigem Ergebnis
   */
  private calculateDifference(currentResult: DenominationResult, previousResult: DenominationResult): DenominationResult {
    const diff: DenominationResult = {};
    const allKeys = new Set([
      ...Object.keys(currentResult).map(Number),
      ...Object.keys(previousResult).map(Number)
    ]);

    for (const key of allKeys) {
      const current = currentResult[key] || 0;
      const previous = previousResult[key] || 0;
      diff[key] = current - previous;
    }

    return diff;
  }

  private resetResults(): void {
    this.result = null;
    this.previousResult = null;
    this.diffResult = null;
  }

  /**
   * Sendet den Betrag zum Backend zur Stückelungsberechnung
   * @param amount Der Betrag in Euro
   */
  private calculateViaBackend(amount: number): void {
    this.denominationApi.calculate(amount).subscribe({
      next: (newResult) => {
        this.applyCalculation(newResult);
      },
      error: (error) => {
        console.error('Backend-Fehler bei Stückelungsberechnung:', error);
        this.resetResults();
      }
    });
  }

  /**
   * @param amount Der Betrag in Euro
   * @returns Objekt mit Stückelung als Key und Anzahl als Value
   */
  private calculateDenomination(amount: number): DenominationResult {
    const result: DenominationResult = {};

    let remainingCents = Math.round(amount * 100);

    for (const denomination of this.ALL_DENOMINATIONS) {
      const denominationCents = Math.round(denomination * 100);

      const count = Math.floor(remainingCents / denominationCents);

      if (count > 0) {
        result[denomination] = count;
        remainingCents -= count * denominationCents;
      }
    }

    return result;
  }

  /**
   * Sortiert Stückelung: Münzen zuerst (aufsteigend), dann Geldscheine (aufsteigend)
   * @param data Array der Stückelung mit Anzahl
   * @returns Sortiertes Array
   */
  private sortDenominations(data: DenominationRow[]): DenominationRow[] {
    return [...data].sort((a, b) => this.compareDenominations(a.denomination, b.denomination));
  }

  private toRows(result: DenominationResult | null): DenominationRow[] {
    if (!result) return [];

    return this.sortDenominations(Object.entries(result).map(([key, value]) => ({
      denomination: Number(key),
      count: value
    })));
  }

  private sumAmount(result: DenominationResult | null): number {
    if (!result) return 0;

    return Object.entries(result).reduce((sum, [denom, count]) => sum + Number(denom) * count, 0);
  }

  /**
   * Vergleicht zwei Stückelungen für Sortierung
   * @returns -1 wenn a < b, 1 wenn a > b, 0 wenn gleich
   */
  private compareDenominations(aDenom: number, bDenom: number): number {
    const aIsCoin = this.isCoin(aDenom);
    const bIsCoin = this.isCoin(bDenom);

    if (aIsCoin && !bIsCoin) return -1;
    if (!aIsCoin && bIsCoin) return 1;

    return aDenom - bDenom;
  }

  /**
   * Gibt die sortierte Tabellendarstellung der Berechnungsergebnisse zurück
   */
  get tableData(): DenominationRow[] {
    return this.toRows(this.result);
  }

  /**
   * Gibt die sortierte Tabellendarstellung der Differenzen zurück
   */
  get diffTableData(): DenominationRow[] {
    return this.toRows(this.diffResult);
  }

  /**
   * Berechnet die Summe des aktuellen Ergebnisses
   */
  get currentAmount(): number {
    return this.sumAmount(this.result);
  }

  /**
   * Berechnet die Summe des vorherigen Ergebnisses
   */
  get previousAmount(): number {
    return this.sumAmount(this.previousResult);
  }

  /**
   * Berechnet die Differenzsumme direkt aus der Differenztabelle
   */
  get diffTableAmount(): number {
    return this.sumAmount(this.diffResult);
  }

  /**
   * Prüft, ob der Wert ein Euro-Geldschein ist
   * @param value Die zu prüfende Stückelung
   * @returns true wenn es ein Geldschein ist
   */
  isBanknote(value: number): boolean {
    return this.EURO_BANKNOTES.has(value);
  }

  /**
   * Prüft, ob der Wert eine Euro-Münze ist
   * @param value Die zu prüfende Stückelung
   * @returns true wenn es eine Münze ist
   */
  isCoin(value: number): boolean {
    return this.EURO_COINS.has(value);
  }

}
