import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { Parameter } from './parameter.model';
import { ParameterService } from './parameter.service';

@Injectable()
export class ParameterPopupService {
    private isOpen = false;
    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private parameterService: ParameterService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.parameterService.find(id).subscribe((parameter) => {
                parameter.creationDateTime = this.datePipe
                    .transform(parameter.creationDateTime, 'yyyy-MM-ddThh:mm');
                parameter.lastModifiedDateTime = this.datePipe
                    .transform(parameter.lastModifiedDateTime, 'yyyy-MM-ddThh:mm');
                this.parameterModalRef(component, parameter);
            });
        } else {
            return this.parameterModalRef(component, new Parameter());
        }
    }

    parameterModalRef(component: Component, parameter: Parameter): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.parameter = parameter;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
