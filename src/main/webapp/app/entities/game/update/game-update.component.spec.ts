import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { GameFormService } from './game-form.service';
import { GameService } from '../service/game.service';
import { IGame } from '../game.model';
import { IGameDetails } from 'app/entities/game-details/game-details.model';
import { GameDetailsService } from 'app/entities/game-details/service/game-details.service';

import { GameUpdateComponent } from './game-update.component';

describe('Game Management Update Component', () => {
  let comp: GameUpdateComponent;
  let fixture: ComponentFixture<GameUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let gameFormService: GameFormService;
  let gameService: GameService;
  let gameDetailsService: GameDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), GameUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(GameUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GameUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    gameFormService = TestBed.inject(GameFormService);
    gameService = TestBed.inject(GameService);
    gameDetailsService = TestBed.inject(GameDetailsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call gameDetails query and add missing value', () => {
      const game: IGame = { id: 456 };
      const gameDetails: IGameDetails = { id: 29431 };
      game.gameDetails = gameDetails;

      const gameDetailsCollection: IGameDetails[] = [{ id: 2478 }];
      jest.spyOn(gameDetailsService, 'query').mockReturnValue(of(new HttpResponse({ body: gameDetailsCollection })));
      const expectedCollection: IGameDetails[] = [gameDetails, ...gameDetailsCollection];
      jest.spyOn(gameDetailsService, 'addGameDetailsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ game });
      comp.ngOnInit();

      expect(gameDetailsService.query).toHaveBeenCalled();
      expect(gameDetailsService.addGameDetailsToCollectionIfMissing).toHaveBeenCalledWith(gameDetailsCollection, gameDetails);
      expect(comp.gameDetailsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const game: IGame = { id: 456 };
      const gameDetails: IGameDetails = { id: 39476 };
      game.gameDetails = gameDetails;

      activatedRoute.data = of({ game });
      comp.ngOnInit();

      expect(comp.gameDetailsCollection).toContain(gameDetails);
      expect(comp.game).toEqual(game);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGame>>();
      const game = { id: 123 };
      jest.spyOn(gameFormService, 'getGame').mockReturnValue(game);
      jest.spyOn(gameService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ game });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: game }));
      saveSubject.complete();

      // THEN
      expect(gameFormService.getGame).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(gameService.update).toHaveBeenCalledWith(expect.objectContaining(game));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGame>>();
      const game = { id: 123 };
      jest.spyOn(gameFormService, 'getGame').mockReturnValue({ id: null });
      jest.spyOn(gameService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ game: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: game }));
      saveSubject.complete();

      // THEN
      expect(gameFormService.getGame).toHaveBeenCalled();
      expect(gameService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGame>>();
      const game = { id: 123 };
      jest.spyOn(gameService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ game });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(gameService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareGameDetails', () => {
      it('Should forward to gameDetailsService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(gameDetailsService, 'compareGameDetails');
        comp.compareGameDetails(entity, entity2);
        expect(gameDetailsService.compareGameDetails).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
