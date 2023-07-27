package org.tjorven.tictactoeserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TicTacToeServer {
    private GameLogic game;
    private List<Socket> clients = new ArrayList<>();
    int port;
    public TicTacToeServer(int port){
        game = new GameLogic();
        this.port = port;
    }

    public void start(){
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel channel) throws Exception{
                    channel.pipeline().addLast(new TicTacToeServerHandler());
                }
            }).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.bind(port).sync();

            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
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
