export class FirebaseConfigModel {
    constructor(
        public apiKey: string,
        public authDomain: string,
        public databaseURL: string,
        public projectId: string,
        public storageBucket: string,
        public messagingSenderId: string
    ) {}
}
