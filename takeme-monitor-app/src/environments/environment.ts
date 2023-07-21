import { NgxLoggerLevel } from 'ngx-logger';

const BACKEND_DOMAIN = 'localhost:8080';
export const BACKEND_BASE_URL = 'http://' + BACKEND_DOMAIN;

export const environment = {

    production: false,

    /* Firebase Configuration */
    firebase: {
        config: {
            apiKey: 'TBD',
            authDomain: 'TBD',
            databaseURL: 'TBD',
            projectId: 'TBD',
            storageBucket: '',
            messagingSenderId: 'TBD'
        }
    },

    /* auth0 angular-jwt configuration */
    WHITELIST_DOMAINS: [BACKEND_DOMAIN],
    BLACKLIST_ROUTES: ['www.googleapis.com/identitytoolkit'],

    logging: {
        serverLoggingUrl: BACKEND_BASE_URL + '/api/logger',
        level: NgxLoggerLevel.DEBUG,
        serverLogLevel: NgxLoggerLevel.ERROR
    }

};
