import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PlatformService } from '../service/platform.service';

import { PlatformComponent } from './platform.component';

describe('Platform Management Component', () => {
  let comp: PlatformComponent;
  let fixture: ComponentFixture<PlatformComponent>;
  let service: PlatformService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'platform', component: PlatformComponent }]),
        HttpClientTestingModule,
        PlatformComponent,
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
      .overrideTemplate(PlatformComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PlatformComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PlatformService);

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
    expect(comp.platforms?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to platformService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getPlatformIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getPlatformIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
