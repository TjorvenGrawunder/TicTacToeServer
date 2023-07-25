package org.tjorven.tictactoeserver;

public class ServerStart {

    public static void main(String[] args){
        TicTacToeServer server = new TicTacToeServer();
        server.start(8080);
    }
}
