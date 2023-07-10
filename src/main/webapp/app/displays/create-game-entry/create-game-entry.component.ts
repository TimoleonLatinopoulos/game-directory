import { Component, Input, OnInit } from '@angular/core';
import { FormControl, Validators, FormGroup, FormBuilder } from '@angular/forms';

@Component({
  selector: 'jhi-create-game-entry',
  templateUrl: './create-game-entry.component.html',
  styleUrls: ['./create-game-entry.component.scss'],
})
export class CreateGameEntryComponent implements OnInit {
  gameDetailsForm: FormGroup;
  pegiRatingList: string[] = ['Three', 'Seven', 'Twelve', 'Sixteen', 'Eighteen'];

  constructor(private fb: FormBuilder) {
    this.gameDetailsForm = this.fb.group({
      id: [null],
      title: [''],
      gameDetails: this.fb.group({
        id: [null],
        releaseDate: [null],
        pegiRating: [null],
        metacriticScore: [null, [Validators.min(0), Validators.max(100)]],
        imageUrl: [''],
        thumbnailUrl: [''],
        price: [],
        description: [''],
        snippet: [''],
        notes: [''],
        steamApiid: [],
        platforms: [[]],
        developers: [[]],
        publishers: [[]],
        categories: [[]],
      }),
    });
  }

  ngOnInit(): void {
    this.fetchData();
  }

  public fetchData(): void {
    return;
  }

  public save(): void {
    return;
  }
}
