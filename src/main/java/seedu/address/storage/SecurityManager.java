
package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.IOException;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.PasswordAcceptedEvent;
import seedu.address.commons.util.FileUtil;

//@@author syy94
/**
 * Accesses and edits the password.
 */
public class SecurityManager {
    private static String path = "data/pass";

    public static void setPath(String path) {
        SecurityManager.path = path;
    }

    /**
     * Saves password
     */
    public static void savePass(String pass) throws IOException {
        requireNonNull(pass);
        final File passFile = new File(path);

        final String trimmedPass = pass.trim();
        if (trimmedPass.length() != 0) {
            FileUtil.createIfMissing(passFile);
            FileUtil.writeToFile(passFile, trimmedPass);
        }
    }

    /**
     * Checks if given password is correct and posts and event to enable the application features.
     */
    public static boolean unlock(String pass) throws IOException {
        boolean isUnlocked = checkPass(pass);
        if (isUnlocked) {
            EventsCenter.getInstance().post(new PasswordAcceptedEvent());
        }
        return isUnlocked;
    }

    /**
     * Checks if given password is correct.
     */
    public static boolean checkPass(String pass) throws IOException {
        return getPass().equals(pass);
    }

    /**
     * removes the need for password on application start.
     */
    public static void removePass() {
        final File passFile = new File(path);
        if (FileUtil.isFileExists(passFile)) {
            passFile.delete();
        }
    }

    /**
     * Checks if there is a password.
     */
    public static boolean passExists() {
        final File passFile = new File(path);

        return FileUtil.isFileExists(passFile);
    }

    /**
     * Retrieves the Password.
     *
     * @throws IOException
     */
    private static String getPass() throws IOException {
        final File file = new File(path);
        return FileUtil.readFromFile(file);
    }
}
