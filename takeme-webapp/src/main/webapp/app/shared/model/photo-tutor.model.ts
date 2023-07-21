export interface IPhotoTutor {
    userId: string;
    photo: string;
}

export class PhotoTutor implements IPhotoTutor {
    constructor(public userId: string, public photo: string) {
        this.userId = userId;
        this.photo = photo;
    }
}
