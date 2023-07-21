import { ComponentFixture, inject, TestBed } from '@angular/core/testing';
import { ElementRef, Renderer } from '@angular/core';
import { of, throwError } from 'rxjs';

import { FamilityBackofficeTestModule } from '../../../../test.module';
import { PasswordResetInitComponent } from 'app/account/password-reset/init/password-reset-init.component';
import { PasswordResetInitService } from 'app/account/password-reset/init/password-reset-init.service';
import { EMAIL_NOT_FOUND_TYPE } from 'app/shared';

describe('Component Tests', () => {
    describe('PasswordResetInitComponent', () => {
        let fixture: ComponentFixture<PasswordResetInitComponent>;
        let comp: PasswordResetInitComponent;

        beforeEach(() => {
            fixture = TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [PasswordResetInitComponent],
                providers: [
                    PasswordResetInitService,
                    {
                        provide: Renderer,
                        useValue: {
                            invokeElementMethod(renderElement: any, methodName: string, args?: any[]) {}
                        }
                    },
                    {
                        provide: ElementRef,
                        useValue: new ElementRef(null)
                    }
                ]
            })
                .overrideTemplate(PasswordResetInitComponent, '')
                .createComponent(PasswordResetInitComponent);
            comp = fixture.componentInstance;
            comp.ngOnInit();
        });

        it('should define its initial state', () => {
            expect(comp.success).toBeUndefined();
            expect(comp.error).toBeUndefined();
            expect(comp.errorEmailNotExists).toBeUndefined();
            expect(comp.resetAccount).toEqual({});
        });

        it('sets focus after the view has been initialized', inject([ElementRef], (elementRef: ElementRef) => {
            const element = fixture.nativeElement;
            const node = {
                focus() {}
            };

            elementRef.nativeElement = element;
            spyOn(element, 'querySelector').and.returnValue(node);
            spyOn(node, 'focus');

            comp.ngAfterViewInit();

            expect(element.querySelector).toHaveBeenCalledWith('#email');
            expect(node.focus).toHaveBeenCalled();
        }));
    });
});
