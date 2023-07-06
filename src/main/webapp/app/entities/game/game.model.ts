export interface IGame {
  id: number;
  title?: string | null;
}

export type NewGame = Omit<IGame, 'id'> & { id: null };
