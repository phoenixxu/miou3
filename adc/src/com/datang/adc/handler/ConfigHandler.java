package com.datang.adc.handler;

import io.netty.channel.ChannelHandlerContext;

import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.datang.adc.IMsg;
import com.datang.adc.testplan.TestPlanVerParser;
import com.datang.adc.util.SDCardUtils;
import com.datang.adc.util.StringUtils;

/**
 * Created by dingzhongchang on 13-6-6.
 */
class ConfigHandler implements IHandler {
	// private static final Logger L = Logger.getLogger(ConfigHandler.class);
	static java.text.DateFormat format = new java.text.SimpleDateFormat(
			"yyyyMMddhhmmss",Locale.CHINA);
	static ExecutorService Plan = Executors
			.newSingleThreadExecutor(new ThreadFactory() {

				@Override
				public Thread newThread(Runnable r) {
					Thread t = new Thread(r);
					t.setDaemon(true);
					t.setName("TESTPLAN");
					return t;
				}

			});
	private final MsgHandler msgHandler;
	private final StringBuilder testPlan = new StringBuilder();

	public ConfigHandler(MsgHandler msgHandler) {
		this.msgHandler = msgHandler;
	}

	/**
	 * Configuration request 配置请求 [Request] Command=Config Session=Session ID
	 * <p/>
	 * Configuration response 配置响应 [Response] Command=Config Session=Session ID
	 * Cver=config version #平台的最新配置版本 Result=AC/NAC Code=A Code
	 * #如果结果为NAC时的处理结果代码 PacketCount=N #将配置更新包拆分成N个包下发 PacketNo=n
	 * #当前是第n个包（n=1,2,……）
	 * <p/>
	 * Configuration command 配置指令 Command=Config Session=Session ID Cver=config
	 * version #平台的最新配置版本 PacketCount=N #将配置更新包拆分成N个包下发 PacketNo=n #当前是第n个包
	 * （n=1,2,……）
	 */

	@Override
	public void handle(ChannelHandlerContext ctx, IMsg msg, String session) {
		final Map<String, Object> map = msg.toMap();
		String type = map.get("type").toString();
		if (!StringUtils.isEmpty(type) && type.equalsIgnoreCase("[Response]")) { // 配置响应
			if (map.get("result").toString().equalsIgnoreCase("NAC"))
				return;
		} else {// 配置指令

		}
		String packetno = map.get("packetno").toString();
		if ("1".equals(packetno)) {
			testPlan.delete(0, testPlan.length());
		}
		testPlan.append(new String((byte[]) map.get("data")));
		if (packetno.equals(map.get("packetcount"))) {
			Plan.submit(new Runnable() {
				@Override
				public void run() {
					String cver = map.get("cver").toString();
					if (cver == null) {
						cver = TestPlanVerParser.getPlanVer(testPlan.toString());
					}
					if (cver != null) {
						if (msgHandler != null)
							msgHandler.setCver(cver);
						SDCardUtils.writeReadOnly(SDCardUtils.getSystemPath(),
								format.format(new Date()) + cver + ".xml",
								testPlan.toString().getBytes());
					} else {
						// L.error("Not Found Version in testplan ：" +
						// testPlan);
					}
				}
			});
		}

	}
}
