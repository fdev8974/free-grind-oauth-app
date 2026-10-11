# Free Grind Google OAuth

An Android companion app that obtains a Google OAuth token for signing in to [Free Grind](https://github.com/fdev8974/free-grind). Based on [Open Grind Google OAuth](https://git.opengrind.org/open-grind/google-oauth-app).

Install it next to Free Grind, then tap **Sign in with the Google app** on Free Grind's sign-in screen. The token is handed straight back to Free Grind; no copy and paste needed. Opened on its own, the app hands the token to Free Grind too, or shows it for copying when Free Grind is not installed.

Free Grind and this app only talk to each other when both are signed with the same key (Free Grind's release keystore, see [BUILDING.md](./BUILDING.md#signing)).

## Building

See [BUILDING.md](./BUILDING.md).

## License

[MIT](./LICENSE)
