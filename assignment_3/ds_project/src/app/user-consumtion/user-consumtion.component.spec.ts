import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserConsumtionComponent } from './user-consumtion.component';

describe('UserConsumtionComponent', () => {
  let component: UserConsumtionComponent;
  let fixture: ComponentFixture<UserConsumtionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserConsumtionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserConsumtionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
