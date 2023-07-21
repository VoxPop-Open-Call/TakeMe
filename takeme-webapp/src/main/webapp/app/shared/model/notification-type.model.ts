export interface INotificationType {
    id?: number;
    active?: boolean;
    title?: string;
    userConfigurable?: boolean;
}

export class NotificationType implements INotificationType {
    constructor(
        public id?: number,
        public active?: boolean,
        public title?: string,
        public userConfigurable?: boolean
    ) {
        this.active = this.active || false;
        this.userConfigurable = this.userConfigurable || false;
    }
}
