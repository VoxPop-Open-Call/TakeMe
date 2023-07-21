export interface IPhotoChild {
    id: string;
    photo: string;
}

export class PhotoChild implements IPhotoChild {
    constructor(public id: string, public photo: string) {
        this.id = id;
        this.photo = photo;
    }
}
