package com.smicc.CreditShare;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class AutoFolderDeletion {

    private static final Logger logger = LoggerFactory.getLogger(AutoFolderDeletion.class);

    public static void main(String[] args) {
        logger.info("Starting delete utility");

        Configuration config = ConfigurationLoader.loadConfiguration();
        FolderProcessor folderProcessor = new FolderProcessor(config);

        try {
            folderProcessor.processFolders();
        } catch (IOException e) {
            logger.error("Error during file and folder processing", e);
        }

    }
}
