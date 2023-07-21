import {StackFrame} from 'error-stack-parser';

export class ErrorWithKey implements Error {
    constructor(
        public message: string,
        public name: string,
        public stack: string,
        public key: string) { }
}

export class ErrorDetail  {
    appId: string;
    user: string;
    name: string;
    message: string;
    url: string;
    status: number;
    stack: StackFrame[];

}
