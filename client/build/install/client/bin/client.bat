@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem
@rem SPDX-License-Identifier: Apache-2.0
@rem

@if "%DEBUG%"=="" @echo off
@rem ##########################################################################
@rem
@rem  client startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.
@rem This is normally unused
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and CLIENT_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS="--module-path" "/Volumes/RCA/arcade/ArcadeHub/client/build/install/client/lib" "--add-modules" "javafx.controls,javafx.fxml,javafx.media"

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if %ERRORLEVEL% equ 0 goto execute

echo. 1>&2
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH. 1>&2
echo. 1>&2
echo Please set the JAVA_HOME variable in your environment to match the 1>&2
echo location of your Java installation. 1>&2

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo. 1>&2
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME% 1>&2
echo. 1>&2
echo Please set the JAVA_HOME variable in your environment to match the 1>&2
echo location of your Java installation. 1>&2

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\client-1.0.0.jar;%APP_HOME%\lib\common-1.0.0.jar;%APP_HOME%\lib\jackson-databind-2.17.0.jar;%APP_HOME%\lib\jackson-annotations-2.17.0.jar;%APP_HOME%\lib\jackson-core-2.17.0.jar;%APP_HOME%\lib\jackson-module-kotlin-2.17.0.jar;%APP_HOME%\lib\kotlin-reflect-1.7.22.jar;%APP_HOME%\lib\kotlin-stdlib-2.0.21.jar;%APP_HOME%\lib\logback-classic-1.5.3.jar;%APP_HOME%\lib\slf4j-api-2.0.12.jar;%APP_HOME%\lib\netty-all-4.1.108.Final.jar;%APP_HOME%\lib\gdx-backend-lwjgl3-1.12.1.jar;%APP_HOME%\lib\gdx-1.12.1.jar;%APP_HOME%\lib\javafx-fxml-20-mac.jar;%APP_HOME%\lib\javafx-controls-20-mac.jar;%APP_HOME%\lib\javafx-media-20-mac.jar;%APP_HOME%\lib\gdx-platform-1.12.1-natives-desktop.jar;%APP_HOME%\lib\annotations-13.0.jar;%APP_HOME%\lib\hibernate-core-6.5.0.Final.jar;%APP_HOME%\lib\jakarta.persistence-api-3.1.0.jar;%APP_HOME%\lib\netty-transport-native-epoll-4.1.108.Final-linux-x86_64.jar;%APP_HOME%\lib\netty-transport-native-epoll-4.1.108.Final-linux-aarch_64.jar;%APP_HOME%\lib\netty-transport-native-epoll-4.1.108.Final-linux-riscv64.jar;%APP_HOME%\lib\netty-transport-native-kqueue-4.1.108.Final-osx-x86_64.jar;%APP_HOME%\lib\netty-transport-native-kqueue-4.1.108.Final-osx-aarch_64.jar;%APP_HOME%\lib\netty-transport-classes-epoll-4.1.108.Final.jar;%APP_HOME%\lib\netty-transport-classes-kqueue-4.1.108.Final.jar;%APP_HOME%\lib\netty-resolver-dns-native-macos-4.1.108.Final-osx-x86_64.jar;%APP_HOME%\lib\netty-resolver-dns-native-macos-4.1.108.Final-osx-aarch_64.jar;%APP_HOME%\lib\netty-resolver-dns-classes-macos-4.1.108.Final.jar;%APP_HOME%\lib\netty-resolver-dns-4.1.108.Final.jar;%APP_HOME%\lib\netty-handler-4.1.108.Final.jar;%APP_HOME%\lib\netty-transport-native-unix-common-4.1.108.Final.jar;%APP_HOME%\lib\netty-codec-dns-4.1.108.Final.jar;%APP_HOME%\lib\netty-codec-4.1.108.Final.jar;%APP_HOME%\lib\netty-transport-4.1.108.Final.jar;%APP_HOME%\lib\netty-buffer-4.1.108.Final.jar;%APP_HOME%\lib\netty-codec-haproxy-4.1.108.Final.jar;%APP_HOME%\lib\netty-codec-http-4.1.108.Final.jar;%APP_HOME%\lib\netty-codec-http2-4.1.108.Final.jar;%APP_HOME%\lib\netty-codec-memcache-4.1.108.Final.jar;%APP_HOME%\lib\netty-codec-mqtt-4.1.108.Final.jar;%APP_HOME%\lib\netty-codec-redis-4.1.108.Final.jar;%APP_HOME%\lib\netty-codec-smtp-4.1.108.Final.jar;%APP_HOME%\lib\netty-codec-socks-4.1.108.Final.jar;%APP_HOME%\lib\netty-codec-stomp-4.1.108.Final.jar;%APP_HOME%\lib\netty-codec-xml-4.1.108.Final.jar;%APP_HOME%\lib\netty-resolver-4.1.108.Final.jar;%APP_HOME%\lib\netty-common-4.1.108.Final.jar;%APP_HOME%\lib\netty-handler-proxy-4.1.108.Final.jar;%APP_HOME%\lib\netty-handler-ssl-ocsp-4.1.108.Final.jar;%APP_HOME%\lib\netty-transport-rxtx-4.1.108.Final.jar;%APP_HOME%\lib\netty-transport-sctp-4.1.108.Final.jar;%APP_HOME%\lib\netty-transport-udt-4.1.108.Final.jar;%APP_HOME%\lib\gdx-jnigen-loader-2.3.1.jar;%APP_HOME%\lib\lwjgl-glfw-3.3.3.jar;%APP_HOME%\lib\lwjgl-glfw-3.3.3-natives-linux.jar;%APP_HOME%\lib\lwjgl-glfw-3.3.3-natives-linux-arm32.jar;%APP_HOME%\lib\lwjgl-glfw-3.3.3-natives-linux-arm64.jar;%APP_HOME%\lib\lwjgl-glfw-3.3.3-natives-macos.jar;%APP_HOME%\lib\lwjgl-glfw-3.3.3-natives-macos-arm64.jar;%APP_HOME%\lib\lwjgl-glfw-3.3.3-natives-windows.jar;%APP_HOME%\lib\lwjgl-glfw-3.3.3-natives-windows-x86.jar;%APP_HOME%\lib\lwjgl-jemalloc-3.3.3.jar;%APP_HOME%\lib\lwjgl-jemalloc-3.3.3-natives-linux.jar;%APP_HOME%\lib\lwjgl-jemalloc-3.3.3-natives-linux-arm32.jar;%APP_HOME%\lib\lwjgl-jemalloc-3.3.3-natives-linux-arm64.jar;%APP_HOME%\lib\lwjgl-jemalloc-3.3.3-natives-macos.jar;%APP_HOME%\lib\lwjgl-jemalloc-3.3.3-natives-macos-arm64.jar;%APP_HOME%\lib\lwjgl-jemalloc-3.3.3-natives-windows.jar;%APP_HOME%\lib\lwjgl-jemalloc-3.3.3-natives-windows-x86.jar;%APP_HOME%\lib\lwjgl-openal-3.3.3.jar;%APP_HOME%\lib\lwjgl-openal-3.3.3-natives-linux.jar;%APP_HOME%\lib\lwjgl-openal-3.3.3-natives-linux-arm32.jar;%APP_HOME%\lib\lwjgl-openal-3.3.3-natives-linux-arm64.jar;%APP_HOME%\lib\lwjgl-openal-3.3.3-natives-macos.jar;%APP_HOME%\lib\lwjgl-openal-3.3.3-natives-macos-arm64.jar;%APP_HOME%\lib\lwjgl-openal-3.3.3-natives-windows.jar;%APP_HOME%\lib\lwjgl-openal-3.3.3-natives-windows-x86.jar;%APP_HOME%\lib\lwjgl-opengl-3.3.3.jar;%APP_HOME%\lib\lwjgl-opengl-3.3.3-natives-linux.jar;%APP_HOME%\lib\lwjgl-opengl-3.3.3-natives-linux-arm32.jar;%APP_HOME%\lib\lwjgl-opengl-3.3.3-natives-linux-arm64.jar;%APP_HOME%\lib\lwjgl-opengl-3.3.3-natives-macos.jar;%APP_HOME%\lib\lwjgl-opengl-3.3.3-natives-macos-arm64.jar;%APP_HOME%\lib\lwjgl-opengl-3.3.3-natives-windows.jar;%APP_HOME%\lib\lwjgl-opengl-3.3.3-natives-windows-x86.jar;%APP_HOME%\lib\lwjgl-stb-3.3.3.jar;%APP_HOME%\lib\lwjgl-stb-3.3.3-natives-linux.jar;%APP_HOME%\lib\lwjgl-stb-3.3.3-natives-linux-arm32.jar;%APP_HOME%\lib\lwjgl-stb-3.3.3-natives-linux-arm64.jar;%APP_HOME%\lib\lwjgl-stb-3.3.3-natives-macos.jar;%APP_HOME%\lib\lwjgl-stb-3.3.3-natives-macos-arm64.jar;%APP_HOME%\lib\lwjgl-stb-3.3.3-natives-windows.jar;%APP_HOME%\lib\lwjgl-stb-3.3.3-natives-windows-x86.jar;%APP_HOME%\lib\lwjgl-3.3.3.jar;%APP_HOME%\lib\lwjgl-3.3.3-natives-linux.jar;%APP_HOME%\lib\lwjgl-3.3.3-natives-linux-arm32.jar;%APP_HOME%\lib\lwjgl-3.3.3-natives-linux-arm64.jar;%APP_HOME%\lib\lwjgl-3.3.3-natives-macos.jar;%APP_HOME%\lib\lwjgl-3.3.3-natives-macos-arm64.jar;%APP_HOME%\lib\lwjgl-3.3.3-natives-windows.jar;%APP_HOME%\lib\lwjgl-3.3.3-natives-windows-x86.jar;%APP_HOME%\lib\jlayer-1.0.1-gdx.jar;%APP_HOME%\lib\jorbis-0.0.17.jar;%APP_HOME%\lib\byte-buddy-1.14.12.jar;%APP_HOME%\lib\javafx-graphics-20-mac.jar;%APP_HOME%\lib\logback-core-1.5.3.jar;%APP_HOME%\lib\jakarta.transaction-api-2.0.1.jar;%APP_HOME%\lib\jboss-logging-3.5.0.Final.jar;%APP_HOME%\lib\hibernate-commons-annotations-6.0.6.Final.jar;%APP_HOME%\lib\jandex-3.1.2.jar;%APP_HOME%\lib\classmate-1.5.1.jar;%APP_HOME%\lib\jaxb-runtime-4.0.2.jar;%APP_HOME%\lib\jaxb-core-4.0.2.jar;%APP_HOME%\lib\jakarta.xml.bind-api-4.0.0.jar;%APP_HOME%\lib\jakarta.inject-api-2.0.1.jar;%APP_HOME%\lib\antlr4-runtime-4.13.0.jar;%APP_HOME%\lib\javafx-base-20-mac.jar;%APP_HOME%\lib\angus-activation-2.0.0.jar;%APP_HOME%\lib\jakarta.activation-api-2.1.1.jar;%APP_HOME%\lib\txw2-4.0.2.jar;%APP_HOME%\lib\istack-commons-runtime-4.1.1.jar


@rem Execute client
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %CLIENT_OPTS%  -classpath "%CLASSPATH%" com.arcadehub.client.Main %*

:end
@rem End local scope for the variables with windows NT shell
if %ERRORLEVEL% equ 0 goto mainEnd

:fail
rem Set variable CLIENT_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
set EXIT_CODE=%ERRORLEVEL%
if %EXIT_CODE% equ 0 set EXIT_CODE=1
if not ""=="%CLIENT_EXIT_CONSOLE%" exit %EXIT_CODE%
exit /b %EXIT_CODE%

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
