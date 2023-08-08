package org.tjorven.tictactoeserver;

public class GameLogic {
    private int[][] currentGameState = new int[3][3];
    private int currentPlayer;

    private int winner = 0;
    private boolean won = false;
    private int newPlayer = 1;
    private int turns = 0;

    private int line = -1;

    public GameLogic(){
        init();
    }

    private void init(){
        for(int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                currentGameState[i][j] = 0;
            }
        }
        currentPlayer = 1;
    }

    public void nextPlayer(){
        if(winner == 0) {
            if (currentPlayer == 1) {
                currentPlayer = 2;
            } else if (currentPlayer == 2) {
                currentPlayer = 1;
            } else {
                throw new RuntimeException("Kein Spieler ist momentan am Zug!");
            }
        }else{
            printGameState();
        }
    }

    public boolean makeMove(int row, int col){
        if(!won) {
            if (currentGameState[row][col] == 0) {
                currentGameState[row][col] = currentPlayer;
                turns++;
                nextPlayer();
                return true;
            }else {
                return false;
            }
        }
        return false;
    }

    public boolean checkWin(){
        // Überprüfe Zeilen und Spalten
        for (int i = 0; i < 3; i++) {
            if (currentGameState[i][0] == currentGameState[i][1] && currentGameState[i][1] == currentGameState[i][2] && currentGameState[i][0] != 0) {
                winner = currentGameState[i][0]; // Zeile i gewinnt
                line = i;
                won = true;
                return true;
            }
            if (currentGameState[0][i] == currentGameState[1][i] && currentGameState[1][i] == currentGameState[2][i] && currentGameState[0][i] != 0) {
                winner = currentGameState[0][i]; // Spalte i gewinnt
                line = i + 3;
                won = true;
                return true;
            }
        }

        // Überprüfe Diagonalen
        if (currentGameState[0][0] == currentGameState[1][1] && currentGameState[1][1] == currentGameState[2][2] && currentGameState[0][0] != 0) {
            winner = currentGameState[0][0]; // Hauptdiagonale gewinnt
            line = 6;
            won = true;
            return true;
        }
        if (currentGameState[0][2] == currentGameState[1][1] && currentGameState[1][1] == currentGameState[2][0] && currentGameState[1][1] != 0) {
            winner = currentGameState[0][2]; // Nebendiagonale gewinnt
            line = 7;
            won = true;
            return true;
        }
        if(turns == 9){
            won = true;
            return true;
        }
        return false;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isWon(){
        return this.won;
    }

    public int getWinner() {
        return winner;
    }

    public int getLine() {
        return line;
    }

    public int getNewPlayer() {
        return newPlayer;
    }

    public void setNewPlayer(int newPlayer) {
        this.newPlayer = newPlayer;
    }

    private void printGameState(){
        for (int i = 0; i < 3; i++){
            System.out.println(currentGameState[i][0] + "," + currentGameState[i][1] + "," + currentGameState[i][2]);
        }
    }
}
