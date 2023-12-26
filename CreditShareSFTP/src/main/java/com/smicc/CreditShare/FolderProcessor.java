package com.smicc.CreditShare;

import com.jcraft.jsch.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class FolderProcessor {

    private Configuration config;
    private static final Logger logger = LoggerFactory.getLogger(FolderProcessor.class);

    public FolderProcessor(Configuration config) {
        this.config = config;
    }

    public void processFolders() throws IOException {
        logger.info("Inside processFolders method");
        FolderHandler folderHandler = new FolderHandler(config);

        File tmpFile = new File(config.getTempPath());
        if (!tmpFile.exists()) {
            Files.createDirectories(Paths.get(config.getTempPath()));
        }

        folderHandler.deleteFolderFromTemp(new File(config.getTempPath()));

        for (String folderName : config.getFolderNames()) {
            logger.info("Folder Name: {}", folderName);
            logger.info("Root path: {}", config.getRootPath());
            Path folderPath = Paths.get(config.getRootPath(), folderName);

            // SFTP Configuration
            String sftpUsername = "yourSFTPUsername";
            String sftpPassword = "yourSFTPPassword";
            String sftpHostname = "fccfs02"; // Replace with your SFTP server hostname or IP address
            int sftpPort = 22; // Default SFTP port

            try {
                SftpUtil sftpUtil = new SftpUtil(sftpUsername, sftpPassword, sftpHostname, sftpPort);
                sftpUtil.connect();

                // Handle the folder using SFTP
                sftpUtil.handleFolder(folderPath.toString(), config.getTempPath());

                sftpUtil.disconnect();
            } catch (JSchException | SftpException e) {
                logger.error("Error during SFTP operations", e);
            }
        }
    }
}
