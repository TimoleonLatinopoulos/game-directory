import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateGameEntryComponent } from './create-game-entry.component';

describe('CreateGameEntryComponent', () => {
  let component: CreateGameEntryComponent;
  let fixture: ComponentFixture<CreateGameEntryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CreateGameEntryComponent],
    });
    fixture = TestBed.createComponent(CreateGameEntryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
