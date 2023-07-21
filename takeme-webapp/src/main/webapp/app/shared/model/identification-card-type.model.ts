export interface IIdentificationCardType {
    id?: number;
    name?: string;
}

export class IdentificationCardType implements IIdentificationCardType {
    constructor(public id?: number, public name?: string) {}
}
