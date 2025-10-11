import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BarcoListComponent } from './barco-list.component';

describe('BarcoListComponent', () => {
  let component: BarcoListComponent;
  let fixture: ComponentFixture<BarcoListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BarcoListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BarcoListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
