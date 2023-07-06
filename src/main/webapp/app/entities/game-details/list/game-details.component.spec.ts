import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { GameDetailsService } from '../service/game-details.service';

import { GameDetailsComponent } from './game-details.component';

describe('GameDetails Management Component', () => {
  let comp: GameDetailsComponent;
  let fixture: ComponentFixture<GameDetailsComponent>;
  let service: GameDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'game-details', component: GameDetailsComponent }]),
        HttpClientTestingModule,
        GameDetailsComponent,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(GameDetailsComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GameDetailsComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(GameDetailsService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.gameDetails?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to gameDetailsService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getGameDetailsIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getGameDetailsIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
