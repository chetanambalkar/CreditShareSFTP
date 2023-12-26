package com.smicc.CreditShare;


import com.jcraft.jsch.*;

import java.io.InputStream;
import java.util.Properties;

public class SftpUtil {

    private String username;
    private String password;
    private String hostname;
    private int port;
    private Session session;
    private ChannelSftp channelSftp;

    public SftpUtil(String username, String password, String hostname, int port) {
        this.username = username;
        this.password = password;
        this.hostname = hostname;
        this.port = port;
    }

    public void connect() throws JSchException {
        JSch jsch = new JSch();

        session = jsch.getSession(username, hostname, port);
        session.setPassword(password);

        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);

        session.connect();

        Channel channel = session.openChannel("sftp");
        channel.connect();

        channelSftp = (ChannelSftp) channel;
    }

    public void disconnect() {
        if (channelSftp != null) {
            channelSftp.disconnect();
        }
        if (session != null) {
            session.disconnect();
        }
    }

    public void handleFolder(String sourcePath, String destinationPath) throws SftpException {
        channelSftp.put(sourcePath, destinationPath);
    }
}
