import { EntityResponseType, GameService } from './../../entities/game/service/game.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IGame } from 'app/entities/game/game.model';
import { getPegiNumber } from 'app/entities/game-details/game-details.model';
import { SeparateCategoriesPipe } from 'app/shared/pipes/separate-categories.pipe';

@Component({
  selector: 'jhi-game-preview',
  templateUrl: './game-preview.component.html',
  styleUrls: ['./game-preview.component.scss'],
})
export class GamePreviewComponent implements OnInit {
  public game: IGame;

  public hasPegiRating = false;
  public hasMetacritic = false;
  public hasNotes = false;
  public hasSteamAppid = false;

  public pegiRatingValue: number;
  public releaseDate: string;
  public result: IGame;

  public categories: string;
  public platforms: string;
  public developers: string;
  public publishers: string;

  constructor(
    private activatedRoute: ActivatedRoute,
    private gameService: GameService,
    private separateCategories: SeparateCategoriesPipe
  ) {}

  ngOnInit(): void {
    window.scrollTo(0, 0);
    this.activatedRoute.data.subscribe(object => {
      this.game = object.game as IGame;

      this.gameService.find(this.game.id).subscribe((response: EntityResponseType) => {
        if (response.body) {
          this.result = response.body;
        }
      });

      if (this.game.gameDetails != null) {
        if (this.game.gameDetails.releaseDate) {
          this.releaseDate = this.game.gameDetails.releaseDate.toString();
        }

        const pegiRating = this.game.gameDetails.pegiRating;
        if (pegiRating && pegiRating !== '' && pegiRating !== 'Zero') {
          this.hasPegiRating = true;
          this.pegiRatingValue = getPegiNumber(pegiRating);
        }

        if (this.game.gameDetails.metacriticScore) {
          this.hasMetacritic = true;
        }

        if (this.game.gameDetails.notes) {
          this.hasNotes = true;
        }
        if (this.game.gameDetails.steamAppid) {
          this.hasSteamAppid = true;
        }

        this.categories = this.separateCategories.transform(this.game.gameDetails.categories);
        this.publishers = this.separateCategories.transform(this.game.gameDetails.publishers);
        this.developers = this.separateCategories.transform(this.game.gameDetails.developers);
        this.platforms = this.separateCategories.transform(this.game.gameDetails.platforms);
      }
    });
  }
}
