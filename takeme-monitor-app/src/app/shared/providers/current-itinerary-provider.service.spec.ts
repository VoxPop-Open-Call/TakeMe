import { TestBed } from '@angular/core/testing';

import { CurrentItineraryProvider } from './current-itinerary-provider.service';

describe('CurrentItineraryProvider', () => {
    beforeEach(() => TestBed.configureTestingModule({}));

    it('should be created', () => {
        const service: CurrentItineraryProvider = TestBed.get(CurrentItineraryProvider);
        expect(service).toBeTruthy();
    });
});
