export interface IServiceStopPointFrequency {
    id?: number;
    description?: string;
    weekInterval?: number;
}

export class ServiceStopPointFrequency implements IServiceStopPointFrequency {
    constructor(
        public id?: number,
        public description?: string,
        public weekInterval?: number
    ) {
    }
}
