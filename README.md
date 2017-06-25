# NoteBuddy
A simple Android application for storing encrypted notes. The notes are encrypted using password key derivation. The password will not be stored to ensure security.

It is important to note that, when one deletes the account, all notes will be deleted automatically. Moreover, when one forgots the password, it should be impossible to read the notes. In that case, it is needed to reinstall the app.

To use NoteBuddy, one must setup a simple user account (username and password with more than five chars) in order to login. All settings are stored in Android's Shared Preferences for now. The idea is to work with a SQlite database on the long term.

<a href="https://f-droid.org/repository/browse/?fdid=nl.yoerinijs.notebuddy" target="_blank">
<img src="https://f-droid.org/badge/get-it-on.png" alt="Get it on F-Droid" height="100"/></a> 
<a href='https://play.google.com/store/apps/details?id=nl.yoerinijs.nb'><img alt='Get it on Google Play' height="100" src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png'/></a>

# Help wanted
NoteBuddy was a project to learn how to code in Android. However, due the fact learning is fun, the app will be extended in the following time. Obviously, all help will be appreciated!

# Todo
- Adding some search functionality;
- Implement unit tests;
- The possibility to work with multiple user accounts;
- Come up with the possibility to hash keys in Android's in shared preferences.

# Done
- Add some (external) back-up functionality;
- Better check for password hash;
- Do some code reviewing.

# Changes
- Version 1.3.0: Major code refactoring. Furthermore, it is possible to follow backup progresses now due to new progress bars. And, finally, there is a feedback button in the menu;
- Version 1.2.2: Fixed a bug that prevented to backup files correctly;
- Version 1.2.1: Bugfix and possibility added to clear external backup storage, so it is not necessary to use another file browser to do this;
- Version 1.2.0: Major improvements added. Primarily, the initial version of some external back-up functionality is introduced. It has been chosen to simply copy the encrypted or decrypted notes, and not to support Google, Dropbox, or whatsoever due to privacy concerns. Users can copy the notes to external backup services themselves, if wanted. Secondly, the setup now validates the password, so it should be impossible to make a mistake. Lastly, some dialog language is changed and a new introduction text is added to the NotesActivity;
- Version 1.1.4: When one changes the note name, the note with the old name will be deleted automatically. Furthermore, password validation is improved. <b>Warning:</b> when updating to this version, all previously stored notes are not accessible anymore;
- Version 1.1.3: Bugfix;
- Version 1.1.2: One can now send plain text directly to NoteBuddy. Furthermore two bugs fixed. Primarily, a bug that prevents users with Android API level < 24 to login. Lastly, in some cases it was possible to go back to the previous activity, even when the user had locked the session;
- Version 1.1.1: Bugfix and Dutch translation added;
- Version 1.1.0: enhanced encryption based on key derivation. To strengthen security, it is decided to delete the forgotten password functionality. See [this issue](https://github.com/YoeriNijs/NoteBuddy/issues/1). Furthermore, code cleaned and added Japanese translation;
- Version 1.0.1: due a bug it was possible that one was not able to login. Fixed;
- Version 1.0.0: Initial release.

# Screenshots
[Screenshots](https://play.google.com/store/apps/details?id=nl.yoerinijs.nb)
