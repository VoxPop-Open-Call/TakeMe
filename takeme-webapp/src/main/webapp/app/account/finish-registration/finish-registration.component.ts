import {Component, OnDestroy, OnInit} from '@angular/core';

import {Router} from "@angular/router";
import {FinishRegistrationService} from "./finish-registration.service";
import {IIdentificationCardType} from "../../shared/model/identification-card-type.model";
import {IdTypeCardService} from "../../shared/services/id-type-card.service";
import {HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {ErrorHandlerProviderService} from "../../shared/providers/error-handler-provider.service";
import {LoginService, Principal} from "../../core";
import {TutorDTO} from "../../shared/model/tutor-dto";
import {EventManager} from "../../core/util/event-manager.service";

@Component({
  selector: 'jhi-finish-registration',
  templateUrl: './finish-registration.component.html',
  providers: [IdTypeCardService]
})
export class FinishRegistrationComponent implements OnInit, OnDestroy {
  typeDocuments: IIdentificationCardType[] = [];
  error: string;
  accountInfo: any;
  idCardType: string;

  constructor(
    private finishRegistrationService: FinishRegistrationService,
    private idTypeCardService: IdTypeCardService,
    private loginService: LoginService,
    private errorHandler: ErrorHandlerProviderService,
    private principal: Principal,
    private router: Router,
    private eventManager: EventManager,
  ) {
  }

  ngOnInit() {
    this.idTypeCardService.getAllIdTypeCards().subscribe({
      next: (result: HttpResponse<IIdentificationCardType[]>) => {
        this.typeDocuments = result.body;
        this.idCardType = this.typeDocuments[0].name;
      },
      error: (error: HttpErrorResponse) => {
        this.errorHandler.showError(error);
      }
    });

    this.accountInfo = {};
  }

  ngOnDestroy(): void {
    this.errorHandler.clean();
  }

  confirm() {
    this.error = null;

    let tutorDTO: TutorDTO = {
      userId: this.principal.getUserIdentity().id,
      name: this.principal.getUserIdentity().firstName + " " + this.principal.getUserIdentity().lastName,
      nifCountry: "pt",
      nifNumber: this.accountInfo.nif,
      identificationCardNumber: this.accountInfo.idCardNumber,
      identificationCardTypeName: this.idCardType,
      phoneNumber: this.accountInfo.phoneNumber,
      photo: this.accountInfo.photo ? this.accountInfo.photo : null
    }

    this.finishRegistrationService.createTutor(tutorDTO).subscribe({
      next: tutor => {
        this.principal.setTutor(tutor);

        this.eventManager.broadcast({
          name: 'authenticationSuccess',
          content: 'Sending Authentication Success'
        });

        this.router.navigate(['famility']);
      },
      error: () => this.error = 'ERROR'
    })
  }

  back() {
    this.loginService.logout();
    this.router.navigate(['../']);
  }

  onAddPhoto(event) {
    const reader = new FileReader();

    if (event.target.files && event.target.files.length) {
      const [file] = event.target.files;
      reader.readAsDataURL(file);

      reader.onload = () => {
        this.accountInfo.photo = reader.result.toString().split(',')[1];
      };
    }
  }
}
