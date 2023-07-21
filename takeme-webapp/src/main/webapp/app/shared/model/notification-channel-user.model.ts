import { INotificationChannel } from 'app/shared/model//notification-channel.model';
import { INotificationType } from 'app/shared/model//notification-type.model';

export interface INotificationChannelUser {
    id?: number;
    active?: boolean;
    userId?: string;
    notificationChannel?: INotificationChannel;
    notificationType?: INotificationType;
}

export class NotificationChannelUser implements INotificationChannelUser {
    constructor(
        public id?: number,
        public active?: boolean,
        public userId?: string,
        public notificationChannel?: INotificationChannel,
        public notificationType?: INotificationType
    ) {
        this.active = this.active || false;
    }
}
