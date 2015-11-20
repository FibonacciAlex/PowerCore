package core.game.activatecodeTest;




public class HttpRequestHandler /*extends SimpleChannelUpstreamHandler*/{
//
//	@Override
//	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
//			throws Exception {
//		// TODO Auto-generated method stub
//		super.messageReceived(ctx, e);
//		HttpResponse response = (HttpResponse) e.getMessage();
////		System.out.println("status:" + response.getStatus());
////		System.out.println("version:" + response.getProtocolVersion());
//		
//		
////		if(!response.getHeaderNames().isEmpty()){
////			
////			for (String header : response.getHeaderNames()) {
////				
////				for (String value : response.getHeaders(header)) {
////					System.out.println("header" + header + ", value:" + value);
////				}
////			}
////		}
//		
//		ChannelBuffer content = response.getContent();
//		String resultStr = "";
//		if(content.readable()){
//			System.out.println("CONTENT {");
//			resultStr = content.toString(CharsetUtil.UTF_8);
//			System.out.println(resultStr);
//            System.out.println("} END OF CONTENT");
//            
//            //接收到返回数据后  send reconfirm msg
//            KRewardDataTemplate template = JSON.parseObject(resultStr, KRewardDataTemplate.class);
//            
//
//            if(template.isResult()){
//            	KActivateCodeEnterManager.getInstance().submitResponseTask(template);
//            }else{
//            	System.out.println(template.getMsg());
//            }
//		}
//		System.out.println("------------------------------------------------------------------------------");
//		
//	}
//
//	@Override
//	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
//			throws Exception {
//		// TODO Auto-generated method stub
//		super.exceptionCaught(ctx, e);
//		e.getCause().printStackTrace();
//        e.getChannel().close();
//	}

	
	
}
