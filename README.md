# NoteBuddy
A simple Android application for storing encrypted notes. The notes are encrypted using password key derivation. The password will not be stored to ensure security.

It is important to note that, when one deletes the account, all notes will be deleted automatically. Moreover, when one forgots the password, it should be impossible to read the notes. In that case, it is needed to reinstall the app.

To use NoteBuddy, one must setup a simple user account (username and password with more than five chars) in order to login. All settings are stored encrypted in Android's Shared Preferences for now. The idea is to work with a SQlite database on the long term.

<a href="https://f-droid.org/repository/browse/?fdid=nl.yoerinijs.notebuddy" target="_blank">
<img src="https://f-droid.org/badge/get-it-on.png" alt="Get it on F-Droid" height="100"/></a> 
<a href='https://play.google.com/store/apps/details?id=nl.yoerinijs.notebuddy&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' height="100" src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png'/></a>

# Help wanted
NoteBuddy was a project to learn how to code in Android. However, due the fact learning is fun, the app will be extended in the following time. Obviously, all help will be appreciated!

# Todo
- Implement unit tests;
- Do some code reviewing;
- Add some (external) back-up functionality;
- The possibility to work with multiple user accounts;
- Create a better way for validating the login password.

# Changes
- Version 1.1.1: Bugfix and Dutch translation added
- Version 1.1.0: enhanced encryption based on key derivation. To strengthen security, it is decided to delete the forgotten password functionality. See [this issue](https://github.com/YoeriNijs/NoteBuddy/issues/1). Furthermore, code cleaned and added Japanese translation;
- Version 1.0.1: due a bug it was possible that one was not able to login. Fixed;
- Version 1.0.0: Initial release.

# Screenshots
[Screenshots](http://imgur.com/a/bQrgZ)
