import { EnvConfigModel } from 'app/core/models/env-config.model';
import { Injectable } from '@angular/core';

export let environment = {
  config: new EnvConfigModel(),
};

@Injectable({ providedIn: 'root' })
export class Environment {

    constructor() {}

}
