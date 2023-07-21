import { Moment } from 'moment';
import { INotificationChannel } from 'app/shared/model//notification-channel.model';
import { INotificationType } from 'app/shared/model//notification-type.model';

export interface IUserNotification {
    id?: number;
    sentDate?: Moment;
    title?: string;
    body?: string;
    userId?: string;
    notificationChannel?: INotificationChannel;
    notificationType?: INotificationType;
}

export class UserNotification implements IUserNotification {
    constructor(
        public id?: number,
        public sentDate?: Moment,
        public title?: string,
        public body?: string,
        public userId?: string,
        public notificationChannel?: INotificationChannel,
        public notificationType?: INotificationType
    ) {}
}
