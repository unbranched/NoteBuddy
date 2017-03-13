package nl.yoerinijs.notebuddy.credits;

/**
 * A class for creating a credits string
 */
public class CreditsBuilder {

    /**
     * Credits getter
     * @return
     */
    public String getCredits() {
        return createCredits();
    }

    /**
     * A method for creating the credits
     * @return
     */
    private String createCredits() {
        StringBuilder sb = new StringBuilder();

        // General message
        sb.append("NoteBuddy is made by Yoeri Nijs and is released under the GNU General Public License v3 with gratitude.\n");
        sb.append("https://www.yoerinijs.nl\n\n");

        // General thank you message
        sb.append("This program uses various sources in order to work properly. The following people and institutions will be acknowledged for their work:\n\n");

        // Android Studio Code credit
        sb.append(setFormat("Android Studio Code", "Google", "https://developer.android.com/studio/index.html"));

        // Java AES Crypto
        sb.append(setFormat("Java AES Crypto", "Isaac Potoczny-Jones", "https://github.com/tozny/java-aes-crypto/"));

        // Icon credit
        sb.append(setFormat("NoteBuddy Icon", "Freepik", "http://www.flaticon.com/free-icon/notes_129492/"));

        return sb.toString();
    }

    /**
     * Setting the format of a credits string
     * @param title
     * @param author
     * @param url
     * @return
     */
    private String setFormat(String title, String author, String url) {
        return String.format("%s\n%s\n%s\n\n", title, author, url);
    }
}
