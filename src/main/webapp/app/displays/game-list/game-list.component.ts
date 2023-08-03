import { Component, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { ActivatedRoute } from '@angular/router';
import { ISearchType } from 'app/entities/game/game.model';
import { GameService } from 'app/entities/game/service/game.service';
import { UserGameService } from 'app/entities/user-game/service/user-game.service';

@Component({
  selector: 'jhi-game-list',
  templateUrl: './game-list.component.html',
  styleUrls: ['./game-list.component.scss'],
})
export class GameListComponent implements OnInit {
  public isLoading = false;
  public gameListDataResult: ISearchType;

  public userGames = false;

  public state = {
    skip: 0,
    take: 10,
    filter: { logic: 'and', filters: [] as any[] },
    sort: [{ field: 'title', dir: 'asc' }],
  };

  constructor(public gameService: GameService, public userGameService: UserGameService, public route: ActivatedRoute) {}

  ngOnInit(): void {
    this.userGames = this.route.snapshot.data['userGames'];
    this.fetchGameData();
  }

  handlePageEvent(e: PageEvent): void {
    this.state.take = e.pageSize;
    this.state.skip = e.pageIndex;
    this.fetchGameData();
  }

  public filter(event: EventTarget | null): void {
    const value = (event as HTMLInputElement).value;
    if (value && value.length >= 3) {
      const newFilter = { field: 'title', operator: 'like', value };
      this.state.filter.filters = [];
      this.state.filter.filters.push(newFilter);
      this.state.skip = 0;
      this.fetchGameData();
    } else {
      if (this.state.filter.filters.length !== 0) {
        this.state.filter.filters = [];
        this.state.skip = 0;
        this.fetchGameData();
      }
    }
  }

  public fetchGameData(): void {
    this.isLoading = true;
    if (this.userGames) {
      this.userGameService.search(this.state).subscribe(
        (res: any) => {
          this.gameListDataResult = {
            data: res.data.content,
            total: res.data.totalEntries,
          };
          this.isLoading = false;
          window.scrollTo(0, 0);
        },
        () => {
          this.isLoading = false;
        }
      );
    } else {
      this.gameService.search(this.state).subscribe(
        (res: any) => {
          this.gameListDataResult = {
            data: res.data.content,
            total: res.data.totalEntries,
          };
          this.isLoading = false;
          window.scrollTo(0, 0);
        },
        () => {
          this.isLoading = false;
        }
      );
    }
  }
}
