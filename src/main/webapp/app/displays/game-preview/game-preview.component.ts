import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { IGame } from 'app/entities/game/game.model';
import { getPegiNumber } from 'app/entities/game-details/game-details.model';
import { MatDialog } from '@angular/material/dialog';
import { DialogComponent } from 'app/shared/components/dialog/dialog.component';
import { GameService } from 'app/entities/game/service/game.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SnackBarAlertComponent } from 'app/shared/components/snack-bar-alert/snack-bar-alert.component';

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

  public delete = false;

  constructor(
    private gameService: GameService,
    private activatedRoute: ActivatedRoute,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private router: Router
  ) {}

  ngOnInit(): void {
    window.scrollTo(0, 0);
    this.activatedRoute.data.subscribe(object => {
      this.game = object.game as IGame;

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
      }
    });
  }

  editGameDetails(): void {
    this.router.navigate(['/edit-entry', this.game.id]);
  }

  deleteGame(): void {
    const dialogRef = this.dialog.open(DialogComponent, {
      data: {
        dialogTitle: 'Delete Game',
        dialogMessage: 'Are you sure you want to delete this game?',
        dialogYesButton: 'Yes',
        dialogNoButton: 'No',
      },
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result !== undefined) {
        this.delete = result;
      }
      if (this.delete) {
        this.gameService.delete(this.game.id).subscribe(
          () => {
            this.openSnackBar('The game has been deleted');
            this.router.navigate(['../']);
          },
          (error: any) => {
            this.openSnackBar(error.error.detail);
          }
        );
      }
    });
  }

  openSnackBar(errorMessage: string): void {
    this.snackBar.openFromComponent(SnackBarAlertComponent, {
      duration: 5000,
      data: errorMessage,
      panelClass: ['snackbar', 'success-snackbar'],
    });
  }
}
