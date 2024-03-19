/*
 * G4IT
 * Copyright 2023 Sopra Steria
 *
 * This product includes software developed by
 * French Ecological Ministery (https://gitlab-forge.din.developpement-durable.gouv.fr/pub/numeco/m4g/numecoeval)
 */ 
import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, ReplaySubject, tap } from "rxjs";

import { environment } from "src/environments/environment";
import { User } from "../../interfaces/user.interfaces";

const endpoint = environment.apiEndpoints.users;

@Injectable({
    providedIn: "root",
})
export class UserDataService {
    userSubject = new ReplaySubject<User>(1);

    constructor(private http: HttpClient) {}

    fetchUserInfo(): Observable<User> {
        return this.http.get<User>(`${endpoint}/me`).pipe(
            tap((user) => {
                this.userSubject.next(user);
            })
        );
    }
    // getAll() {
    //     return this.http.get<User>(`${endpoint}/me`);
    // }

}
