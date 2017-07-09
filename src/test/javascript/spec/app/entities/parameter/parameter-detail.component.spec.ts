/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SkeletonTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ParameterDetailComponent } from '../../../../../../main/webapp/app/entities/parameter/parameter-detail.component';
import { ParameterService } from '../../../../../../main/webapp/app/entities/parameter/parameter.service';
import { Parameter } from '../../../../../../main/webapp/app/entities/parameter/parameter.model';

describe('Component Tests', () => {

    describe('Parameter Management Detail Component', () => {
        let comp: ParameterDetailComponent;
        let fixture: ComponentFixture<ParameterDetailComponent>;
        let service: ParameterService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SkeletonTestModule],
                declarations: [ParameterDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ParameterService,
                    JhiEventManager
                ]
            }).overrideTemplate(ParameterDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ParameterDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ParameterService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Parameter(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.parameter).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
