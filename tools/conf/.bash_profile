# .bash_profile

# Get the aliases and functions
if [ -f ~/.bashrc ]; then
	. ~/.bashrc
fi

if [ -f ~/.local-settings ]; then
	. ~/.local-settings
fi

export BASH_ENV="~/.bash_env"

# User specific environment and startup programs

PATH=$PATH:$HOME/bin:/usr/local/bin:/usr/local/maven/bin:/usr/local/ant/bin

# ONLY EXPORT ON *NIX OR WINDOWS, OSX ALREADY HAS IT SET
#export JAVA_HOME=/usr/lib/jvm/java
export PATH
unset USERNAME

# SUBVERSION EDITOR
export SVN_EDITOR=emacs

