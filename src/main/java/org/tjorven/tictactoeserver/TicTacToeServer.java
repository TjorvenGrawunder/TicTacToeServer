package org.tjorven.tictactoeserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TicTacToeServer {
    private GameLogic game;
    public TicTacToeServer(){
        game = new GameLogic();
    }

    public void start(int port){
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server is running");
            while (true){
                Socket clientSocket = serverSocket.accept();
                handle(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void handle(Socket clientSocket){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
            String inputLine = reader.readLine();

            String[] cell = inputLine.split(",");

            int row = Integer.parseInt(cell[0]);
            int col = Integer.parseInt(cell[1]);

            game.makeMove(row, col);
            int winner = game.getWinner();
            if(winner != 0){
                writer.println("Winner: " + winner);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
