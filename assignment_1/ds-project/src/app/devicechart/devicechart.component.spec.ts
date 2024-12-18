import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DevicechartComponent } from './devicechart.component';

describe('DevicechartComponent', () => {
  let component: DevicechartComponent;
  let fixture: ComponentFixture<DevicechartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DevicechartComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DevicechartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
