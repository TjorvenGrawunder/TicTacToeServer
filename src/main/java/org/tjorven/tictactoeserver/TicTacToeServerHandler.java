package org.tjorven.tictactoeserver;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TicTacToeServerHandler extends ChannelInboundHandlerAdapter {

    TicTacToeServer server;
    GameLogic game;

    public TicTacToeServerHandler(TicTacToeServer server) {
        super();
        this.server = server;
        game = server.getGame();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        server.addClientChannel(ctx.channel());
        int newPlayer = game.getNewPlayer();
        ctx.writeAndFlush("setPlayer," + newPlayer);
        game.setNewPlayer(newPlayer + 1);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        server.removeClientChannel(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        //ctx.writeAndFlush(msg);
        if(game.isWon()){
            ((ByteBuf) msg).release();
        }else {
            String respond = parseMessage((String) msg);
            server.getClients().writeAndFlush(respond);
        }

        //System.out.println(msg);
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

    private String parseMessage(String msg){
        String answer = "";
        String[] msgParts = msg.split(",");

        System.out.println(msgParts[1] + "," + msgParts[2]);

        switch (msgParts[0]){
            case "clickedOn":

                if (Integer.parseInt(msgParts[3]) == game.getCurrentPlayer()) {
                    game.makeMove(Integer.parseInt(msgParts[1]), Integer.parseInt(msgParts[2]));
                    boolean isWon = game.checkWin();
                    int nextPlayer = game.getCurrentPlayer();
                    int winner = 0;
                    int line = -1;
                    if (isWon) {
                        winner = game.getWinner();
                        line = game.getLine();
                    }
                    answer = "draw," + msgParts[1] + "," + msgParts[2] + "," + isWon + "," + nextPlayer + "," + winner + "," + line;
                }
                break;
            case "quit":
                //TO-DO
                System.out.println("Player quit");
                break;
            default:
                break;
        }

        return answer;
    }

}
