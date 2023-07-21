import { ILocation } from 'app/shared/model/location.model';

export class BuildAddress {
    static buildAddressFromLocationObject(location: ILocation): string {
        if (!location) {
            return '';
        }

        let address;
        address = location.street;
        address = address + ', ' + location.portNumber;

        if (location.floor) {
            address = address + ', ' + location.floor;
        }

        address = address + ', ' + location.city + ' ' + location.postalCode;

        return address;
    }
}
