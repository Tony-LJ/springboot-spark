package com.turing.bigdata.service.impl;

import com.jcraft.jsch.*;
import com.turing.bigdata.service.SftpService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@Service
public class SftpServiceImpl implements SftpService {

    private static String host;
    @Value("${sftp_ubantu.host}")
    public void setHost(String host) {
        SftpServiceImpl.host = host;
    }

    private static Integer port;
    @Value("${sftp_ubantu.port}")
    public void setPort(Integer port) {
        SftpServiceImpl.port = port;
    }

    private static String username;
    @Value("${sftp_ubantu.username}")
    public void setUsername(String username) {
        SftpServiceImpl.username = username;
    }

    private static String password;
    @Value("${sftp_ubantu.password}")
    public void setPassword(String password) {
        SftpServiceImpl.password = password;
    }

    private static String remoteDir;
    @Value("${sftp_ubantu.remoteDir}")
    public void setRemoteDir(String remoteDir) {
        SftpServiceImpl.remoteDir = remoteDir;
    }

    @Override
    public void uploadFile(InputStream inputStream,
                           String fileName) throws IOException, JSchException, SftpException {
        Session session = null;
        Channel channel = null;
        JSch jsch = new JSch();
        try {
            //创建会话
            session = jsch.getSession(username, host, port != null ? port : 22);
            //设置密码
            //设置密码
            session.setPassword(password);
            //设置第一次登陆的时候提示，可选值：(ask | yes | no)
            session.setConfig("StrictHostKeyChecking", "no");
            //设置连接超时时间
            session.connect(30000);
            //创建sftp通信通道
            channel = session.openChannel("sftp");
            channel.connect(1000);
            //创建sftp客户端
            ChannelSftp sftp = (ChannelSftp) channel;
            //进入远程目录
            sftp.cd(remoteDir);
            //上传文件
            sftp.put(inputStream, fileName);
            inputStream.close();
        } finally {
            //关流操作
            if (session != null) {
                session.disconnect();
            }
            if (channel != null) {
                channel.disconnect();
            }
        }
    }

    @Override
    public List<String> listFiles(String host,
                                  int port,
                                  String username,
                                  String password, String remoteDir) {
        List<String> files = new ArrayList<>();
        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(username, host, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            session.connect();

            channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;

            // 进入指定目录
            channelSftp.cd("/opt/project/springboot");
            Vector fileList = channelSftp.ls("/opt/project/springboot"); // 获取当前目录下的文件和文件夹列表
            for (Object fileObj : fileList) {
                ChannelSftp.LsEntry entry = (ChannelSftp.LsEntry) fileObj;
                if (!entry.getAttrs().isDir()) { // 过滤出文件，排除目录
                    files.add(entry.getFilename());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channelSftp != null) {
                channelSftp.exit();
            }
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }

        return files;
    }
}
