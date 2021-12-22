package com.polyu.zwq.HeartBeatTest;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

public class MyServerHandler extends ChannelInboundHandlerAdapter {
    /**
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            //将 evt 向下转型
            IdleStateEvent event= (IdleStateEvent) evt;
            String eventType=null;
            /**
             * 根据不同事件做相应的处理
             */
            switch (event.state()){
                case READER_IDLE:
                    eventType="reader idle!";
                    break;

                case WRITER_IDLE:
                    eventType = "writer idle!";
                    break;

                case ALL_IDLE:
                    eventType="all idle!";
                    break;
            }
            System.out.println("[client] "+ctx.channel().remoteAddress()+"---Timeout event----"+eventType);

        }

    }
}
