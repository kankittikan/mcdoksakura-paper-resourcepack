# ResourcePackEnforcer (Paper 1.21.x)

Enforces a server resource pack on join. Players who decline (or fail to download) are kicked if `require` is true. URL and options are configurable.

## Configure

Edit `src/main/resources/config.yml`:

```yaml
resource-pack:
  url: "https://example.com/pack.zip"
  sha1: ""           # optional but recommended (40-char lowercase SHA-1)
  require: true       # kick players who decline or fail to download
  prompt: "This server requires a resource pack. Click Yes to continue."
  kick-message: "You must accept the server resource pack to play."
```

Generate SHA-1 (macOS):

```bash
shasum /path/to/pack.zip | awk '{print $1}'
```

## Build

Requires Java 21.

```bash
./gradlew build
```

The plugin JAR will be in `build/libs/`.

## Install

1. Copy the built JAR to your server's `plugins/` directory.
2. Start the server once to generate `plugins/ResourcePackEnforcer/config.yml`.
3. Edit the generated config with your URL (and SHA-1), then `/resourcepackreload` in-game or restart.

Tip: If you previously set `resource-pack` in `server.properties`, consider clearing it to avoid conflicts.

## Commands

- `/resourcepackreload` — reloads the plugin configuration.


# mcdoksakura-paper-resourcepack
