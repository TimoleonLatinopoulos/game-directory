/* eslint-disable @typescript-eslint/no-unnecessary-condition */
import { UtilService } from './../../shared/util/utils.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { IGame } from 'app/entities/game/game.model';
import { getPegiNumber } from 'app/entities/game-details/game-details.model';
import { MatDialog } from '@angular/material/dialog';
import { ChoiceDialogComponent } from 'app/shared/components/choice-dialog/choice-dialog.component';
import { GameService } from 'app/entities/game/service/game.service';
import { IUserGame } from 'app/entities/user-game/user-game.model';
import { AccountService } from 'app/core/auth/account.service';
import { UserGameService } from 'app/entities/user-game/service/user-game.service';
import { Location } from '@angular/common';
import { Title } from '@angular/platform-browser';
import { CommentService } from 'app/entities/comment/service/comment.service';
import { IComment } from 'app/entities/comment/comment.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

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

  public userComment: IComment;
  public hasOwnComment = false;
  public commentList: IComment[] = [];
  public showComments = false;
  public commentForm: FormGroup;
  public recommended = 0;
  public isCommentEdited = false;
  public userDescription: string;

  public delete = false;

  constructor(
    private gameService: GameService,
    private userGameService: UserGameService,
    private commentService: CommentService,
    private accountService: AccountService,
    private activatedRoute: ActivatedRoute,
    private dialog: MatDialog,
    private router: Router,
    private utilService: UtilService,
    private location: Location,
    private titleService: Title,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    window.scrollTo(0, 0);

    this.commentForm = this.fb.group({
      id: [null],
      description: ['', Validators.required],
      recommended: [null, Validators.required],
      datePosted: [null],
      game: [null],
      user: [null],
    });

    this.activatedRoute.data.subscribe(object => {
      this.game = object.game as IGame;

      this.changeLocation();

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

      this.fetchComments();
    });
  }

  changeLocation(): void {
    if (this.game.title && this.game.title) {
      this.location.replaceState('game-preview/' + this.game.id.toString() + '/' + this.game.title.replace(/\s+/g, '_'));
      this.titleService.setTitle(this.game.title);
    }
  }

  editGameDetails(): void {
    this.router.navigate(['/edit-entry', this.game.id]);
  }

  deleteGame(): void {
    const dialogRef = this.dialog.open(ChoiceDialogComponent, {
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
    const dialogRef = this.dialog.open(ChoiceDialogComponent, {
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

  fetchComments(): void {
    this.showComments = false;
    this.commentList = [];

    this.commentService.findUserCommentByGame(this.game.id).subscribe((response: any) => {
      if (response.body != null && response.body.length !== 0) {
        this.userComment = response.body[0];
        this.commentList.push(this.userComment);
        this.hasOwnComment = true;
      } else {
        this.hasOwnComment = false;
      }

      this.commentService.findAllByGame(this.game.id).subscribe((repsonseList: any) => {
        this.commentList = [...this.commentList, ...repsonseList.body];
        if (this.commentList.length !== 0) {
          this.showComments = true;
        }
      });
    });
  }

  setFormRecommended(value: boolean): void {
    this.commentForm.get('recommended')?.setValue(value);
    this.recommended = value ? 1 : 2;
  }

  submitComment(): void {
    if (!this.commentForm.invalid) {
      this.commentForm.get('game')?.setValue(this.game);
      this.commentService.create(this.commentForm.value).subscribe(
        () => {
          this.utilService.openSnackBar('Your review has been submitted!', 'success');
          this.fetchComments();
        },
        (error: any) => {
          this.utilService.openSnackBar(error.error.detail, 'error');
        }
      );
    }
  }

  deleteComment(): void {
    const dialogRef = this.dialog.open(ChoiceDialogComponent, {
      data: {
        dialogTitle: 'Remove Review',
        dialogMessage: 'Are you sure you want to delete your review?',
        dialogYesButton: 'Yes',
        dialogNoButton: 'No',
      },
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result !== undefined) {
        this.delete = result;
      }
      if (this.delete) {
        this.commentService.delete(this.userComment.id).subscribe(
          () => {
            this.utilService.openSnackBar('The review has been deleted!', 'success');
            this.fetchComments();
          },
          (error: any) => {
            this.utilService.openSnackBar(error.error.detail, 'error');
          }
        );
      }
    });
  }

  showUpdateComment(show: boolean): void {
    if (show) {
      if (this.userComment.description != null) {
        this.userDescription = this.userComment.description.replaceAll('<br>', '\n');
        this.userComment.description = this.userDescription;
      }
      this.commentForm.patchValue(this.userComment);
      this.recommended = this.commentForm.get('recommended')?.value ? 1 : 2;
      this.isCommentEdited = true;
    } else {
      this.userComment.description = this.userDescription.replaceAll('\n', '<br>');
      this.commentForm.reset();
      this.recommended = 0;
      this.isCommentEdited = false;
    }
  }

  updateComment(): void {
    if (!this.commentForm.invalid) {
      const comment = this.commentForm.value;
      comment.user = null;
      comment.datePosted = null;
      this.commentService.update(comment).subscribe(
        () => {
          this.utilService.openSnackBar('Your review has been updated!', 'success');
          this.showUpdateComment(false);
          this.fetchComments();
        },
        (error: any) => {
          this.utilService.openSnackBar(error.error.detail, 'error');
        }
      );
    }
  }
}
