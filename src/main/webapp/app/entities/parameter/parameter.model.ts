import { BaseEntity } from './../../shared';

export class Parameter implements BaseEntity {
    constructor(
        public id?: number,
        public domain?: string,
        public parameter?: string,
        public name?: string,
        public description?: string,
        public value?: string,
        public creationUser?: string,
        public creationDateTime?: any,
        public lastModifiedUser?: string,
        public lastModifiedDateTime?: any,
    ) {
    }
}
