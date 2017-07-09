import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Parameter } from './parameter.model';
import { ParameterPopupService } from './parameter-popup.service';
import { ParameterService } from './parameter.service';

@Component({
    selector: 'jhi-parameter-dialog',
    templateUrl: './parameter-dialog.component.html'
})
export class ParameterDialogComponent implements OnInit {

    parameter: Parameter;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private parameterService: ParameterService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.parameter.id !== undefined) {
            this.subscribeToSaveResponse(
                this.parameterService.update(this.parameter));
        } else {
            this.subscribeToSaveResponse(
                this.parameterService.create(this.parameter));
        }
    }

    private subscribeToSaveResponse(result: Observable<Parameter>) {
        result.subscribe((res: Parameter) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Parameter) {
        this.eventManager.broadcast({ name: 'parameterListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-parameter-popup',
    template: ''
})
export class ParameterPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private parameterPopupService: ParameterPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.parameterPopupService
                    .open(ParameterDialogComponent, params['id']);
            } else {
                this.modalRef = this.parameterPopupService
                    .open(ParameterDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
