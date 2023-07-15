import { Component, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { ISearchType } from 'app/entities/game/game.model';
import { GameService } from 'app/entities/game/service/game.service';

@Component({
  selector: 'jhi-game-list',
  templateUrl: './game-list.component.html',
  styleUrls: ['./game-list.component.scss'],
})
export class GameListComponent implements OnInit {
  public isLoading = false;
  public gameListDataResult: ISearchType;

  public state = {
    skip: 0,
    take: 10,
    filter: { logic: 'and', filters: [] as any[] },
    sort: [{ field: 'title', dir: 'asc' }],
  };

  constructor(public gameService: GameService) {}

  ngOnInit(): void {
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
