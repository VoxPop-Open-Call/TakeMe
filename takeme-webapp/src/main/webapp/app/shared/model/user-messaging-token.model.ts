export const enum OriginType {
    BROWSER = 'BROWSER',
    NATIVE = 'NATIVE'
}

export interface IUserMessagingToken {
    id?: number;
    token?: string;
    origin?: OriginType;
    userId?: string;
}

export class UserMessagingToken implements IUserMessagingToken {
    constructor(public id?: number, public token?: string, public origin?: OriginType, public userId?: string) {}
}
