package org.tjorven.tictactoeserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TicTacToeServer {
    private GameLogic game;
    private List<Socket> clients = new ArrayList<>();
    public TicTacToeServer(){
        game = new GameLogic();
    }

    public void start(int port){
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server is running");
            while (true){
                Socket clientSocket = serverSocket.accept();
                clients.add(clientSocket);
                handle(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handle(Socket clientSocket){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine = reader.readLine();

            String[] cell = inputLine.split(",");
            String respond = "";
            String cmd = cell[0];
            switch (cmd){
                case "move":
                    int param1 = Integer.parseInt(cell[1]);
                    int param2 = Integer.parseInt(cell[2]);
                    respond = handleMove(param1, param2);
                    break;
                default:
                    respond = "invalid Command";
                    break;
            }

            for(Socket client: clients){
                PrintWriter writer = new PrintWriter(client.getOutputStream());
                writer.println(respond);
                writer.flush();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String handleMove(int row, int column){
        String answer;
        if(game.makeMove(column, row)){
            if(game.checkWin()){
                int line = game.getLine();
                answer = "move,won," + column +"," + row + "," + game.getWinner() + "," + line;
            }else{
                answer = "move,notWon," + column +"," + row;
            }
        }else{
            answer = "invalid";
        }
        return answer;

    }
}
