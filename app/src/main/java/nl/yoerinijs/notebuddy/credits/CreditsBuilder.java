package nl.yoerinijs.notebuddy.credits;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * A class for creating credits
 */
public class CreditsBuilder {

    private static final String LOG_TAG = "Credits Builder";

    /**
     * Credits getter
     * @return
     */
    public String getCredits(@NonNull Context context) {
        return createCredits(context);
    }

    /**
     * A method for creating the credits
     * @return
     */
    private String createCredits(@NonNull Context context) {
        StringBuilder sb = new StringBuilder();

        // General message
        sb.append("NoteBuddy is made by Yoeri Nijs and is released under the GNU General Public License v3 with gratitude.\n\n");
        sb.append("Source: https://github.com/YoeriNijs/NoteBuddy/\n");

        // Current version
        try {
            String noteBuddyVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            sb.append("Bug reports: https://github.com/YoeriNijs/NoteBuddy/issues\n");
            sb.append("Installed version: " + noteBuddyVersion + "\n\n");
        } catch (PackageManager.NameNotFoundException e) {
            // Log failure
            Log.d(LOG_TAG, "Cannot get version name");
        }

        // General thank you message
        sb.append("This program uses various sources in order to work properly. The following people and institutions will be acknowledged for their work:\n\n");

        // Java AES Crypto
        sb.append(setFormat("Java AES Crypto", "Isaac Potoczny-Jones", "https://github.com/tozny/java-aes-crypto/"));

        // Icon credit
        sb.append(setFormat("NoteBuddy Icon", "Mohammad Roqib", "https://store.kde.org/p/1012528/"));

        // Commit message
        sb.append("The following people have contributed to this FOSS project:\n\n");

        // Advise contributors
        sb.append(setFormat("Security advise", "Vanitasvitae", "https://github.com/vanitasvitae"));

        // Documentation contributors
        sb.append(setFormat("Documentation", "Poussinou", "https://github.com/Poussinou"));

        // Translation contributors
        sb.append(setFormat("Japanese translation", "Naofum", "https://github.com/naofum"));

        return sb.toString();
    }

    /**
     * Setting the format of a credits string
     * @param title
     * @param author
     * @param url
     * @return
     */
    private String setFormat(@NonNull String title, @NonNull String author, @NonNull String url) {
        return String.format("%s\n%s\n%s\n\n", title, author, url);
    }
}
