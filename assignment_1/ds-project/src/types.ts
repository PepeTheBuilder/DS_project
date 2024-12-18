import { HttpContext, HttpHeaders, HttpParams } from "@angular/common/http";

export interface Options {
        headers?: HttpHeaders | {
            [header: string]: string | string[];
        };
        observe?: 'body';
        context?: HttpContext;
        params?: HttpParams | {
            [param: string]: string | number | boolean | ReadonlyArray<string | number | boolean>;
        };
        reportProgress?: boolean;
        responseType?: 'json';
        withCredentials?: boolean;
        transferCache?: {
            includeHeaders?: string[];
        } | boolean;
        
}

export interface Device {
  id: number;
  name: string;
  location: string;
  email: string;
  maxkwh: number;
}
   
export interface User {
    id: number;
    username: string;
    password: string;
    email: string;
    admin: boolean;
}