import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ParameterComponent } from './parameter.component';
import { ParameterDetailComponent } from './parameter-detail.component';
import { ParameterPopupComponent } from './parameter-dialog.component';
import { ParameterDeletePopupComponent } from './parameter-delete-dialog.component';

import { Principal } from '../../shared';

export const parameterRoute: Routes = [
    {
        path: 'parameter',
        component: ParameterComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'skeletonApp.parameter.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'parameter/:id',
        component: ParameterDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'skeletonApp.parameter.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const parameterPopupRoute: Routes = [
    {
        path: 'parameter-new',
        component: ParameterPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'skeletonApp.parameter.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'parameter/:id/edit',
        component: ParameterPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'skeletonApp.parameter.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'parameter/:id/delete',
        component: ParameterDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'skeletonApp.parameter.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
