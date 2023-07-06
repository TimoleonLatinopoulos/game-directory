import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { GameDetailsFormService } from './game-details-form.service';
import { GameDetailsService } from '../service/game-details.service';
import { IGameDetails } from '../game-details.model';
import { IPlatform } from 'app/entities/platform/platform.model';
import { PlatformService } from 'app/entities/platform/service/platform.service';
import { IDeveloper } from 'app/entities/developer/developer.model';
import { DeveloperService } from 'app/entities/developer/service/developer.service';
import { IPublisher } from 'app/entities/publisher/publisher.model';
import { PublisherService } from 'app/entities/publisher/service/publisher.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';

import { GameDetailsUpdateComponent } from './game-details-update.component';

describe('GameDetails Management Update Component', () => {
  let comp: GameDetailsUpdateComponent;
  let fixture: ComponentFixture<GameDetailsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let gameDetailsFormService: GameDetailsFormService;
  let gameDetailsService: GameDetailsService;
  let platformService: PlatformService;
  let developerService: DeveloperService;
  let publisherService: PublisherService;
  let categoryService: CategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), GameDetailsUpdateComponent],
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
      .overrideTemplate(GameDetailsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GameDetailsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    gameDetailsFormService = TestBed.inject(GameDetailsFormService);
    gameDetailsService = TestBed.inject(GameDetailsService);
    platformService = TestBed.inject(PlatformService);
    developerService = TestBed.inject(DeveloperService);
    publisherService = TestBed.inject(PublisherService);
    categoryService = TestBed.inject(CategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Platform query and add missing value', () => {
      const gameDetails: IGameDetails = { id: 456 };
      const platforms: IPlatform[] = [{ id: 16525 }];
      gameDetails.platforms = platforms;

      const platformCollection: IPlatform[] = [{ id: 60684 }];
      jest.spyOn(platformService, 'query').mockReturnValue(of(new HttpResponse({ body: platformCollection })));
      const additionalPlatforms = [...platforms];
      const expectedCollection: IPlatform[] = [...additionalPlatforms, ...platformCollection];
      jest.spyOn(platformService, 'addPlatformToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ gameDetails });
      comp.ngOnInit();

      expect(platformService.query).toHaveBeenCalled();
      expect(platformService.addPlatformToCollectionIfMissing).toHaveBeenCalledWith(
        platformCollection,
        ...additionalPlatforms.map(expect.objectContaining)
      );
      expect(comp.platformsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Developer query and add missing value', () => {
      const gameDetails: IGameDetails = { id: 456 };
      const developers: IDeveloper[] = [{ id: 5887 }];
      gameDetails.developers = developers;

      const developerCollection: IDeveloper[] = [{ id: 98427 }];
      jest.spyOn(developerService, 'query').mockReturnValue(of(new HttpResponse({ body: developerCollection })));
      const additionalDevelopers = [...developers];
      const expectedCollection: IDeveloper[] = [...additionalDevelopers, ...developerCollection];
      jest.spyOn(developerService, 'addDeveloperToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ gameDetails });
      comp.ngOnInit();

      expect(developerService.query).toHaveBeenCalled();
      expect(developerService.addDeveloperToCollectionIfMissing).toHaveBeenCalledWith(
        developerCollection,
        ...additionalDevelopers.map(expect.objectContaining)
      );
      expect(comp.developersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Publisher query and add missing value', () => {
      const gameDetails: IGameDetails = { id: 456 };
      const publishers: IPublisher[] = [{ id: 664 }];
      gameDetails.publishers = publishers;

      const publisherCollection: IPublisher[] = [{ id: 41285 }];
      jest.spyOn(publisherService, 'query').mockReturnValue(of(new HttpResponse({ body: publisherCollection })));
      const additionalPublishers = [...publishers];
      const expectedCollection: IPublisher[] = [...additionalPublishers, ...publisherCollection];
      jest.spyOn(publisherService, 'addPublisherToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ gameDetails });
      comp.ngOnInit();

      expect(publisherService.query).toHaveBeenCalled();
      expect(publisherService.addPublisherToCollectionIfMissing).toHaveBeenCalledWith(
        publisherCollection,
        ...additionalPublishers.map(expect.objectContaining)
      );
      expect(comp.publishersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Category query and add missing value', () => {
      const gameDetails: IGameDetails = { id: 456 };
      const categories: ICategory[] = [{ id: 93517 }];
      gameDetails.categories = categories;

      const categoryCollection: ICategory[] = [{ id: 506 }];
      jest.spyOn(categoryService, 'query').mockReturnValue(of(new HttpResponse({ body: categoryCollection })));
      const additionalCategories = [...categories];
      const expectedCollection: ICategory[] = [...additionalCategories, ...categoryCollection];
      jest.spyOn(categoryService, 'addCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ gameDetails });
      comp.ngOnInit();

      expect(categoryService.query).toHaveBeenCalled();
      expect(categoryService.addCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        categoryCollection,
        ...additionalCategories.map(expect.objectContaining)
      );
      expect(comp.categoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const gameDetails: IGameDetails = { id: 456 };
      const platform: IPlatform = { id: 48297 };
      gameDetails.platforms = [platform];
      const developers: IDeveloper = { id: 53863 };
      gameDetails.developers = [developers];
      const publishers: IPublisher = { id: 93454 };
      gameDetails.publishers = [publishers];
      const categories: ICategory = { id: 95708 };
      gameDetails.categories = [categories];

      activatedRoute.data = of({ gameDetails });
      comp.ngOnInit();

      expect(comp.platformsSharedCollection).toContain(platform);
      expect(comp.developersSharedCollection).toContain(developers);
      expect(comp.publishersSharedCollection).toContain(publishers);
      expect(comp.categoriesSharedCollection).toContain(categories);
      expect(comp.gameDetails).toEqual(gameDetails);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGameDetails>>();
      const gameDetails = { id: 123 };
      jest.spyOn(gameDetailsFormService, 'getGameDetails').mockReturnValue(gameDetails);
      jest.spyOn(gameDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gameDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gameDetails }));
      saveSubject.complete();

      // THEN
      expect(gameDetailsFormService.getGameDetails).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(gameDetailsService.update).toHaveBeenCalledWith(expect.objectContaining(gameDetails));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGameDetails>>();
      const gameDetails = { id: 123 };
      jest.spyOn(gameDetailsFormService, 'getGameDetails').mockReturnValue({ id: null });
      jest.spyOn(gameDetailsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gameDetails: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gameDetails }));
      saveSubject.complete();

      // THEN
      expect(gameDetailsFormService.getGameDetails).toHaveBeenCalled();
      expect(gameDetailsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGameDetails>>();
      const gameDetails = { id: 123 };
      jest.spyOn(gameDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gameDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(gameDetailsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePlatform', () => {
      it('Should forward to platformService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(platformService, 'comparePlatform');
        comp.comparePlatform(entity, entity2);
        expect(platformService.comparePlatform).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDeveloper', () => {
      it('Should forward to developerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(developerService, 'compareDeveloper');
        comp.compareDeveloper(entity, entity2);
        expect(developerService.compareDeveloper).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePublisher', () => {
      it('Should forward to publisherService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(publisherService, 'comparePublisher');
        comp.comparePublisher(entity, entity2);
        expect(publisherService.comparePublisher).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCategory', () => {
      it('Should forward to categoryService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(categoryService, 'compareCategory');
        comp.compareCategory(entity, entity2);
        expect(categoryService.compareCategory).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
