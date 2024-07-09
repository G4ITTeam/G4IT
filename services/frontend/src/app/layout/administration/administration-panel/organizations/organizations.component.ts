/*
 * G4IT
 * Copyright 2023 Sopra Steria
 *
 * This product includes software developed by
 * French Ecological Ministery (https://gitlab-forge.din.developpement-durable.gouv.fr/pub/numeco/m4g/numecoeval)
 */
import { Component } from "@angular/core";
import { TranslateService } from "@ngx-translate/core";
import { ConfirmationService, MessageService } from "primeng/api";
import { take } from "rxjs";
import {
    Organization,
    OrganizationUpsertRest,
    Subscriber,
} from "src/app/core/interfaces/administration.interfaces";
import { AdministrationService } from "src/app/core/service/business/administration.service";
import { UserDataService } from "src/app/core/service/data/user-data.service";
import { Constants } from "src/constants";

@Component({
    selector: "app-organizations",
    templateUrl: "./organizations.component.html",
    providers: [ConfirmationService, MessageService],
})
export class OrganizationsComponent {
    editable = false;
    subscribersDetails!: Subscriber[];
    unmodifiedSubscribersDetails!: Subscriber[];
    subscriber: any;
    newOrganization: Organization = {} as Organization;

    status = Constants.ORGANIZATION_STATUSES;

    constructor(
        private confirmationService: ConfirmationService,
        public administrationService: AdministrationService,
        private translate: TranslateService,
        private userDataService: UserDataService,
    ) {}

    ngOnInit() {
        this.init();
    }

    init(subscriber: string | undefined = undefined) {
        this.administrationService.getOrganizations().subscribe((res: Subscriber[]) => {
            this.subscribersDetails = res;
            this.unmodifiedSubscribersDetails = JSON.parse(JSON.stringify(res));
            if (subscriber) {
                this.subscriber = this.unmodifiedSubscribersDetails.find(
                    (s) => s.name === subscriber,
                );
            }
        });
    }

    checkOrganization(event: any, organization: Organization, subscriber: Subscriber) {
        const organizations =
            this.unmodifiedSubscribersDetails.find((s) => s.name === subscriber.name)
                ?.organizations || [];

        organization.uiStatus = undefined;

        if (event.trim().includes(" ")) {
            organization.uiStatus = "SPACE";
            return;
        }

        if (
            organizations.some((org) => org.name === event && org.id !== organization.id)
        ) {
            organization.uiStatus = "DUPLICATE";
            return;
        }

        if (event && !organizations.some((org) => org.name === event)) {
            organization.uiStatus = "OK";
        }
    }

    confirmDelete(event: Event, organization: Organization) {
        this.confirmationService.confirm({
            target: event.target as EventTarget,
            message: this.translate.instant("administration.delete-message"),
            header: this.translate.instant("administration.delete-confirmation"),
            icon: "pi pi-info-circle",
            acceptLabel: this.translate.instant("administration.delete"),
            acceptButtonStyleClass: "p-button-danger center",
            rejectButtonStyleClass: Constants.CONSTANT_VALUE.NONE,
            acceptIcon: Constants.CONSTANT_VALUE.NONE,
            rejectIcon: Constants.CONSTANT_VALUE.NONE,
            rejectVisible: false,

            accept: () => {
                this.updateOrganization(organization.id, {
                    subscriberId: this.subscriber.id,
                    name: organization.name.trim(),
                    status: Constants.ORGANIZATION_STATUSES.TO_BE_DELETED,
                });
            },
        });
    }

    confirmToActive(organization: Organization) {
        this.updateOrganization(organization.id, {
            subscriberId: this.subscriber.id,
            name: organization.name.trim(),
            status: Constants.ORGANIZATION_STATUSES.ACTIVE,
        });
    }

    saveOrganizations(organizations: Organization[]) {
        organizations
            .filter((organization) => organization.uiStatus === "OK")
            .forEach((organization) => {
                this.updateOrganization(organization.id, {
                    subscriberId: this.subscriber.id,
                    name: organization.name,
                    status: organization.status,
                });
            });
    }

    addOrganization(organization: Organization) {
        if (organization === undefined) return;
        let body = {
            subscriberId: this.subscriber.id,
            name: organization.name.trim(),
            status: Constants.ORGANIZATION_STATUSES.ACTIVE,
        };
        this.administrationService.postOrganization(body).subscribe((_) => {
            this.init(this.subscriber.name);
            this.newOrganization = {} as Organization;
            this.editable = false;
            this.userDataService.fetchUserInfo().pipe(take(1)).subscribe();
        });
    }

    updateOrganization(organizationId: number, body: OrganizationUpsertRest) {
        this.administrationService
            .updateOrganization(organizationId, body)
            .subscribe((_) => {
                this.init(this.subscriber.name);
                this.userDataService.fetchUserInfo().pipe(take(1)).subscribe();
            });
    }
}
