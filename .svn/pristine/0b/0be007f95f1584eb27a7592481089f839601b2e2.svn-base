package com.geberl.gcodesender.firmware;

import com.geberl.gcodesender.IController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.JOptionPane;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Static utils for handling firmware settings
 *
 * @author Joacim Breiler
 */
public class FirmwareSettingUtils {
    private static final Logger LOGGER = Logger.getLogger(FirmwareSettingUtils.class.getName());

    /**
     * Imports firmware settings from a settings file. Displays a popup with a question if the file should be imported.
     *
     * @param settingsFile     the file containing a JSON with {@link FirmwareSettingsFile}
     * @param firmwareSettings the firmware settings to update
     */
    public static void importSettings(File settingsFile, IFirmwareSettings firmwareSettings) {
        try {
            FirmwareSettingsFile firmwareSettingsFile = readSettings(settingsFile);
            String message = "Do you want to import this settings file?" + "\n\n  " +
                    "Name: " + firmwareSettingsFile.getName() + "\n  " +
                    "Date: " + firmwareSettingsFile.getDate() + "\n  " +
                    "Created by: " + firmwareSettingsFile.getCreatedBy() + "\n  " +
                    "Firmware: " + firmwareSettingsFile.getFirmwareName();

            int result = JOptionPane.showConfirmDialog(new Frame(), message, "Import settings file", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                firmwareSettings.setSettings(firmwareSettingsFile.getSettings());
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Couldn't open the settings file " + settingsFile.getAbsolutePath(), e);
        } catch (FirmwareSettingsException e) {
            LOGGER.log(Level.SEVERE, "Couldn't update all settings", e);
        }
    }

    /**
     * Exports the current firmware settings to a file. The file will be in JSON format with
     * {@link FirmwareSettingsFile}
     *
     * @param settingsFile the file to write the settings to.
     * @param controller   the controller to get the firmware settings from
     */
    public static void exportSettings(final File settingsFile, IController controller) {
        File file = settingsFile;
        if (!StringUtils.endsWith(settingsFile.getName(), ".settings")) {
            file = new File(settingsFile.getAbsolutePath() + ".settings");
        }

        FirmwareSettingsFile firmwareSettingsFile = new FirmwareSettingsFileBuilder()
                .setCreatedBy(System.getProperty("user.name"))
                .setDate(LocalDate.now().format(DateTimeFormatter.ISO_DATE))
                .setFirmwareName(controller.getFirmwareVersion())
                .setName("Exported settings")
                .setSettings(controller.getFirmwareSettings().getAllSettings())
                .build();

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        try {
            String json = gson.toJson(firmwareSettingsFile);
            FileUtils.writeStringToFile(file, json);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Couldn't write the settings file " + settingsFile.getAbsolutePath(), e);
        }
    }

    /**
     * Reads a file and parses it to a firmware settings file
     *
     * @param settingsFile the file containing a JSON with {@link FirmwareSettingsFile}
     * @return the firmware settings together with its meta data
     * @throws IOException if the settings file couldn't be read or parsed
     */
    public static FirmwareSettingsFile readSettings(File settingsFile) throws IOException {
        String json = FileUtils.readFileToString(settingsFile);
        Gson gson = new Gson();
        return gson.fromJson(json, FirmwareSettingsFile.class);
    }
}
