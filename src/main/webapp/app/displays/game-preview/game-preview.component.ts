import { UtilService } from './../../shared/util/utils.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { IGame } from 'app/entities/game/game.model';
import { getPegiNumber } from 'app/entities/game-details/game-details.model';
import { MatDialog } from '@angular/material/dialog';
import { DialogComponent } from 'app/shared/components/dialog/dialog.component';
import { GameService } from 'app/entities/game/service/game.service';
import { IUserGame } from 'app/entities/user-game/user-game.model';
import { AccountService } from 'app/core/auth/account.service';
import { UserGameService } from 'app/entities/user-game/service/user-game.service';

@Component({
  selector: 'jhi-game-preview',
  templateUrl: './game-preview.component.html',
  styleUrls: ['./game-preview.component.scss'],
})
export class GamePreviewComponent implements OnInit {
  public game: IGame;
  public userGame: IUserGame;
  public showUserGame = false;
  public favouriteToggle = false;

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
    private userGameService: UserGameService,
    private accountService: AccountService,
    private activatedRoute: ActivatedRoute,
    private dialog: MatDialog,
    private router: Router,
    private utilService: UtilService
  ) {}

  ngOnInit(): void {
    window.scrollTo(0, 0);
    this.activatedRoute.data.subscribe(object => {
      this.game = object.game as IGame;

      if (this.accountService.hasAnyAuthority('ROLE_USER')) {
        this.userGameService.findByGameId(this.game.id).subscribe(
          (response: any) => {
            this.userGame = response.body;
            this.showUserGame = true;
          },
          () => {
            this.showUserGame = true;
          }
        );
      }

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
            this.utilService.openSnackBar('The game has been deleted!', 'success');
            this.router.navigate(['../']);
          },
          (error: any) => {
            this.utilService.openSnackBar(error.error.detail, 'error');
          }
        );
      }
    });
  }

  addToUserGames(): void {
    this.userGameService.create(this.game.id).subscribe(
      () => {
        this.utilService.openSnackBar('The game has been added to your games list!', 'success');
        this.router.navigate(['../my-games']);
      },
      (error: any) => {
        this.utilService.openSnackBar(error.error.detail, 'error');
      }
    );
  }

  removeUserGame(): void {
    const dialogRef = this.dialog.open(DialogComponent, {
      data: {
        dialogTitle: 'Remove Game',
        dialogMessage: 'Are you sure you want to remove this game from your games?',
        dialogYesButton: 'Yes',
        dialogNoButton: 'No',
      },
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result !== undefined) {
        this.delete = result;
      }
      if (this.delete) {
        this.userGameService.delete(this.userGame.id).subscribe(
          () => {
            this.utilService.openSnackBar('The game has been removed from your games list!', 'success');
            this.router.navigate(['../my-games']);
          },
          (error: any) => {
            this.utilService.openSnackBar(error.error.detail, 'error');
            this.favouriteToggle = false;
          }
        );
      }
    });
  }

  favourite(): void {
    if (!this.favouriteToggle) {
      this.favouriteToggle = true;
      this.userGameService.setFavourite(this.userGame.id).subscribe(
        (response: any) => {
          this.userGame = response.body;
          if (this.userGame.favourite) {
            this.utilService.openSnackBar('The game has been set as favourite!', 'success');
          } else {
            this.utilService.openSnackBar('The game has been reset!', 'success');
          }
          this.favouriteToggle = false;
        },
        (error: any) => {
          this.utilService.openSnackBar(error.error.detail, 'error');
          this.favouriteToggle = false;
        }
      );
    }
  }
}
