package com.example.framework.utils;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.framework.exceptions.FrameworkException;

/**
 * Video recording utility for capturing test execution
 * Uses FFmpeg to record browser window
 */
public class VideoRecorder {
    private static final Logger logger = LogManager.getLogger(VideoRecorder.class);
    private static final String RECORDING_PATH = "target/videos";
    private Process recordingProcess;
    private String recordingFile;
    private boolean isRecording = false;

    /**
     * Start video recording
     * Requires FFmpeg to be installed and in PATH
     */
    public void startRecording(String testName) {
        try {
            // Create video directory
            new File(RECORDING_PATH).mkdirs();

            // Generate recording file path
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            String timestamp = formatter.format(LocalDateTime.now());
            recordingFile = RECORDING_PATH + File.separator + testName + "_" + timestamp + ".mp4";

            logger.info("Starting video recording to: {}", recordingFile);

            // Note: Actual FFmpeg command would be system-specific
            // This is a placeholder for the recording logic
            // Uncomment and modify for your system:
            /*
            String[] command = {
                "ffmpeg",
                "-f", "gdigrab",  // Windows screen capture
                "-framerate", "30",
                "-i", "desktop",
                "-c:v", "mpeg4",
                "-q:v", "5",
                recordingFile
            };
            recordingProcess = Runtime.getRuntime().exec(command);
            */

            isRecording = true;
            logger.info("Video recording started for test: {}", testName);
        } catch (Exception e) {
            logger.error("Failed to start video recording", e);
            // Don't fail test if recording fails - continue without recording
        }
    }

    /**
     * Stop video recording
     */
    public String stopRecording() {
        if (isRecording && recordingProcess != null) {
            try {
                recordingProcess.destroy();
                recordingProcess.waitFor();
                isRecording = false;
                logger.info("Video recording stopped. File: {}", recordingFile);
                return recordingFile;
            } catch (Exception e) {
                logger.error("Error stopping video recording", e);
            }
        }
        return null;
    }

    /**
     * Check if recording is active
     */
    public boolean isRecording() {
        return isRecording;
    }

    /**
     * Get current recording file path
     */
    public String getRecordingFile() {
        return recordingFile;
    }

    /**
     * Verify FFmpeg is available
     */
    public static boolean isFfmpegAvailable() {
        try {
            ProcessBuilder pb = new ProcessBuilder("ffmpeg", "-version");
            pb.redirectErrorStream(true);
            Process process = pb.start();
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (Exception e) {
            logger.warn("FFmpeg not available: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Delete recording file
     */
    public void deleteRecording(String filePath) {
        try {
            if (filePath != null) {
                File file = new File(filePath);
                if (file.exists() && file.delete()) {
                    logger.info("Video recording deleted: {}", filePath);
                }
            }
        } catch (Exception e) {
            logger.error("Failed to delete video recording", e);
        }
    }
}
