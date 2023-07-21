import { Moment } from "moment";

export class ChildDTO {
  name: string;
  nifCountry: string;
  nifNumber: string;
  dateOfBirth: Moment;
  photo: string;

  constructor(name: string, nifCountry: string, nifNumber: string, dateOfBirth: Moment, photo: string) {
      this.name = name;
      this.nifCountry = nifCountry;
      this.nifNumber = nifNumber;
      this.dateOfBirth = dateOfBirth;
      this.photo = photo;
  }
}
