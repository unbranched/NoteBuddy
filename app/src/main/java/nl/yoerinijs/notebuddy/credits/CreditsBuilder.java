package nl.yoerinijs.notebuddy.credits;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

/**
 * A class for creating credits
 */
public class CreditsBuilder {

    /**
     * Credits getter
     * @return
     */
    public static String getCredits(@NonNull Context context) {
        return createCredits(context);
    }

    /**
     * A method for creating the credits
     * @return
     */
    private static String createCredits(@NonNull Context context) {
        StringBuilder sb = new StringBuilder();

        sb.append("NoteBuddy is made by Yoeri Nijs and is released under the GNU General Public License v3 with gratitude.\n\n");
        sb.append("Source: https://github.com/YoeriNijs/NoteBuddy/\n");

        try {
            String noteBuddyVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            sb.append("Bug reports: https://github.com/YoeriNijs/NoteBuddy/issues\n");
            sb.append("Installed version: " + noteBuddyVersion + "\n\n");
        } catch (PackageManager.NameNotFoundException e) {
            // Cannot get version name. Skip.
        }

        sb.append("This program uses various sources in order to work properly. The following people and institutions will be acknowledged for their work:\n\n");
        sb.append(setFormat("Java AES Crypto", "Isaac Potoczny-Jones", "https://github.com/tozny/java-aes-crypto/"));
        sb.append(setFormat("NoteBuddy Icon", "DinosoftLabs/Flaticon (Flaticon Basic License)", "http://www.flaticon.com/free-icon/notepad_345743"));
        sb.append("The following people have contributed to this FOSS project:\n\n");
        sb.append(setFormat("Security advise", "Vanitasvitae", "https://github.com/vanitasvitae"));
        sb.append(setFormat("Documentation", "Poussinou", "https://github.com/Poussinou"));
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
    private static String setFormat(@NonNull String title, @NonNull String author, @NonNull String url) {
        return String.format("%s\n%s\n%s\n\n", title, author, url);
    }
}
