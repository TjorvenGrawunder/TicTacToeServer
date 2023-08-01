package org.tjorven.tictactoeserver;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TicTacToeServerHandler extends ChannelInboundHandlerAdapter {

    TicTacToeServer server;

    public TicTacToeServerHandler(TicTacToeServer server) {
        super();
        this.server = server;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        server.addClientChannel(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        server.removeClientChannel(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        //ctx.writeAndFlush(msg);
        server.getClients().writeAndFlush(msg);
        System.out.println(msg.toString());
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

}
