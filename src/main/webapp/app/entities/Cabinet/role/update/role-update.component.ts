import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IRole, Role } from '../role.model';
import { RoleService } from '../service/role.service';
import { RoleName } from 'app/entities/enumerations/role-name.model';

@Component({
  selector: 'jhi-role-update',
  templateUrl: './role-update.component.html',
})
export class RoleUpdateComponent implements OnInit {
  isSaving = false;
  roleNameValues = Object.keys(RoleName);

  editForm = this.fb.group({
    userUuid: [null, [Validators.required]],
    id: [null, [Validators.required]],
    rolename: [],
  });

  constructor(protected roleService: RoleService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ role }) => {
      this.updateForm(role);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const role = this.createFromForm();
    if (role.id !== undefined) {
      this.subscribeToSaveResponse(this.roleService.update(role));
    } else {
      this.subscribeToSaveResponse(this.roleService.create(role));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRole>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(role: IRole): void {
    this.editForm.patchValue({
      userUuid: role.userUuid,
      id: role.id,
      rolename: role.rolename,
    });
  }

  protected createFromForm(): IRole {
    return {
      ...new Role(),
      userUuid: this.editForm.get(['userUuid'])!.value,
      id: this.editForm.get(['id'])!.value,
      rolename: this.editForm.get(['rolename'])!.value,
    };
  }
}
