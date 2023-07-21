import { FirebaseConfigModel } from 'app/core/models/firebase-config.model';

export class EnvConfigModel {
    production: boolean;
    firebase: FirebaseConfigModel;
    authorizationHeader: string;
    whiteListDomains: string[];
    blackListRoutes: string[];
}
