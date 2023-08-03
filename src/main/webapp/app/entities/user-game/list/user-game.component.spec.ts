import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { UserGameService } from '../service/user-game.service';

import { UserGameComponent } from './user-game.component';

describe('UserGame Management Component', () => {
  let comp: UserGameComponent;
  let fixture: ComponentFixture<UserGameComponent>;
  let service: UserGameService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'user-game', component: UserGameComponent }]),
        HttpClientTestingModule,
        UserGameComponent,
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
      .overrideTemplate(UserGameComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserGameComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(UserGameService);

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
    expect(comp.userGames?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to userGameService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getUserGameIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getUserGameIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
