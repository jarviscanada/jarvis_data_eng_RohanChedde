FROM mcr.microsoft.com/devcontainers/java:1-21-bullseye
RUN sudo apt-get update && sudo apt-get install -y postgresql-client
RUN su vscode -c "umask 0002 && . /usr/local/sdkman/bin/sdkman-init.sh && sdk install maven"
