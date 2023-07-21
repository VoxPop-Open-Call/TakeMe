export const enum NotificationChannelType {
    EMAIL = 'EMAIL',
    PUSH_NOTIFICATION = 'PUSH_NOTIFICATION'
}

export interface INotificationChannel {
    id?: number;
    type?: NotificationChannelType;
    active?: boolean;
}

export class NotificationChannel implements INotificationChannel {
    constructor(public id?: number, public type?: NotificationChannelType, public active?: boolean) {
        this.active = this.active || false;
    }
}
