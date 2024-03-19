/*
 * G4IT
 * Copyright 2023 Sopra Steria
 *
 * This product includes software developed by
 * French Ecological Ministery (https://gitlab-forge.din.developpement-durable.gouv.fr/pub/numeco/m4g/numecoeval)
 */ 
import { NgModule } from "@angular/core";
import { NgxSpinnerModule } from "ngx-spinner";
import { SharedModule } from "../core/shared/shared.module";
import { HeaderComponent } from "./header/header.component";
import { LayoutComponent } from "./layout.component";
import { layoutRouter } from "./layout.router";

@NgModule({
    declarations: [LayoutComponent, HeaderComponent],
    imports: [SharedModule, NgxSpinnerModule, layoutRouter],
})
export class LayoutModule {}
