package com.turing.bigdata.service;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public interface SftpService {

    public void uploadFile(InputStream inputStream, String fileName) throws IOException, JSchException, SftpException;

    public List<String> listFiles(String host, int port, String username, String password, String remoteDir);

}
