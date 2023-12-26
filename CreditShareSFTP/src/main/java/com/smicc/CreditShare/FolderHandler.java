package com.smicc.CreditShare;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FolderHandler {

	private int count = 0;
	private Configuration config;
	private static final Logger logger = LoggerFactory.getLogger(FolderHandler.class);

	public FolderHandler(Configuration config) {
		this.config = config;
	}

	public void handleFolder(Path folderPath) throws IOException {
		// logger.info("Inside handleFolder method");
		logger.info("Inside Folder");
		File files = new File(folderPath.toString());
		iterateFilesInFolder(files.listFiles());
		logger.info("Outside the Folder");
	}

	private void iterateFilesInFolder(File[] listOfFiles) throws IOException {
		count = count + 1;
		// logger.info("Iteration Count: {}", count);
		if (listOfFiles != null) {
			for (File file : listOfFiles) {
				if (file.isDirectory()) {
					logger.info("Directory found in {}", file.getAbsolutePath());
					iterateFilesInFolder(file.listFiles());
				} else {
					logger.info("File found {}", file.getName());
					checkModifiedFiles(file);
				}
			}
		} else {
			logger.info("No files are found in the folder");
		}
	}

	private void checkModifiedFiles(File file) throws IOException {
		Date lastModifiedDate = new Date(Files.getLastModifiedTime(file.toPath()).toMillis());
		logger.info("lastModified date: {} for file {}", lastModifiedDate, file.getName());
		if (isOlderThanThreshold(lastModifiedDate)) {
			moveFolderToTemp(file.toPath(), Paths.get(config.getTempPath()));
		}
	}

	private void moveFolderToTemp(Path source, Path destination) {
		try {
			logger.info("Source path: {}", source.toString());
			String resolvedPathString = String.valueOf(destination.resolve(destination.relativize(source).normalize()));
			String finalDestinationPath = resolvedPathString.replace("\\..\\", "\\");
			Path finalMovePath = Paths.get(finalDestinationPath);
			logger.info("Final Destination Path: {}", finalMovePath);
			logger.info("Creating Directory in Temp folder");
			Path path1 = Files.createDirectories(finalMovePath.getParent());
			logger.info("Directories created as per path: {}", path1.toString());
			logger.info("Moving source file to temp folder");
			Files.move(source, Paths.get(finalMovePath.toString()));
			logger.info("File moved to temp folder: {}", finalMovePath.toString());

		} catch (IOException e) {
			logger.error("Error moving folder to temp folder", e);
		}
	}

	public void deleteFolderFromTemp(File folder) {
		logger.info("Inside deleteFolderFormTemp method");
		try {
			Files.walk(folder.toPath()).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
			logger.info("Folder deleted from temp folder: {}", folder.toPath().toString());
		} catch (IOException e) {
			logger.error("Error deleting folder from temp folder", e);
		}
	}

	private boolean isOlderThanThreshold(Date lastModifiedDate) {
		logger.info("Inside isOlderThanThreshold Method");
		long currentMillis = System.currentTimeMillis();
		long thresholdMillis = currentMillis - TimeUnit.DAYS.toMillis(config.getDeletionFrequencyDays());
		Date deletionThreshold = new Date(thresholdMillis);
		logger.info("Deletion threshold date: {}", deletionThreshold.toString());
		return lastModifiedDate.before(deletionThreshold);
		//return true;
	}
}
