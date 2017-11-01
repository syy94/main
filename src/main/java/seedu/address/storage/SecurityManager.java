package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.IOException;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.PasswordAcceptedEvent;
import seedu.address.commons.util.FileUtil;

public class SecurityManager {
    private static String path = "data/pass";

    public static void setPath(String path) {
        SecurityManager.path = path;
    }

    public static void savePass(String pass) throws IOException {
        requireNonNull(pass);
        final File passFile = new File(path);

        final String trimmedPass = pass.trim();
        if (trimmedPass.length() != 0) {
            FileUtil.createIfMissing(passFile);
            FileUtil.writeToFile(passFile, trimmedPass);
        }
    }

    public static boolean unlock(String pass) throws IOException {
        boolean isUnlocked = checkPass(pass);
        if (isUnlocked) {
            EventsCenter.getInstance().post(new PasswordAcceptedEvent());
        }
        return isUnlocked;
    }

    public static boolean checkPass(String pass) throws IOException {
        return getPass().equals(pass);
    }

    public static void removePass() {
        final File passFile = new File(path);
        if (FileUtil.isFileExists(passFile)) {
            passFile.delete();
        }
    }

    public static boolean passExists() {
        final File passFile = new File(path);

        return FileUtil.isFileExists(passFile);
    }

    private static String getPass() throws IOException {
        final File file = new File(path);
        return FileUtil.readFromFile(file);
    }
}