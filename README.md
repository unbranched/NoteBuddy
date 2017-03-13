# NoteBuddy
A simple Android application for storing encrypted notes. Notes are encrypted using [the AES Crypto library by Tozny LLC](https://github.com/tozny/java-aes-crypto), which means that they are stored using key generation, encryption, and decryption using 128-bit AES, CBC, PKCS5 padding, and a random 16-byte IV with SHA1PRNG.

To use NoteBuddy, one must setup a simple user account (username and numerical password) in order to login. Moreover, one may create a secret question in order to gain access when the password is lost. All settings are stored encrypted in Android's Shared Preferences for now. The idea is to work with a SQlite database on the long term.
