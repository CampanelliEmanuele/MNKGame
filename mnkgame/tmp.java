public class tmp {

  for (int pos = 0; pos < MC.length; pos += 2) {
    int i_MC = MC[pos].i;   // Coordinata i (riga) della cella in analisi
    int j_MC = MC[pos].j;   // Coordinata j (colonna) della cella in analisi

    // Controllo set orizzontale
    for (int c = 0; c < j_colonne; c++) {     // Controllo della i-esima righe (da sx verso dx)
      if (tabellaGioco[i_MC][c].state == playerState) playerCell (vars);
      if (tabellaGioco[i_MC][c].state == FREE) freeCell (vars);
      if (tabellaGioco[i_MC][c].state != playerState && tabellaGioco[i_MC][c].state != FREE) { enemyCell (vars); noEnemy = false; }
    }
    editVar (vars);
    in_noEnemy = true;

    // Controllo riga in verticale
    for (int r = 0; r < i_righe; r++) {     // Controllo della j-esima colonna (dall'alto verso il basso)
      if (tabellaGioco[r][j_MC].state == playerState) playerCell (vars);
      if (tabellaGioco[r][j_MC].state == FREE) freeCell (vars);
      if (tabellaGioco[r][j_MC].state != playerState && tabellaGioco[r][j_MC].state != FREE) enemyCell (vars);
    }
    editVar (vars);
    in_noEnemy = true;


    // Controllo diagonale
    if (M_righe >= k && N_colonne >= k) {   // Se è possibile la creazione di diagonali
      System.out.println("Controllo diagonale eseguito.");
      int tmp = 1;

      // Scorrimento verso in alto a sx
      for (tmp; i_MC - tmp > 0 && j_MC - tmp > 0; tmp++)    // Si incrementa tmp fino a quando sottratto alle coordinate i ed j non ci si ritrova in (i - tmp, j - tmp = 0)
      // while (i_righe - tmp > 0 && j_colonne - tmp > 0) { tmp++; }
      int start_i = i_MC - tmp;  // DA CONTROLLARE SE SERVE + 1 !!!!!!!!!!
      int start_j = j_MC - tmp;

      // Discesa fino in basso a dx
      for (tmp = 0; start_i + tmp <= start_i + vars[k] && start_j + tmp <= start_j + vars[k]; tmp++) {
        if (tabellaGioco[start_i + tmp][start_j + tmp].state == playerState) playerCell (vars);
        if (tabellaGioco[i_righe + tmp][j_colonne + tmp].state == FREE) freeCell(vars);
        if (tabellaGioco[i_righe + tmp][j_colonne + tmp].state != playerState && tabellaGioco[i_righe + tmp][j_colonne + tmp].state != FREE) enemyCell(vars);
      }
      editVar (vars);
      noEnemy = true;

      // Scorrimento verso in alto a dx
      for (tmp = 1; i_MC - tmp < i_righe && j_MC + tmp < j_colonne; tmp++)    // Controllare il reset di tmp  !!!!!!!!!!!!!!!!!!!!
      // tmp = 1; while (i_righe - tmp < i_righe && j_colonne + tmp < j_colonne) { tmp++; }
      start_i = i_MC - tmp;  // DA CONTROLLARE SE SERVE + 1 !!!!!!!!!!
      start_j = j_MC - tmp;


      // Discesa fino in basso a sx
      for (int tmp = 1; start_i + tmp <= start_i + vars[k] && start_j - tmp <= 0; tmp++) {
        if (tabellaGioco[start_i + tmp][start_j - tmp].state == playerState) playerCell (vars);
        if (tabellaGioco[start_i + tmp][start_j - tmp].state == FREE) freeCell (vars);
        if (tabellaGioco[start_i + tmp][start_j - tmp].state != playerState && tabellaGioco[i_righe + tmp][j_colonne - tmp].state != FREE) enemyCell (vars):
      }
      editVar (vars);
      noEnemy = true;

      } else {
        System.out.println("controllo diagonale Impossibile.");
    }

  }

  }

vars
pos --> i_MC j_MC
(in_noEnemy)

public static void diagonalSet (int[] in_vars, boolean in_player)
