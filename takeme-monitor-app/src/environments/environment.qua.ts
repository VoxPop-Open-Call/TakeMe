import { NgxLoggerLevel } from 'ngx-logger';

const BACKEND_DOMAIN = 'TBD';
export const BACKEND_BASE_URL = 'https://' + BACKEND_DOMAIN;

export const environment = {

    production: true,

    /* Firebase Configuration */
    firebase: {
        config: {
            apiKey: 'TBD',
            authDomain: 'TBD',
            databaseURL: 'TBD',
            projectId: 'TBD',
            storageBucket: 'TBD',
            messagingSenderId: 'TBD',
            appId: 'TBD',
            measurementId: 'TBD'
        }
    },

    /* auth0 angular-jwt configuration */
    WHITELIST_DOMAINS: [BACKEND_DOMAIN],
    BLACKLIST_ROUTES: ['www.googleapis.com/identitytoolkit'],

    logging: {
        serverLoggingUrl: BACKEND_BASE_URL + '/api/logger',
        level: NgxLoggerLevel.OFF,
        serverLogLevel: NgxLoggerLevel.WARN
    }

};
