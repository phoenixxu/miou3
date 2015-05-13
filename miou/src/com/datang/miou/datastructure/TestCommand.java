package com.datang.miou.datastructure;

import java.io.Serializable;

public class TestCommand implements Serializable, Cloneable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2325410432458371801L;

	private String mId;
	
	private String mCallNumber;
	private String mRandomCall;
	private String mDuration;
	private String mInterval;
	private String mMaxTime;
	private String mTestMos;
	private String mCallMosServer;
	private String mMosLimit;
	
	private String mWaitTimes;
	
	private String mKeepTime;
	
	private String mApn;
	
	private String mIp;
	private String mPackageSize;
	private String mTimeOut;
	
	private String mUrl;
	private String mAgent;
	private String mConnectionMode;
	private String mGateWay;
	private String mPort;
	
	private String mServerCenterAddress;
	private String mDestination;
	private String mMode;
	private String mText;
	private String mReport;
	private String mContent;
	
	private String mAccount;
	private String mPassword;
	private String mServerAddress;
	private String mSyncMsno;
	private String mMediaFileSize;
	
	private String mPTimeOut;
	
	private String mRemoteHost;
	private String mPassive;
	private String mBinary;
	private String mDownload;
	private String mRemoteFile;
	
	private String mVersion;
	private String mUserName;
	private String mRtp;
	private String mRtspHttpPort;
	private String mLocalRtpPort;
	private String mPreBufferLength;
	private String mRebufferLength;
	private String mPlayTime;
	private String mBufferLength;
	private String mBufferPlayThreshold;
	
	private String mMailServer;
	private String mDeleteMail;
	private String mPath;
	private String mSsl;
	
	private String mSender;
	private String mFrom;
	private String mTo;
	private String mFileSize;
	private String mSubject;
	private String mBody;
	private String mAddress;
	private String mAuthentication;
	private String mEncoding;
	private String mHtml;

	private String mRepeat;
	
	private String mProxy;
	private String mProxyType;
	
	public String getId() {
		return mId;
	}
	public void setId(String id) {
		mId = id;
	}
	public String getCallNumber() {
		return mCallNumber;
	}
	public void setCallNumber(String callNumber) {
		mCallNumber = callNumber;
	}
	public String getRandomCall() {
		return mRandomCall;
	}
	public void setRandomCall(String randomCall) {
		mRandomCall = randomCall;
	}
	public String getDuration() {
		return mDuration;
	}
	public void setDuration(String duration) {
		mDuration = duration;
	}
	public String getInterval() {
		return mInterval;
	}
	public void setInterval(String interval) {
		mInterval = interval;
	}
	public String getMaxTime() {
		return mMaxTime;
	}
	public void setMaxTime(String maxTime) {
		mMaxTime = maxTime;
	}
	public String getTestMos() {
		return mTestMos;
	}
	public void setTestMos(String testMos) {
		mTestMos = testMos;
	}
	public String getCallMosServer() {
		return mCallMosServer;
	}
	public void setCallMosServer(String callMosServer) {
		mCallMosServer = callMosServer;
	}
	public String getMosLimit() {
		return mMosLimit;
	}
	public void setMosLimit(String mosLimit) {
		mMosLimit = mosLimit;
	}
	public String getWaitTimes() {
		return mWaitTimes;
	}
	public void setWaitTimes(String waitTimes) {
		mWaitTimes = waitTimes;
	}
	public String getKeepTime() {
		return mKeepTime;
	}
	public void setKeepTime(String keepTime) {
		mKeepTime = keepTime;
	}
	public String getApn() {
		return mApn;
	}
	public void setApn(String apn) {
		mApn = apn;
	}
	public String getIp() {
		return mIp;
	}
	public void setIp(String ip) {
		mIp = ip;
	}
	public String getPackageSize() {
		return mPackageSize;
	}
	public void setPackageSize(String packageSize) {
		mPackageSize = packageSize;
	}
	public String getTimeOut() {
		return mTimeOut;
	}
	public void setTimeOut(String timeOut) {
		mTimeOut = timeOut;
	}
	public String getUrl() {
		return mUrl;
	}
	public void setUrl(String url) {
		mUrl = url;
	}
	public String getAgent() {
		return mAgent;
	}
	public void setAgent(String agent) {
		mAgent = agent;
	}
	public String getConnectionMode() {
		return mConnectionMode;
	}
	public void setConnectionMode(String connectionMode) {
		mConnectionMode = connectionMode;
	}
	public String getGateWay() {
		return mGateWay;
	}
	public void setGateWay(String gateWay) {
		mGateWay = gateWay;
	}
	public String getPort() {
		return mPort;
	}
	public void setPort(String port) {
		mPort = port;
	}
	public String getServerCenterAddress() {
		return mServerCenterAddress;
	}
	public void setServerCenterAddress(String serverCenterAddress) {
		mServerCenterAddress = serverCenterAddress;
	}
	public String getDestination() {
		return mDestination;
	}
	public void setDestination(String destination) {
		mDestination = destination;
	}
	public String getMode() {
		return mMode;
	}
	public void setMode(String mode) {
		mMode = mode;
	}
	public String getText() {
		return mText;
	}
	public void setText(String text) {
		mText = text;
	}
	public String getReport() {
		return mReport;
	}
	public void setReport(String report) {
		mReport = report;
	}
	public String getContent() {
		return mContent;
	}
	public void setContent(String content) {
		mContent = content;
	}
	public String getAccount() {
		return mAccount;
	}
	public void setAccount(String account) {
		mAccount = account;
	}
	public String getPassword() {
		return mPassword;
	}
	public void setPassword(String password) {
		mPassword = password;
	}
	public String getSyncMsno() {
		return mSyncMsno;
	}
	public void setSyncMsno(String syncMsno) {
		mSyncMsno = syncMsno;
	}
	public String getMediaFileSize() {
		return mMediaFileSize;
	}
	public void setMediaFileSize(String mediaFileSize) {
		mMediaFileSize = mediaFileSize;
	}
	public String getPTimeOut() {
		return mPTimeOut;
	}
	public void setPTimeOut(String pTimeOut) {
		mPTimeOut = pTimeOut;
	}
	public String getRemoteHost() {
		return mRemoteHost;
	}
	public void setRemoteHost(String remoteHost) {
		mRemoteHost = remoteHost;
	}
	public String getPassive() {
		return mPassive;
	}
	public void setPassive(String passive) {
		mPassive = passive;
	}
	public String getBinary() {
		return mBinary;
	}
	public void setBinary(String binary) {
		mBinary = binary;
	}
	public String getDownload() {
		return mDownload;
	}
	public void setDownload(String download) {
		mDownload = download;
	}
	public String getRemoteFile() {
		return mRemoteFile;
	}
	public void setRemoteFile(String remoteFile) {
		mRemoteFile = remoteFile;
	}
	public String getVersion() {
		return mVersion;
	}
	public void setVersion(String version) {
		mVersion = version;
	}
	public String getUserName() {
		return mUserName;
	}
	public void setUserName(String userName) {
		mUserName = userName;
	}
	public String getRtp() {
		return mRtp;
	}
	public void setRtp(String rtp) {
		mRtp = rtp;
	}
	public String getRtspHttpPort() {
		return mRtspHttpPort;
	}
	public void setRtspHttpPort(String rtspHttpPort) {
		mRtspHttpPort = rtspHttpPort;
	}
	public String getLocalRtpPort() {
		return mLocalRtpPort;
	}
	public void setLocalRtpPort(String localRtpPort) {
		mLocalRtpPort = localRtpPort;
	}
	public String getPreBufferLength() {
		return mPreBufferLength;
	}
	public void setPreBufferLength(String preBufferLength) {
		mPreBufferLength = preBufferLength;
	}
	public String getRebufferLength() {
		return mRebufferLength;
	}
	public void setRebufferLength(String rebufferLength) {
		mRebufferLength = rebufferLength;
	}
	public String getPlayTime() {
		return mPlayTime;
	}
	public void setPlayTime(String playTime) {
		mPlayTime = playTime;
	}
	public String getBufferLength() {
		return mBufferLength;
	}
	public void setBufferLength(String bufferLength) {
		mBufferLength = bufferLength;
	}
	public String getBufferPlayThreshold() {
		return mBufferPlayThreshold;
	}
	public void setBufferPlayThreshold(String bufferPlayThreshold) {
		mBufferPlayThreshold = bufferPlayThreshold;
	}
	public String getMailServer() {
		return mMailServer;
	}
	public void setMailServer(String mailServer) {
		mMailServer = mailServer;
	}
	public String getDeleteMail() {
		return mDeleteMail;
	}
	public void setDeleteMail(String deleteMail) {
		mDeleteMail = deleteMail;
	}
	public String getPath() {
		return mPath;
	}
	public void setPath(String path) {
		mPath = path;
	}
	public String getSsl() {
		return mSsl;
	}
	public void setSsl(String ssl) {
		mSsl = ssl;
	}
	public String getSender() {
		return mSender;
	}
	public void setSender(String sender) {
		mSender = sender;
	}
	public String getFrom() {
		return mFrom;
	}
	public void setFrom(String from) {
		mFrom = from;
	}
	public String getTo() {
		return mTo;
	}
	public void setTo(String to) {
		mTo = to;
	}
	public String getFileSize() {
		return mFileSize;
	}
	public void setFileSize(String fileSize) {
		mFileSize = fileSize;
	}
	public String getSubject() {
		return mSubject;
	}
	public void setSubject(String subject) {
		mSubject = subject;
	}
	public String getBody() {
		return mBody;
	}
	public void setBody(String body) {
		mBody = body;
	}
	public String getAddress() {
		return mAddress;
	}
	public void setAddress(String address) {
		mAddress = address;
	}
	public String getAuthentication() {
		return mAuthentication;
	}
	public void setAuthentication(String authentication) {
		mAuthentication = authentication;
	}
	public String getEncoding() {
		return mEncoding;
	}
	public void setEncoding(String encoding) {
		mEncoding = encoding;
	}
	public String getHtml() {
		return mHtml;
	}
	public void setHtml(String html) {
		mHtml = html;
	}
	public String getServerAddress() {
		return mServerAddress;
	}
	public void setServerAddress(String serverAddress) {
		mServerAddress = serverAddress;
	}
	
	@Override
	public String toString() {
		return "[" + this.mId + ", " + this.mDownload + ", " + this.mRepeat + "]";
	}
	
	public void setRepeat(String repeat) {
		// TODO Auto-generated method stub
		this.mRepeat = repeat;
	}
	public String getRepeat() {
		return this.mRepeat;
	}
	
	public Object clone() {
		Object o = null;
		try {
			o = (TestCommand) super.clone();
		} catch (CloneNotSupportedException e) {
			;
		}
		return o;
	}
	public String getProxy() {
		return mProxy;
	}
	public void setProxy(String proxy) {
		mProxy = proxy;
	}
	public String getProxyType() {
		return mProxyType;
	}
	public void setProxyType(String proxyType) {
		mProxyType = proxyType;
	}
}
