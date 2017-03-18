# NoteBuddy
A simple Android application for storing encrypted notes. Notes are encrypted using [the AES Crypto library by Tozny LLC](https://github.com/tozny/java-aes-crypto), which mean that they are stored using key generation, encryption, and decryption using 128-bit AES, CBC, PKCS5 padding, and a random 16-byte IV with SHA1PRNG.

To use NoteBuddy, one must setup a simple user account (username and numerical password) in order to login. Moreover, one may create a secret question in order to gain access when the password is lost. All settings are stored encrypted in Android's Shared Preferences for now. The idea is to work with a SQlite database on the long term.

# Todo
- Implement unit tests;
- Do some code reviewing.

# Screenshots
[Login](http://imgur.com/tyCq6Nu)

[Menu](http://imgur.com/Amme6pb)

[Note editing](http://imgur.com/Cnzwmbd)

# Download
![Download NoteBuddy](https://upload.wikimedia.org/wikipedia/commons/thumb/0/0d/Get_it_on_F-Droid.svg/320px-Get_it_on_F-Droid.svg.png)

[Download NoteBuddy](https://f-droid.org/repository/browse/?fdfilter=notebuddy&fdid=nl.yoerinijs.notebuddy)
