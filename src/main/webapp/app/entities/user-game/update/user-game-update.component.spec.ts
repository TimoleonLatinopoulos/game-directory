import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { UserGameFormService } from './user-game-form.service';
import { UserGameService } from '../service/user-game.service';
import { IUserGame } from '../user-game.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IGame } from 'app/entities/game/game.model';
import { GameService } from 'app/entities/game/service/game.service';

import { UserGameUpdateComponent } from './user-game-update.component';

describe('UserGame Management Update Component', () => {
  let comp: UserGameUpdateComponent;
  let fixture: ComponentFixture<UserGameUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userGameFormService: UserGameFormService;
  let userGameService: UserGameService;
  let userService: UserService;
  let gameService: GameService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), UserGameUpdateComponent],
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
      .overrideTemplate(UserGameUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserGameUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userGameFormService = TestBed.inject(UserGameFormService);
    userGameService = TestBed.inject(UserGameService);
    userService = TestBed.inject(UserService);
    gameService = TestBed.inject(GameService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const userGame: IUserGame = { id: 456 };
      const user: IUser = { id: 89195 };
      userGame.user = user;

      const userCollection: IUser[] = [{ id: 69180 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userGame });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Game query and add missing value', () => {
      const userGame: IUserGame = { id: 456 };
      const game: IGame = { id: 49187 };
      userGame.game = game;

      const gameCollection: IGame[] = [{ id: 1084 }];
      jest.spyOn(gameService, 'query').mockReturnValue(of(new HttpResponse({ body: gameCollection })));
      const additionalGames = [game];
      const expectedCollection: IGame[] = [...additionalGames, ...gameCollection];
      jest.spyOn(gameService, 'addGameToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userGame });
      comp.ngOnInit();

      expect(gameService.query).toHaveBeenCalled();
      expect(gameService.addGameToCollectionIfMissing).toHaveBeenCalledWith(
        gameCollection,
        ...additionalGames.map(expect.objectContaining)
      );
      expect(comp.gamesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const userGame: IUserGame = { id: 456 };
      const user: IUser = { id: 96797 };
      userGame.user = user;
      const game: IGame = { id: 13092 };
      userGame.game = game;

      activatedRoute.data = of({ userGame });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.gamesSharedCollection).toContain(game);
      expect(comp.userGame).toEqual(userGame);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserGame>>();
      const userGame = { id: 123 };
      jest.spyOn(userGameFormService, 'getUserGame').mockReturnValue(userGame);
      jest.spyOn(userGameService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userGame });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userGame }));
      saveSubject.complete();

      // THEN
      expect(userGameFormService.getUserGame).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(userGameService.update).toHaveBeenCalledWith(expect.objectContaining(userGame));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserGame>>();
      const userGame = { id: 123 };
      jest.spyOn(userGameFormService, 'getUserGame').mockReturnValue({ id: null });
      jest.spyOn(userGameService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userGame: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userGame }));
      saveSubject.complete();

      // THEN
      expect(userGameFormService.getUserGame).toHaveBeenCalled();
      expect(userGameService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserGame>>();
      const userGame = { id: 123 };
      jest.spyOn(userGameService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userGame });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userGameService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareGame', () => {
      it('Should forward to gameService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(gameService, 'compareGame');
        comp.compareGame(entity, entity2);
        expect(gameService.compareGame).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
