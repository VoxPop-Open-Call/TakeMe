export interface IPhotoDriver {
    userId: string;
    photo: string;
}

export class PhotoDriver implements IPhotoDriver {
    constructor(public userId: string, public photo: string) {
        this.userId = userId;
        this.photo = photo;
    }
}
