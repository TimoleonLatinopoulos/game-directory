import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IUserGame } from '../user-game.model';
import { UserGameService } from '../service/user-game.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './user-game-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class UserGameDeleteDialogComponent {
  userGame?: IUserGame;

  constructor(protected userGameService: UserGameService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userGameService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
