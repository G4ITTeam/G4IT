import { AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms";

export function noWhitespaceValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        const isWhitespace = String(control.value || "").trim().length === 0;
        const isValid = !isWhitespace;
        return isValid ? null : { required: true };
    };
}
