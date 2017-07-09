import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SkeletonSharedModule } from '../../shared';
import {
    ParameterService,
    ParameterPopupService,
    ParameterComponent,
    ParameterDetailComponent,
    ParameterDialogComponent,
    ParameterPopupComponent,
    ParameterDeletePopupComponent,
    ParameterDeleteDialogComponent,
    parameterRoute,
    parameterPopupRoute,
} from './';

const ENTITY_STATES = [
    ...parameterRoute,
    ...parameterPopupRoute,
];

@NgModule({
    imports: [
        SkeletonSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ParameterComponent,
        ParameterDetailComponent,
        ParameterDialogComponent,
        ParameterDeleteDialogComponent,
        ParameterPopupComponent,
        ParameterDeletePopupComponent,
    ],
    entryComponents: [
        ParameterComponent,
        ParameterDialogComponent,
        ParameterPopupComponent,
        ParameterDeleteDialogComponent,
        ParameterDeletePopupComponent,
    ],
    providers: [
        ParameterService,
        ParameterPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SkeletonParameterModule {}
