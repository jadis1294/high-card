Maven Toolchains for Java 21

This project requests a Java 21 toolchain so you can build with Java 21 without changing your system-wide JAVA_HOME. To enable this, create a `toolchains.xml` file in your Maven settings folder (usually `%USERPROFILE%\.m2\toolchains.xml` on Windows).

Sample `toolchains.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<toolchains xmlns="http://maven.apache.org/TOOLCHAINS/1.1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://maven.apache.org/TOOLCHAINS/1.1.0 https://maven.apache.org/xsd/toolchains-1.1.0.xsd">
  <toolchain>
    <type>jdk</type>
    <provides>
      <version>21</version>
      <vendor>any</vendor>
    </provides>
    <configuration>
      <!-- Change this path to your local JDK 21 installation -->
      <jdkHome>C:\\Program Files\\Java\\jdk-21</jdkHome>
    </configuration>
  </toolchain>
</toolchains>
```

How to use

1. Install a JDK 21 (Temurin / Corretto / Zulu / Microsoft builds). Note the installation path.
2. Create `%USERPROFILE%\\.m2\\toolchains.xml` with the sample above, updating `<jdkHome>` to the JDK 21 path.
3. Run Maven from the project root:

```powershell
.\mvnw.cmd -DskipTests=false clean test
```

If Maven finds the toolchain it will use that JDK for compilation even if your system `JAVA_HOME` is older.

Helper script

There's a helper PowerShell script at `scripts/setup-toolchains.ps1` that writes a `toolchains.xml` for you. Example usage from the repo root (PowerShell):

```powershell
# interactive: will prompt for JDK path
.\scripts\setup-toolchains.ps1

# or pass path directly
.\scripts\setup-toolchains.ps1 -JdkHome "C:\\Program Files\\Java\\jdk-21"
```

After running the script, run the Maven build:

```powershell
.\mvnw.cmd -DskipTests=false clean test
```

Troubleshooting

- If Maven still uses Java 11, confirm your `toolchains.xml` path and contents and run Maven with `-X` for debug logging.
- You can check which toolchain Maven selected by running Maven with `-Dorg.apache.maven.plugins.toolchains=true -X` and searching the output for toolchain resolution.
