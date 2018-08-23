# NoteBuddy FAQs

Welcome to the FAQs section of NoteBuddy. This document is available to assist new and existing users of NoteBuddy to navigate easily on NoteBuddy.

#### Why the name NoteBuddy?
NoteBuddy just sounds cool and, moreover, is a friendly tool that helps to create, store and maintain notes for future references.

#### How do I start making notes on NoteBuddy?
Once the user is logged in, he or she can click on the pencil icon at the bottom right of the screen. Subsequently, the user is able to insert the preferred note and the note title in the respective boxes. The title helps users to scroll through the note list with ease.

You can paste notes from anywhere on your device into your NoteBuddy notes. Select the save icon at the bottom right this automatically saves the notes.

#### How do I backup my notes?
Click on the menu icon at the top left of the screen and select **create backup**. Then select either **decrypted** or **encrypted**, and a backup file would be generated depending on your selection.

#### NoteBuddy uses what sort of encryption algorithm?
NoteBuddy uses the Advanced Encryption Standard (AES) algorithm implementation by Tozny's for encryption and decryption of data.
Click [here](https://github.com/tozny/java-aes-crypto) to read more about Tozny's AES algorithm.

#### Which encryption algorithm does NoteBuddy use?
Advanced Encryption Standard (AES) is a **symmetric-key algorithm**, which implies the use of the same key for both encrypting and decrypting data.

#### On which Android versions does NoteBuddy work?
NoteBuddy is a note app that is available for all devices that are running on Android 6.0 and above.

#### What permission does NoteBuddy require?
NoteBuddy only requires read and write access to your device storage.

#### Where are my NoteBuddy login details stored?
For now, login details, such as the username, salts and user preferences, are stored in Android shared preferences. However, in the future, SQLite might be used. Please note: NoteBuddy **does not** store the password; the application uses password key derivation.

#### What is the minimum password length on NoteBuddy?
The minimum password length required by NoteBuddy is six (6), although the recommendend password length is eight (8).

#### What mixtures of password characters are viable?
NoteBuddy allows users to use the mixture of numbers, letters, and symbols for a stronger password.

#### How do I change my NoteBuddy password?
First, you need to make an unencrypted backup of your notes, because with encryption enabled it is not possible to read the notes with a new password. Next, click the menu button at the top left of the screen and select **reset NoteBuddy**. Warning: this process deletes all your notes. A password setup page will appear, where you fill in your new details and click done.
Click on the menu button at the top left of the screen and select **Import backed up notes**, this would restore your deleted notes. In the future, a more efficient process will become available.

#### What do I have to do when I have forgotten my password and cannot log in anymore?
In this case, the only option you have is to reinstall the app to gain access to NoteBuddy. Warning: this process deletes all your saved notes. 

#### I like this app, how can I help?
NoteBuddy is an open source project, and free to use. You can support the project via developing more features. Feel free to make a pull request. 

#### Are my notes accessible to the developer?
No, NoteBuddy does not collect any user data.

#### Where can I download NoteBuddy?
NoteBuddy is available in Google Play and F-droid.

#### Need to contact the developer?	
Click the menu button at the top left of NoteBuddy's screen and select **feedback**.

NoteBuddy is an app under development, with more exciting features to be added to enhance user’s experience.
This document would be updated from time to time, whenever new changes or implementations are made to the software, to help users navigate easily and better with new updates. For further inquiries, you can contact the developer via the issue section [here]( https://github.com/YoeriNijs/NoteBuddy/issues)

### Credits
This document is provided by [omideleayo](https://github.com/omideleayo).
