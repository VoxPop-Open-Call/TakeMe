import { TestBed } from '@angular/core/testing';

import { UserProvider } from './user-provider.service';

describe('UserProviderService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: UserProvider = TestBed.get(UserProvider);
    expect(service).toBeTruthy();
  });
});
