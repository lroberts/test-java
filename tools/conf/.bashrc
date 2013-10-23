#!/bin/bash

# Source global definitions
if [ -f /etc/bashrc ]; then
	. /etc/bashrc
fi

export PATH=${PATH}:/Volumes/UCSF-Server/subversion/bin:/usr/local/maven:/Users/larry/Development/checkouts/webapp/bin

alias udrive="mount -t smbfs //lroberts@saa-udrive.campus.net.ucsf.edu/ ~/udrive"

export CLASSPATH=${CLASSPATH}:~/.m2/repository/org/apache/derby/derbytools/10.4.2.0/derbytools-10.4.2.0.jar:~/.m2/repository/org/apache/derby/derby/10.4.2.0/derby-10.4.2.0.jar:~/.m2/repository/org/apache/derby/derbyclient/10.4.2.0/derbyclient-10.4.2.0.jar

# IMPORTANT: annoyingly derby issues classnotfound exceptions if you set the classpath
# as an argument (ie. java -cp [my classpath] org.pache.derby.tools.ij), so you have to
# set it as an env var. (above)
alias derby="java org.apache.derby.tools.ij"

#connect 'jdbc:derby://127.0.0.1/immunization;create=true'
#connect 'jdbc:derby:immunization;create=true'
#connect 'jdbc:derby://127.0.0.1:1527/immunization;create=true'
#alias derby_createdb="java org.apache.derby.tools.ij"

export MAVEN_OPTS="-Xmx512m"
export ANT_HOME=/usr/local/ant
export TOMCAT_HOME=/usr/local/tomcat
#export JAVA_HOME=/System/Library/Frameworks/JavaVM.framework/Versions/1.5/Home
# ALIASES - we could put these in ~/.bash_aliases

# Trying to do this: alias grep="grep --exclude=*.svn* -Hrn "
# ie. ignore everything in the .svn dir
# but this is the only this that works 
# I CAN'T REALLY EXPLAIN THE WHOLE COMMAND BUT ITS MOSTLY THIS:
# http://snippets.dzone.com/posts/show/3707
# WITH A BIT OF:
# http://notfaq.wordpress.com/2006/07/30/unix-filenames-with-spaces-in-find-xargs/ 
#alias search="find . -path '*/.svn' -prune -o -type f -print0 | xargs -0 -e grep --exclude=*~ -I -n -e "
# mac xargs doesn't like -e. not sure what it's doing there anyway
alias search="find . -path '*/.svn' -prune -o -type f -print0 | xargs -0 grep --exclude=*~ -I -n -e "

# SOOOO MUCH EASIER WITH DIFF
alias diff="diff --exclude=*.svn -wBb -r --exclude=*.cvs --exclude=*.class  --side-by-side --suppress-common-lines --minimal"

# MAVEN WITH DEFAULT DEPLOYMENT
alias maven_offline="/usr/local/maven/bin/mvn -o -P ojb,development,tomcat-live,postgresql clean install"

#  -Djava.home=${JAVA_HOME} # this argument is sometimes needed for niotrack
alias mvn_online_Werrors="/usr/local/maven/bin/mvn -e -P ojb,development,tomcat-live,postgresql clean install"

alias mvno="dump maven_offline"
alias maven="dump maven_online_Werrors"
#
alias tomcat_log="tail -n 400 /usr/local/tomcat/logs/catalina.out | less"
alias bounce_tomcat="/usr/local/tomcat/bin/shutdown.sh; /usr/local/tomcat/bin/startup.sh"

alias get="wget --no-clobber -r --no-parent --passive-ftp "

alias start_apache="/Volumes/UCSF-Server/apache2/bin/apachectl -f /Volumes/UCSF-Server/apache2/conf/httpd.conf -k restart"
alias start_pgsql="pg_ctl -D ~/Development/cluster/ -l ~/Development/cluster/log.txt start"


# [please leave this message here so i will be reminded when i haven't
#  used shell scripts in a few months, and attempt to do the same again  ]
# 
# i have just spent an incredibly painful **THREE** hours attempting
# to get a simple search and replace working...
# 
# RULE #1 :  If you are having problems and you script uses backticks try using 'eval' instead
#            or both, ie. `eval ls -l`
# RULE #2 :  NEVER PUT SPACES BETWEEN THE REFERENCE, THE EQUALS SIGN and THE VALUE
#            ie. A="${something}" or B=`ls -l`
#            and NOT A = "blah"
# RULE #3 :  String handling is not always as expected. For a little test script I'm writing 
#            I needed to split a line on a ';' but preservere the "s and 's, something that 
#            echo doesn't like to do. Digging deeper into the bash docs I see that there 
#            are some handy string handling functions.
#
#            line='this "is" a command;this "is" a pattern'
#            COMMAND=${line%;*}
#            PATTERN=${line#*;}
#            echo $COMMAND
#            echo $PATTERN
#
#            see http://www.museum.state.il.us/ismdepts/library/linuxguides/abs-guide/string-manipulation.html
#
# checkout - checkout from sis cvs
# version  - operating system version info
# log      - redirect command output to a file (and keep on 
#            printing to the console)
# nuke     - delete files by name pattern. 
#            [operates in the current directory]
# repl     - replace "old text" "new text" '*.java'  
#            [operates in the current directory]
# diff_dir - find missing files betwen directories
# ask      - ask a question with a boolean/yes-no answer
# backup   - backup a directory to disk or a server
# calc     - simple calculator
# round    - round a floating point number
# exists   - test if a file or directory exists
# toLowercase - convert all files in the working directory
#            to lowercase. this function should be able to handle
#            paths with newlines 
# dos2unix - convert line ending from dos to unix
# echon    - echo without newline
# mk_key   - create a new key and update (this) client and a server 
# sync     - perform a complete backup (unfinished)
#
# mvn_install  - install a library into the maven repo
# jaxb_install - this is an older version of this function
#                the current version is part of the 3rdParty repo:
#                3rdParty/trunk/jaxb2/install_jaxb
#

function checkout(){
export CVS_RSH=ssh
cvs -d :ext:lroberts@gforge.campus.net.ucsf.edu:/cvsroot/portal checkout .
}

# PRINT OUT SYSTEM INFO
function version(){
echo "Kernel: `uname -s -r `"
echo "Release: `uname -v`"
echo "Machine [`uname -n`]: `uname -m -p`"
echo "Platform: `uname -i -o` "
}


function mk_key(){

# MUST RESET OPTIND
OPTIND=1
keyName=tmp_key_`date +%d%b%Y-%H-%M-%S`
keyDir=`pwd`/.ssh
user=${USER}
server="128.218.3.120"
COMMENT=

while getopts "vn:d:c:s:u:" flag
do
    case "$flag" in
      v)  BE_VERBOSE=on;;
      n)  keyName="${OPTARG}";;
      d)  keyDir="${OPTARG}";;
      c)  COMMENT="${OPTARG}";;
      s)  server="${OPTARG}";;
      u)  user="${OPTARG}";;
      \?) echo "usage: mk_key -n backup_auth -c \"admin key\""
          echo ""
          echo "       the key name should not contain any spaces"
          return 1;;
    esac
done

defaultComment="A UCSF SHS ${keyName} key."
KEY_DIR="${keyDir}/${keyName}"
PRIVATE_KEY="${keyName}"
PUBLIC_KEY="${PRIVATE_KEY}.pub"

echo "[INFO] Have private key name [${PRIVATE_KEY}], comment \"${COMMENT}\", dir ${KEY_DIR}"

# THE SIMPLE WAY - JUST A KEY PAIR
# -t rsa uses ssh protocol 2 (-t rsa1 for version 1)
ssh-keygen  -t rsa -b 2048 -C "${COMMENT}" -f "${PRIVATE_KEY}"

if [ $? -ne 0 ]
then
echo "[ERROR] Unable to create key pair [$?]"
return $?
fi

if [ ! -e ${KEY_DIR} ]
then
  mkdir "${KEY_DIR}"
fi

mv "${PRIVATE_KEY}" "${KEY_DIR}"
mv "${PUBLIC_KEY}" "${KEY_DIR}"

# CREATE DISK IMAGE, SEND ENCRYPTED COPY TO SERVER, BURN, AND KEEP THE KEY
# image must allow a partion of at least 512k, we make it a little larger
echo "[INFO] You can use Ctrl-c to skip the CD burn if you are only testing."
backup -b -S 600k -k -s 128.218.3.120 "${KEY_DIR}" 

if [ $? -ne 0 ]
then
echo "[ERROR] Unable to create disk image, backup returned [$?]"
return $?
fi


# UPDATE THE CLIENT AND SERVER ~/.ssh/authorized_keys
echo "[INFO] Updating ${server} with public key [${PUBLIC_KEY}]"
cat "${KEY_DIR}/${PUBLIC_KEY}" | ssh ${user}@${server} 'cat - >> ~/.ssh/authorized_keys'

if [ $? -ne 0 ]
then
echo "[ERROR] Unable to update ${server} server as user \"${user}\" [$?]"
return $?
fi

local_machine=`hostname`
echo "[INFO] Updating local machine [${user}@${local_machine}] with public key [${PUBLIC_KEY}]"
cat "${KEY_DIR}/${PUBLIC_KEY}" >> ~/.ssh/authorized_keys

if [ $? -ne 0 ]
then
echo "[ERROR] Unable to update local machine as user \"${user}\" [$?]"
return $?
fi


}

# @fixme doesn't seem to be working properly - only one diff is found
# @fixme make the temp file names an option
function diff_dir(){
# diff -qr "${1}" "${2}" | grep -v -e 'DS_Store' -e 'Thumbs' | sort 

SCRIPT="diff_dir"
FILELIST=/tmp/$SCRIPT.list$$
DIFFS=/tmp/$SCRIPT.diffs$$
BADUSAGE="Type \"$SCRIPT -h\" for command usage."

EXCLUDE_LIST=
EXCLUDE_DIRLIST=
BINARY=false
IGNORECR=false
IGNOREEXIST=false
LISTONLY=false
QUIET=false
RECURSIVE=false
SHOWLEFT=false
SHOWRIGHT=false
IGNORETEXT=false
DIFFOPTS=
CMPOPTS=

OPTIND=1
while getopts bBce:E:hlqrLRTX switch; do
   case "$switch" in
   b)
DIFFOPTS="$DIFFOPTS -b"
;;
   B)
BINARY=true
;;
   c)
IGNORECR=true
;;
   e)
EXCLUDE_LIST="$EXCLUDE_LIST $OPTARG"
;;
   E)
       EXCLUDE_DIRLIST="$EXCLUDE_DIRLIST $OPTARG"
    ;;
   h)
echo "$SCRIPT -- Compare files in two directories."
echo "Usage: $SCRIPT [options] leftdir rightdir"
echo "Options:"
echo "   -b        ignore trailing blanks"
echo "   -B        include comparison of binary files"
echo "   -c        ignore trailing carriage returns"
echo "   -e file   exclude file from comparison"
echo "   -E dir    exclude subdirectory from comparison"
echo "   -l        list filenames only"
echo "   -q        quiet mode (only list filename if there are differences)"
echo "   -r        recursive mode (descend all subdirectories)"
echo "   -L        show contents of files that only exist in leftdir"
echo "   -R        show contents of files that only exist in rightdir"
echo "   -T        ignore files that are text in one directory"
echo "   -X        ignore files that only exist in one directory"
exit 0
;;
   l)
LISTONLY=true
QUIET=true
;;
   q)
QUIET=true
;;
   r)
RECURSIVE=true
;;
   L)
SHOWLEFT=true
;;
   R)
SHOWRIGHT=true
;;
   T)
IGNORETEXT=true
;;
   X)
IGNOREEXIST=true
;;
   \?)
echo "$BADUSAGE" >&2
exit 1
;;
   esac
done
shift `expr $OPTIND - 1`

if [ $# -ne 2 ]; then
   echo "$BADUSAGE" >&2
   exit 1
elif [ ! -d "$1" ]; then
   echo "Invalid directory: $1" >&2
   echo "$BADUSAGE" >&2
   exit 1
elif [ ! -d "$2" ]; then
   echo "Invalid directory: $2" >&2
   echo "$BADUSAGE" >&2
   exit 1
fi

cdir=`pwd` # current directory
cd "$2"
rdir=`pwd` # right directory
cd "$cdir"
cd "$1"
ldir=`pwd` # left directory

if [ "$ldir" = "$rdir" ]; then
   exit 0
fi

NODIRS=`(for name in $EXCLUDE_DIRLIST; do
   find "$ldir" -name "$name" -type d -print | sed "s;^$ldir/;;"
   find "$rdir" -name "$name" -type d -print | sed "s;^$rdir/;;"
done) | sort | uniq`

if $RECURSIVE; then
   (
find "$ldir" -type f -print | sed "s;^$ldir/;;"
find "$rdir" -type f -print | sed "s;^$rdir/;;"
   ) | sort | uniq >"$FILELIST"
else
   (
'ls' -a1 "$ldir" | while read f; do
    if [ ! -d "$ldir/$f" ]; then
        echo "$f"
    fi
done
'ls' -a1 "$rdir" | while read f; do
    if [ ! -d "$rdir/$f" ]; then
        echo "$f"
    fi
done
   ) | sort | uniq >"$FILELIST"
fi

while read f; do
   if [ -n "$EXCLUDE_LIST" ]; then
doexclude=false
for exclude in $EXCLUDE_LIST; do
    if [ "`basename \"$f\"`" = "$exclude" ]; then
 doexclude=true
 break
    fi
done
if $doexclude; then
    continue
fi
   fi

   if [ -n "$NODIRS" ]; then
       doexclude=false
       for dir in $NODIRS; do
           quit=`expr "$f" : "$dir"`
           if [ $quit -gt 0 ]; then
               doexclude=true
               break
           fi
       done
       if $doexclude; then
    continue
       fi
   fi

   lfile="$ldir/$f"
   rfile="$rdir/$f"
   if [ -f "$lfile" ]; then
if [ -f "$rfile" ]; then
    if file "$lfile" | grep "text\$" >/dev/null; then
 if file "$rfile" | grep "text\$" >/dev/null; then
     if $IGNORECR; then
  lfile=/tmp/$SCRIPT.lfile$$
  sed "s;
*$;;" "$ldir/$f" >"$lfile"
  rfile=/tmp/$SCRIPT.rfile$$
  sed "s;
*$;;" "$rdir/$f" >"$rfile"
     fi
     if $QUIET; then
  diff $DIFFOPTS "$lfile" "$rfile" >"$DIFFS"
  if [ -s "$DIFFS" ]; then
      if $LISTONLY; then
   echo "$f"
      else
   echo "FILE: $f"
   cat "$DIFFS"
      fi
  fi
     else
  echo "FILE: $f"
  diff $DIFFOPTS "$lfile" "$rfile"
     fi
     if $IGNORECR; then
  rm -f "$lfile" "$rfile"
     fi
 else
     if $IGNORETEXT; then
  continue
     elif $LISTONLY; then
  echo "$f"
     else
  echo "FILE: $f is not a text file in $rdir"
     fi
 fi
    elif file "$rfile" | grep "text\$" >/dev/null; then
 if $IGNORETEXT; then
     continue
 elif $LISTONLY; then
     echo "$f"
 else
     echo "FILE: $f is not a text file in $ldir"
 fi
    elif $BINARY; then
 if $QUIET; then
     cmp $CMPOPTS "$lfile" "$rfile" >"$DIFFS"
     if [ -s "$DIFFS" ]; then
  if $LISTONLY; then
      echo "$f"
  else
      echo "FILE: $f"
      cat "$DIFFS"
  fi
     fi
 else
     echo "FILE: $f"
     cmp $CMPOPTS "$lfile" "$rfile"
 fi
    fi
else
    if $IGNOREEXIST; then
 continue
    elif $LISTONLY; then
 echo "$f"
    else
 echo "FILE: $f does not exist in $rdir"
 if $SHOWLEFT; then
     if file "$lfile" | grep "text\$" >/dev/null; then
  cat "$lfile" | sed "s;^;<;;"
     fi
 fi
    fi
fi
   else
if $IGNOREEXIST; then
    continue
elif $LISTONLY; then
    echo "$f"
else
    echo "FILE: $f does not exist in $ldir"
    if $SHOWRIGHT; then
 if file "$rfile" | grep "text\$" >/dev/null; then
     cat "$rfile" | sed "s;^;>;;"
 fi
    fi
fi
   fi
done <"$FILELIST"

rm -f "$FILELIST" "$DIFFS"

}


function optionTest() {


while getopts  "abc:d:ef:ghi" flag
do
  echo "$flag" $OPTIND $OPTARG
done
echo "Resetting"
OPTIND=1
while getopts  "abc:d:ef:ghi" flag
do
  echo "$flag" $OPTIND $OPTARG
done

}

function secure_checkout(){

# MUST RESET OPTIND
OPTIND=1

project=
DO_EXPORT=off

while getopts "vp:d:e" flag
do
    case "$flag" in
      v)  BE_VERBOSE=on;;
      e)  DO_EXPORT=on;;
      p)  project="${OPTARG}";;
      d)  projectDir="${OPTARG}";;
      \?) echo "secure_checkout trunk"
          return 1;;
    esac
done

DEFAULT_PROJECT_BASE=/Users/larry/Development/version_control
PROJECT_BASE=${projectDir:-${DEFAULT_PROJECT_BASE}}

DEFAULT_PROJECT=trunk
CHECKOUT_NAME=${project:-${DEFAULT_PROJECT}}

CHECKOUT_IMAGE=`basename ${CHECKOUT_NAME}`_`date +%d%b%Y-%H-%M-%S`
TMP_DIR_NAME=_tmp_${CHECKOUT_IMAGE}

mkdir ${TMP_DIR_NAME}
if [ ${DO_EXPORT} == "on" ]
then
svn export file://${PROJECT_BASE}/${CHECKOUT_NAME} ${TMP_DIR_NAME}
else
svn co file://${PROJECT_BASE}/${CHECKOUT_NAME} ${TMP_DIR_NAME}
fi

if [ $? -ne 0 ]
then
echo "[ERROR] Unable to checkout ${CHECKOUT_NAME}, svn returned [$?]"
return $?
fi

# CREATE DISK IMAGE W. 300mb FREE SPACE
backup -k -S 300m -f ${CHECKOUT_IMAGE}.dmg ${TMP_DIR_NAME} 

if [ $? -ne 0 ]
then
echo "[ERROR] Unable to create disk image, backup returned [$?]"
return $?
fi

# FIXME: backup DOES NOT RETURN A FILE WITH THE NAME WE SPECIFIED
#        SO WE HAVE TO FIDDLE WITH IT HERE
DMG=${CHECKOUT_IMAGE}.dmg.sparseimage
if [ -f ${DMG} ]
then
rm -rf ${TMP_DIR_NAME}
echo "[INFO]  Disk image file [${DMG}] created sucessfully."
else
echo "[ERROR] Disk image file [${DMG}] does not exist."
return 1;
fi

}

function backup_usage(){

echo
echo "usage: backup [options] [files ...]"
echo
echo "options:"
echo
echo " -b       burn cd/dvd"
echo " -d       set the working dir where the disk image will be created. default is pwd."
echo " -f       set the name of the disk image file to be created. "
echo " -k       keep the disk image after burn and/or upload. default is to delete it."
echo " -n       set the name of the volume that the disk image will mount as."
echo " -p       server path"
echo " -s       (ssh) server hostname"
echo " -S       add free space to the disk image. eg. -S 300m"
echo " -u       server user" 
echo " -v       be verbose"

echo

}

# FIXME: this function still has problems if the items in the list
#        have different sizes. everything works ok if all are in megabytes (m)
# FIXME: compressing the backup items seems to somehow upset the process, and
#        results in an error when trying to create the disk image
# FIXME: implement verbose option (echo any [TRACE] messages)
function backup(){

# MUST RESET OPTIND
OPTIND=1

user=${USER}
server=
remoteBackupDir=/home/${USER}/backups
userBackupDir=
userVolumeName=
userDmgName=
DO_BURN=off
DO_UPLOAD=off
DO_KEEP=off
FREE_SPACE=
declare -a BACKUP_THESE
# we use this if we are compressing the items
declare -a BACKUP_THESE_ARCHIVES

while getopts "vbkd:f:n:s:p:u:S:" flag
do
    case "$flag" in
      v)  BE_VERBOSE=on;;
      b)  DO_BURN=on;;
      k)  DO_KEEP=on;;
      s)  DO_UPLOAD=on;
          server="${OPTARG}";;
      S)  FREE_SPACE="${OPTARG}";;
      p)  remoteBackupDir="${OPTARG}";;
      u)  user="${OPTARG}";;
      d)  userBackupDir="${OPTARG}";;
      f)  userDmgName="$OPTARG";;
      n)  userVolumeName="$OPTARG";;
      \?) backup_usage
          return 1;;
    esac
done


let LAST_OPTION=${OPTIND}
let LAST_OPTION--

let LAST_OPTION_IDX=LAST_OPTION
let LAST_OPTION_IDX--

# ---------------------------------------------------
# FIND LIST OF FILES TO BE BACKED UP
# ---------------------------------------------------
count=0
FILE_COUNT=0
if [ $# -gt ${LAST_OPTION_IDX} ]
then
   for file in "$@"
   do
    #echo you typed ${file} ${count}.
    if [ $count -gt ${LAST_OPTION_IDX} ]
    then
	#echo "[TRACE] Adding file ${file} to backup list"
        BACKUP_THESE[FILE_COUNT]="${file}"

	let FILE_COUNT++
    fi

    let count++

   done

fi 


# ---------------------------------------------------
# FIND LIST OF FILES TO BE BACKED UP
# ---------------------------------------------------
if [ ${FILE_COUNT} -lt 1 ]
then
    echo "[ERROR] No files were specified to be backed up."
backup_usage
    return 1;
fi

# ---------------------------------------------------
# FIND WHERE WE'RE GOING TO PUT THINGS
# ---------------------------------------------------
# IF NO BACKUP DIR WAS SPECIFIED, USE THIS DIR
DEFAULT_BACKUP_DIR=`pwd`
#echo "[TRACE] Userdir is ${userBackupDir}, default is ${DEFAULT_BACKUP_DIR}"
BACKUP_DIR=${userBackupDir:-${DEFAULT_BACKUP_DIR}}

# IF NO VOLUME NAME WAS SPECIFIED, MAKE ONE UP FROM THE FIRST FILE IN THE LIST
DEFAULT_VOLUME_NAME=`basename ${BACKUP_THESE[0]}`_`date +%d%b%Y-%H-%M-%S`
#echo "[TRACE] Default Volume Name is ${DEFAULT_VOLUME_NAME}, user supplied ${userVolumeName}"
BACKUP_VOLUME_NAME=${userVolumeName:-${DEFAULT_VOLUME_NAME}}
#echo "[TRACE] Volume Name is ${BACKUP_VOLUME_NAME}"

# IF NO DMG NAME WAS SPECIFIED, MAKE ONE UP FROM THE FIRST FILE IN THE LIST
DEFAULT_BACKUP_DMG="${BACKUP_DIR}/${BACKUP_VOLUME_NAME}.dmg"
#echo "[TRACE] Default Image Name is ${DEFAULT_BACKUP_DMG}, user supplied ${userDmgName}"
BACKUP_DMG=${userDmgName:-${DEFAULT_BACKUP_DMG}}


if [ -f ${BACKUP_DMG} ]
then
  echo "[ERROR] ${BACKUP_DMG} already exists."
  return;
fi 

# ---------------------------------------------------
# CHECK ALL FILES EXIST AND CALCULATE SIZE
# ---------------------------------------------------
echo "[INFO] Backup list contains ${FILE_COUNT} items"

if [ ${FREE_SPACE} ]
then
    echo "[INFO] Adding [${FREE_SPACE}] to disk image."
   FREE_SIZE=`expr "${FREE_SPACE}" : '\([0-9\.]*\)'`   
   let free_len=${#FREE_SIZE}

   FREE_UNITS_UC=${FREE_SPACE:${free_len}}
   BACKUP_UNITS=`echo ${FREE_UNITS_UC} | tr '[:upper:]' '[:lower:]'`
   BACKUP_SIZE_TOTAL=${FREE_SIZE}
else
   BACKUP_SIZE_TOTAL=0
   BACKUP_UNITS=b
fi

CURRENT_BACKUP_UNITS="${BACKUP_UNITS}"
TMP_DIR=${BACKUP_DIR}/tmp
#mkdir ${TMP_DIR}

ORIGINAL_IFS=$IFS
IFS=$'\n'
for ((i=0;i<$FILE_COUNT;i++)); do
   
   BACKUP_THIS="${BACKUP_THESE[${i}]}"
   if [ ! -e ${BACKUP_THIS} ]
   then
     echo "[ERROR] ${BACKUP_THIS} does not exist."
     return;
   fi 

   # FIXME: THIS IS FOR COMPRESSION, SEEMS TO SCREW THINGS UP AT THE MOMENT
   #TAR_COPY=${TMP_DIR}/`basename ${BACKUP_THIS}`.tar
   #ZIP_COPY=${TAR_COPY}.gz
   #echo "[INFO] Creating a tar archive: ${TAR_COPY} from ${BACKUP_THIS}"
   #tar -cf ${TAR_COPY} ${BACKUP_THIS}
   #gzip ${TAR_COPY}
   #BACKUP_THESE_ARCHIVES[${i}]="${ZIP_COPY}"
   #BACKUP_THIS=${ZIP_COPY}


   echo -n "[INFO] Adding ${BACKUP_THIS} "

   # CAN'T USE IFS B/C ALREADY LOOPING WITH IT
   #BACKUP_DIR_SIZE=`du -hc ${BACKUP_THIS} | grep "total"`
   #read BACKUP_SIZE_STRING restOfDuOutput < <(IFS= ; echo ${BACKUP_DIR_SIZE})

   # ** DON'T TOUCH THIS!! ***
   # IT TOOK MANY HOURS AND I STILL DON'T KNOW WHY IT WORKED
   BACKUP_DIR_SIZE=`du -hc ${BACKUP_THIS} | grep "total"`
   CMD="echo ${BACKUP_DIR_SIZE} | cut -d \  -f 1"
   BACKUP_SIZE_STRING=`eval ${CMD}`
   #echo " output is ${BACKUP_SIZE_STRING}"

   BACKUP_SIZE=`expr "${BACKUP_SIZE_STRING}" : '\([0-9\.]*\)'`   
   let len=${#BACKUP_SIZE}

   BACKUP_UNITS_UC=${BACKUP_SIZE_STRING:${len}}
   BACKUP_UNITS=`echo ${BACKUP_UNITS_UC} | tr '[:upper:]' '[:lower:]'`
   echo -n "[${BACKUP_SIZE}${BACKUP_UNITS}] "

   # increment our units

   #echo "[TRACE] Units for this file are ${BACKUP_UNITS} "
   #echo "[TRACE] Current units are ${CURRENT_BACKUP_UNITS} "
   if [ ${BACKUP_UNITS} != ${CURRENT_BACKUP_UNITS} ]
   then
       if [ ${CURRENT_BACKUP_UNITS} == "b" ] 
	   then 
	   #echo "[TRACE] current units are bytes."

	   if [ ${BACKUP_UNITS} == "k" ] 
	       then 
	       BACKUP_SIZE_TOTAL=`calc "${BACKUP_SIZE_TOTAL}" / 1000`
	       CURRENT_BACKUP_UNITS="k"
	   
	   elif [ ${BACKUP_UNITS} == "m" ] 
	       then 
	       #echo "about to calc new total for ${BACKUP_SIZE_TOTAL}"
	       BACKUP_SIZE_TOTAL=`calc "${BACKUP_SIZE_TOTAL}" / 1000000`
	       #echo "total is now ${BACKUP_SIZE_TOTAL}"
	       CURRENT_BACKUP_UNITS="m"
	   
	   elif [ ${BACKUP_UNITS} == "g" ] 
	       then 
	       BACKUP_SIZE_TOTAL=`calc ${BACKUP_SIZE_TOTAL} / 1000000000`
	       CURRENT_BACKUP_UNITS="g"
	   fi

       elif [ ${CURRENT_BACKUP_UNITS} == "k" ] 
	   then 

	   # from b to k
	   if [ ${BACKUP_UNITS} == "b" ] 
	       then 
	       BACKUP_SIZE=`calc ${BACKUP_SIZE} / 1000`
	   
           # from k to m
	   elif [ ${BACKUP_UNITS} == "m" ] 
	       then 
	       BACKUP_SIZE_TOTAL=`calc ${BACKUP_SIZE_TOTAL} / 1000`
	       CURRENT_BACKUP_UNITS="m"
	   
	   elif [ ${BACKUP_UNITS} == "g" ] 
	       then 
	       BACKUP_SIZE_TOTAL=`calc ${BACKUP_SIZE_TOTAL} / 1000000`
	       CURRENT_BACKUP_UNITS="g"
	   fi

       elif [ ${CURRENT_BACKUP_UNITS} == "m" ] 
	   then 
	   if [ ${BACKUP_UNITS} == "b" ] 
	       then 
	       BACKUP_SIZE=`calc ${BACKUP_SIZE} / 1000000`
	   
	   elif [ ${BACKUP_UNITS} == "k" ] 
	       then 
	       BACKUP_SIZE=`calc ${BACKUP_SIZE} / 1000`

	   elif [ ${BACKUP_UNITS} == "g" ] 
	       then 
	       BACKUP_SIZE_TOTAL=`calc ${BACKUP_SIZE_TOTAL} / 1000`
	       CURRENT_BACKUP_UNITS="g"
	   fi

       elif [ ${CURRENT_BACKUP_UNITS} == "g" ] 
	   then
	   if [ ${BACKUP_UNITS} == "b" ] 
	       then 
	       BACKUP_SIZE=`calc ${BACKUP_SIZE} / 1000000000`
	   
	   elif [ ${BACKUP_UNITS} == "k" ] 
	       then 
	       BACKUP_SIZE=`calc ${BACKUP_SIZE} / 1000000`

	   elif [ ${BACKUP_UNITS} == "m" ] 
	       then 
	       BACKUP_SIZE=`calc ${BACKUP_SIZE} / 1000` 
	   fi

       else
	   echo "[ERROR] Unknown units '${BACKUP_UNITS}'"
	   
       fi
       
   fi
#     echo "[TRACE] incrementing size [${BACKUP_SIZE_TOTAL}${BACKUP_UNITS}] by ${BACKUP_SIZE}${BACKUP_UNITS}"
BACKUP_SIZE_TOTAL=`calc ${BACKUP_SIZE_TOTAL} + ${BACKUP_SIZE}`

     echo ", total size is now ${BACKUP_SIZE_TOTAL}${BACKUP_UNITS}"

done
IFS=$ORIGINAL_IFS

# FIXME: THIS IS JUST IF WE ARE ARCHIVING
#BACKUP_THESE=${BACKUP_THESE_ARCHIVES}

ADJUSTED_BACKUP_SIZE=`calc ${BACKUP_SIZE_TOTAL} \* 1.05235`

echo "[INFO] Adjusted backup size by 105% => ${ADJUSTED_BACKUP_SIZE}${BACKUP_UNITS} rounded to ${ADJUSTED_BACKUP_SIZE}${BACKUP_UNITS}"

echo "[INFO] Backing up [${BACKUP_THIS}] to local disk image "

# YOU CAN ADD A TRAILING -verbose TO SEE WHAT IS HAPPENING
# ADDING THE -type SPARSE CREATES A SPARSEIMAGE AND FOR SOME REASON
# hdutil THEN RENAMES THE IMAGE xx.dmg.sparseimage 
# IF I THAN CALL MY IMAGE xxx.dmg.sparseimage hdutil THEN WANTS
# TO CALL IT xxx.dmg.sparse.sparseimage ! I GIVE UP. I WILL
# JUST MAKE A NEW VAR FOR THE SPARSE IMAGE NAME
SPARSE_DMG="${BACKUP_DMG}.sparseimage"
CREATE="hdiutil create -encryption -type SPARSE -fs HFS+ -volname ${BACKUP_VOLUME_NAME} -size ${ADJUSTED_BACKUP_SIZE}${BACKUP_UNITS} ${BACKUP_DMG} "
echo "[INFO] About to create disk image [${BACKUP_DMG}]"
#echo "[TRACE} Command is: ${CREATE}"
${CREATE}

# OR CAN AUTOMATE PASSWORD
# echo -n "mypass" | hdiutil -encryption -stdinpass ...

if [ $? -ne 0 ]
then
echo "[ERROR] Unable to create disk image, hdiutil returned [$?]"
return $?
fi

echo "[INFO] Mounting disk image to /Volumes/${BACKUP_VOLUME_NAME}"
hdiutil mount ${SPARSE_DMG}

if [ $? -ne 0 ]
then
echo "[ERROR] Unable to mount disk image, hdiutil returned [$?]"
return $?
fi

echo "[INFO] Copying files to disk image... [this may take some time]"
ORIGINAL_IFS=$IFS
IFS=$'\n'
for ((i=0;i<$FILE_COUNT;i++)); do
   
   BACKUP_THIS="${BACKUP_THESE[${i}]}"
   # echo "[TRACE] Copying ${BACKUP_THIS} to disk image."
   ditto -rsrc -v ${BACKUP_THIS} /Volumes/${BACKUP_VOLUME_NAME} >> /Volumes/${BACKUP_VOLUME_NAME}/backup.log
done
IFS=$ORIGINAL_IFS



if [ $? -ne 0 ]
then
echo "[ERROR] Unable to copy files to disk image, ditto returned [$?]"
return $?
fi

echo "[INFO] Unmounting disk image"
hdiutil unmount /Volumes/${BACKUP_VOLUME_NAME}

if [ $? -ne 0 ]
then
echo "[ERROR] Unable to unmount disk image, hdiutil returned [$?]"
return $?
fi

if [ ${DO_BURN} == "on" ]
then
  echo "[INFO] About to burn disk image to CD/DVD"
  echo "[WARNING] Image is NOT password protected when it is burned to disc!"
  burn ${SPARSE_DMG}
else
  echo "[INFO] You did not choose to burn the image to a CD/DVD"
fi

if [ ${DO_UPLOAD} == "on" ]
then
  echo "[INFO] About to upload image to the server [${server}]"
  scp ${SPARSE_DMG} ${user}@${server}:${remoteBackupDir}
else
  echo "[INFO] You did not choose to upload the image to a server"
fi

#echo "[INFO] Detaching disk image"
hdiutil eject /Volumes/${BACKUP_VOLUME_NAME}

if [ $? -ne 0 ]
then
echo "[ERROR] Unable to detach disk image, hdiutil returned [$?]"
#return $?
fi


if [ ${DO_KEEP} == "on" ]
then
  echo "[WARNING] Leaving image on disk [${SPARSE_DMG}]"
else
  echo "[INFO] Removing image [${SPARSE_DMG}]"
  rm ${SPARSE_DMG}
fi

echo "[INFO] Done."
echo
}

function burn(){
echo "[INFO] Burning disk image"
hdiutil burn ${1} -noverifyburn -noeject

if [ $? -ne 0 ]
then
echo "[ERROR] Unable to burn disk image, hdiutil returned [$?]"
return $?
fi

}

### usage: calc <math expression>
### multiplication needs escape: \*
# FIXME: we removed the sed that trims the trailing zeros b/c
#        it was returning 14 instead of 140
#        we still need this functionality, but just for zeros
#        after the point
function calc {

# sed info: trim trailing 0s, trim trailing period, is this zero?
#bc -l <<< $@ | sed -e "s:0*$::" -e "s:\.$::" -e "s:^$:0:"
bc -l <<< $@ 
}

function round() {
INT=$(printf "%1.0f" $1)
NEAREST=${2:-10}

while ((INT % NEAREST)); do
   let INT++
done

echo $INT

}


function dump()
{

if [ ! $# -eq 1 ]
then
    echo
    echo "Usage:"
    echo "dump [command] "
    echo
    echo "For Example:"
    echo "dump mvn"
    echo
    return 1 
fi

# create a new FD, 3, and point it to the same place FD 1 is pointed
exec 3>&1 

DUMP_FILE=${1}_out.log
if [[ -e ${DUMP_FILE} ]]
then
    echo "Removing old log [${DUMP_FILE}]";
    rm ${DUMP_FILE}
fi

# Now, redirect FD 1 to point to the log file.
exec 1>${DUMP_FILE}

echo "about to execute ${1} "
# EXECUTE THE COMMAND
eval ${1}
echo "finished executing ${1}"

# Reset FD 1 to point to the same place as FD 3
exec 1>&3

}

with-temp-file ()
{
    local _prefix=$1; shift
    local _tmpfile_var=$1 ; shift
    local ${_tmpfile_var}=$(mktemp -t ${_prefix})
    eval local _tmpfile_value=\${$_tmpfile_var}
    eval "$@"
    rm ${_tmpfile_value}
}

edlo ()
{
    with-temp-file "edlo" tmpfile \
        '$(tail -1 $HISTFILE) > $tmpfile && $EDITOR $tmpfile'
}

eoo () 
{ 
    local _cmd="$@";
    with-temp-file "eoo" tmpfile '$_cmd > $tmpfile && $EDITOR $tmpfile'
}

function jaxb_install(){

echo "please use the separate jaxb install script in 3rdParty repo "
return 1;

JAR=${1}

# WE CHECK JAXB INTO THE MAVEN REPOSITORY USING
# THIS groupId BECAUSE WHEN WE USE 'jaxb-ri'
# MAVEN SEEMS TO USE AN IBIBLIO VERSION OF JAXB,
# REGARDLESS OF THE VERSION I SPECIFY!
NIGHTLY_JAXB=nightly-jaxb


if [[ -e ${1} ]]
then
    echo "Installing ${1}";
else
    echo "Can't find ${1}.";
    echo "usage: ./INSTALL_JAXB.sh JAXB_XXXVERSION.jar";
    return 1;
fi

read jarname ext < <(IFS=.; echo ${1})
read jaxbtext version < <(IFS=_; echo ${jarname})

echo "Version: ${version}"

java -jar ${1}

jaxbdir="jaxb-ri-${version}"

JAXB_LIBS=`pwd`"/${jaxbdir}/lib"

if [[ -e ${jaxbdir} ]]
then
    echo "Installing Maven libraries..."
else
    echo "JAXB was not unpacked "
    return 1;
fi

echo "LIBS: ${JAXB_LIBS}"
echo "...api"
mvn install:install-file \
-Dfile=${JAXB_LIBS}/jaxb-api.jar \
-DgroupId=${NIGHTLY_JAXB}     \
-DartifactId=jaxb-api \
-Dversion=${version} \
-Dpackaging=jar

echo "...xjc"
mvn install:install-file \
-Dfile=${JAXB_LIBS}/jaxb-xjc.jar \
-DgroupId=${NIGHTLY_JAXB}     \
-DartifactId=jaxb-xjc \
-Dversion=${version} \
-Dpackaging=jar

echo "...impl"
mvn install:install-file \
-Dfile=${JAXB_LIBS}/jaxb-impl.jar \
-DgroupId=${NIGHTLY_JAXB}     \
-DartifactId=jaxb-impl \
-Dversion=${version} \
-Dpackaging=jar

echo "...jsr173"
mvn install:install-file \
-Dfile=${JAXB_LIBS}/jsr173_1.0_api.jar \
-DgroupId=${NIGHTLY_JAXB}     \
-DartifactId=jsr173_1.0_api \
-Dversion=${version} \
-Dpackaging=jar

echo "...activation"
mvn install:install-file \
-Dfile=${JAXB_LIBS}/activation.jar \
-DgroupId=${NIGHTLY_JAXB}     \
-DartifactId=activation \
-Dversion=${version} \
-Dpackaging=jar


POMDIR=~/.m2/repository/${JAXB_LIBS}/${NIGHTLY_JAXB}/jaxb-impl/${version}
POMFILE="$POMDIR/jaxb-impl-${version}.pom.xml"

echo "Creating pom"
cat >> $POMFILE <<-EOF
<?xml version="1.0" encoding="UTF-8"?>
<project>
  <modelVersion>4.0.0</modelVersion>
  <groupId>nightly-jaxb</groupId>
  <artifactId>jaxb-impl</artifactId>
  <name>JAXB 2</name>
  <version>JAXB_NIGHTLY_VERSION</version>
  <description>JAXB 2 snapshot</description>
  <build />
  <dependencies>
    <dependency>
      <groupId>nightly-jaxb</groupId>
      <artifactId>jaxb-api</artifactId>
      <version>JAXB_NIGHTLY_VERSION</version>
    </dependency>

    <dependency>
      <groupId>nightly-jaxb</groupId>
      <artifactId>jaxb-xjc</artifactId>
      <version>JAXB_NIGHTLY_VERSION</version>
    </dependency>
    <dependency>
      <groupId>nightly-jaxb</groupId>
      <artifactId>activation</artifactId>
      <version>JAXB_NIGHTLY_VERSION</version>
    </dependency>
    <dependency>
      <groupId>nightly-jaxb</groupId>
      <artifactId>jsr173_1.0_api</artifactId>
      <version>JAXB_NIGHTLY_VERSION</version>
    </dependency>
  </dependencies>
</project>
EOF

THISDIR=`pwd`
cd ${POMDIR}
repl "JAXB_NIGHTLY_VERSION" "${version}" '*.pom'

cd $THISDIR
}


function classpath(){

    if [ ! $# -eq 1 ]
	then
	echo
	echo "Usage:"
	echo "classpath [directory path] "
	echo
	echo "For Example:"
	echo "classpath /my/path/to/lib/dir "
	echo
	return 1 
    else
      if [ -e ${1} ]
	  then
	  echo "[INFO] Adding all jars in ${1} to the CLASSPATH"
      else
	  echo "[ERROR] File [${1}] does not exist. "
	  return 1
      fi
    fi

    for JAR_FILE in `find ${1} -type f -name \*.jar`
      do
      echo "[INFO] Adding ${JAR_FILE}"

      IS_CP_SET=${CLASSPATH:-NOTSET}
      if [ ${IS_CP_SET} == "NOTSET" ]
	  then
	  export CLASSPATH="${JAR_FILE}"
      else
	  export CLASSPATH="${CLASSPATH}":${JAR_FILE}
      fi
    done

}

function mvn_usage(){
echo "Usage: mvn_install mylib-major.minor.jar";	
echo "   or: mvn_install something.jar artifactId groupId version [something.pom] "
}

function mvn_install(){

CURRENT_DIR=`pwd`
FILE_PATH="${CURRENT_DIR}/${1}"
FILE=`basename ${FILE_PATH}`
DIR=`dirname ${FILE_PATH}`

if [ $# -eq 0 ]
then
mvn_usage 
return;

fi


if [ ! -f ${FILE_PATH} ]
then
echo "[ERROR] File ${FILE_PATH} does not exist."

mvn_usage
return;
fi


if [ ! $# -eq 1 ]
then

 NAME=${2}
 GROUPID="${3}"
 VERSION=${4}

 if [ $# -eq 5 ]
 then
  echo "have 5 args"
  POM_FILE="${5}"
else
  # ISSUE: IF WE DON'T SET THIS TO SOMETHING (THAT DOESN'T EXIST)
  #        THE -n ${POM_FILE} -a -f ${POM_FILE} WILL RETURN TRUE! WHY? 
  POM_FILE="none" 
 fi

else 
 NAME=`echo "$FILE" | cut -d'-' -f1`
 VERSION_AND_EXTENSION=`echo "$FILE" | cut -d'-' -f2`
 #VERSION=`echo "${VERSION_AND_EXTENSION}" | cut -d'.' -f3`
 VERSION=`echo ${VERSION_AND_EXTENSION} |sed 's!\.jar$!!'`
 EXTENSION=`echo "${VERSION_AND_EXTENSION}" | cut -d'.' -f2`
 POM_FILE="${DIR}/${NAME}-${VERSION}.pom"

 echo "[INFO] Please enter the GroupID for the ${NAME} artifact."
 read -e GROUPID

fi

echo "[INFO] Adding ${GROUPID}.${NAME} [version: ${VERSION}] artifact to local Maven repository.";


CMD="mvn install:install-file  \
-Dfile=${FILE_PATH} \
-DgroupId=${GROUPID} \
-DartifactId=${NAME} \
-Dversion=${VERSION}  \
-Dpackaging=jar "

if [ -n ${POM_FILE} -a -f ${POM_FILE} ]
then
  echo "[INFO] Found pom file ${POM_FILE}."
  if ask -y "[INFO] Should I add this pom file with the artifact?"
  then
    POM=" -DpomFile=${POM_FILE}"
    COMMAND=${CMD}${POM}
  else
      COMMAND=${CMD}
  fi
else
      COMMAND=${CMD}
fi

if ask -y "[INFO] Should I execute now?"
then
 ${COMMAND}
fi

}

function nuke_usage(){

    echo
    echo "Usage:"
    echo "nuke [options] 'glob pattern' [list of target directories] "
    echo
    echo "For Example:"
    echo "nuke '*semantic_cache* docs' "
    echo

}

function nuke() {


# MUST RESET OPTIND
OPTIND=1
FILES_ONLY=off
DIRECTORIES_ONLY=off
DEFAULT_DIR=`pwd`
declare -a TARGET_DIRECTORIES
GLOB_PATTERN="NOTSPECIFIED"
HAVE_TARGET="NOTSPECIFIED"

while getopts "vfd" flag
do
    case "$flag" in
      v)  BE_VERBOSE=on;;
      f)  FILES_ONLY=on;;
      d)  DIRECTORIES_ONLY=on;;
      \?) nuke_usage
          return 1;;
    esac
done

if [ ${FILES_ONLY} == "on" ]
    then
    TYPE=" -t f "
elif [ ${DIRECTORIES_ONLY} == "on" ]
    then
    TYPE=" -t d "
else
    TYPE=
fi 


let LAST_OPTION=${OPTIND}
let LAST_OPTION--

let LAST_OPTION_IDX=LAST_OPTION
let LAST_OPTION_IDX--

# ---------------------------------------------------
# FIND LIST OF FILES TO BE BACKED UP
# ---------------------------------------------------
count=0
FILE_COUNT=0
if [ $# -gt ${LAST_OPTION_IDX} ]
then
   for file in "$@"
   do
    #echo you typed ${file} ${count}.
    if [ $count -gt ${LAST_OPTION_IDX} ]
    then
	#echo "before blah $count"
	if [ $count -lt 1 ]
	    then

	    # THE FIRST ARG IS THE GLOB PATTERN
	    GLOB_PATTERN=${file}
	    echo "[INFO] Pattern is ${GLOB_PATTERN}"
	else
	    HAVE_TARGET="TRUE"
	    echo "[TRACE] Adding file ${file} to nuke list"
	    TARGET_DIRECTORIES[FILE_COUNT]="${file}"

	    let FILE_COUNT++
	fi
    fi

    let count++

   done
fi


if [ ${GLOB_PATTERN} == "NOTSPECIFIED" ]
then
    nuke_usage
    return 1
fi

if [ ${HAVE_TARGET} == "NOTSPECIFIED" ]
then
    echo "[INFO] No directories specified, will use current dir [${DEFAULT_DIR}]"
    TARGET_DIRECTORIES[0]=${DEFAULT_DIR}
    let FILE_COUNT++
fi 


ORIGINAL_IFS=$IFS
IFS=$'\n'
for ((i=0;i<$FILE_COUNT;i++)); do
   
   DIR="${TARGET_DIRECTORIES[${i}]}"
   echo "[INFO] Processing next directory [${DIR}]"
   if [ ! -e ${DIR} ]
   then
     echo "[ERROR] ${DIR} does not exist."
     return;
   elif [ ! -d ${DIR} ]
   then
     echo "[ERROR] ${DIR} is not a directory."
     return;
   fi 

   DO_FIND="find ${DIR} ${TYPE} -name '${1}'"
   echo $DO_FIND
   eval $DO_FIND

   echo "[WARNING] Are you sure you want to delete all the files listed above?"
   if ask -n "Push the button? "
       then
       
       # Find all files by pattern
       for file in `eval $DO_FIND`
	 do
	 echo "+ Removing ..... $file"
	 rm -rf $file
       done
   fi

done
IFS=$ORIGINAL_IFS

}

function repl(){


# MUST RESET OPTIND
OPTIND=1

declare -a REPL_ARGS
BE_QUIET=off
DELIM="@"

while getopts "qd:" flag
do
    case "$flag" in
      q)  BE_QUIET=on;;
      d)  DELIM="${OPTARG}";;
      \?) echo
          echo "Usage:"
          echo "replace \"old text\" \"new text\" '[glob pattern]' "
          echo
          echo "For Example:"
          echo "replace \"sites/clientX\" \"/new/clientX\" '*.html'"
          echo
    
          return 1;;
    esac
done


let LAST_OPTION=${OPTIND}
let LAST_OPTION--

let LAST_OPTION_IDX=LAST_OPTION
let LAST_OPTION_IDX--

# ---------------------------------------------------
# FIND REGEX
# ---------------------------------------------------
count=0
ARG_COUNT=0
if [ $# -gt ${LAST_OPTION_IDX} ]
then
   for rarg in "$@"
   do
    #echo you typed ${file} ${count}.
    if [ $count -gt ${LAST_OPTION_IDX} ]
    then
        REPL_ARGS[ARG_COUNT]="${rarg}"

	let ARG_COUNT++
    fi

    let count++

   done

fi 

DO_REPLACE="on"
if [ ${BE_QUIET} == "off" ]
then
echo "[WARNING] You must ensure all delimiter charaters in your input strings are escaped." 

if ask -y "The delimiter character is ${DELIM}. Do you wish to continue? "
then
DO_REPLACE="on"
else
DO_REPLACE="off"
fi
fi


if [ ${DO_REPLACE} == "on" ]
then
    DIR=`pwd`
    FIND=`which find`

    #DO_FIND="${FIND} ${DIR} -name '${3}'"
    #REP="${DO_FIND} | xargs perl -pi -e 's${DELIM}${1}${DELIM}${2}${DELIM}g'"

    DO_FIND="${FIND} ${DIR} -name '${REPL_ARGS[2]}'"
    REP="${DO_FIND} | xargs perl -pi -e 's${DELIM}${REPL_ARGS[0]}${DELIM}${REPL_ARGS[1]}${DELIM}g'"
    #echo "Just testing:" ${REP}
    eval ${REP}
    return;
fi

}



function rm_backup() {
#Clear shell
#clear

#Check for a  parameter
if [ ! $# -eq 1 ]
then echo "  ********** ERROR **********"
     echo "  Usage: rm_backup /path/to/directory"
     echo ""
     return 1
fi

#Check if first parameter is a directory
if [ ! -d $1 ]
then 
    echo "  ********** ERROR **********"
     echo "   $1   is not a directory !!"
     echo ""
     return 1
fi

#Find all backup files and remove them
for file in `find $1 -type f -name "*~*"`
do
   echo "+ Removing ..... $file"
   rm -f $file
done

}

function rm_svn() {
#Clear shell
#clear

#Check for a  parameter
if [ ! $# -eq 1 ]
then 
    echo "  ********** ERROR **********"
     echo "  Usage: rm_svn /path/to/directory"
     echo ""
     return 1
fi

#Check if first parameter is a directory
if [ ! -d $1 ]
then 
    echo "  ********** ERROR **********"
     echo "  $1 is not a directory !!"
     echo ""
     return 1
fi

#Find all backup files and remove them
#for file in `find $1 -type d -name "*.svn*"`
for file in `find . -type f -path "./*.svn*"`
do
   echo "+ Removing ..... $file"
   rm -f $file
done


for file in `find . -type d -path "./*.svn/tmp*"`
do
   echo "+ Removing ..... $file"
   rmdir  $file
done


for file in `find . -type d -path "./*.svn*"`
do
   echo "+ Removing ..... $file"
   rmdir  $file
done

}


function unpack_this_dir(){

echo " "
echo "Unpacking archives in this directory."
for file in ./*.tar.gz
do
 echon "Unpacking $file ..."
 tar -zxf $file
 echo " OK."
done
}


echon () {
            if [ X"$ECHON" = X ]
            then
                # Determine how to "echo" without newline: "echo -n"
                # or "echo ...\c"
                if [ X`echo -n` = X-n ]
                then ECHON=echo; NNL="\c"
                else ECHON="echo -n"; NNL=""
                fi

            fi
            $ECHON "$*$NNL"
        }


#echo "Do you really want to remove 1230 files (yes/no)?"
#The script "ask()" allows for easy prompting, e.g.
#if ask -y "really remove $n files"
#then
#  rm -f "$allfiles"
#fi
ask() {
          #
          # ask a question with a yes/no answer;
          # Usage: ask [-y|-n] "question"
          # Set default: -y = yes; -n = no; otherwise no default;
          # Returns: 0 (true) = yes; 1 (false) = no;
          # Note: changing the default does not effect the return value;
          #
          ASK_DFLT=
          # process options/args
          for ASK_OPT do
                case "$ASK_OPT" in
                  -[yY]*) ASK_DFLT='y' ;;
                  -[nN]*) ASK_DFLT='n' ;;
                  --) shift; break ;;
                  -*) ;;
                  *) break
                  esac
                shift
            done
          ASK_PROMPT="$*"

          # get the response
          while : ; do
                echon "$ASK_PROMPT (y/n)?${ASK_DFLT:+ [$ASK_DFLT]} " >&2
                read ASK_ANSWER ASK_JUNK

                : ${ASK_ANSWER:=$ASK_DFLT}

                case "$ASK_ANSWER" in
                  [yY]*)
                        return 0
                        ;;
                  [nN]*)
                        return 1
                        ;;
                  *)
                        echo " " >&2
                  esac
            done

          return
        }



function exists(){

	if [ -d ${1} ]
	    then
	    echo "ok directory ${1} exists"
	else

	    if [ -f ${1} ]
		then
		echo "ok file ${1} exists"
	    else

		echo "${1} does not exist"

		return 1
	    fi
	fi
}

function toLowercase() {
for filename in *    # Not necessary to use basename,
                     # since "*" won't return any file containing "/".
do n=`echo "$filename/" | tr '[:upper:]' '[:lower:]'`
#                             POSIX char set notation.
#                    Slash added so that trailing newlines are not
#                    removed by command substitution.
   # Variable substitution:
   n=${n%/}          # Removes trailing slash, added above, from filename.
   [[ $filename == $n ]] || mv "$filename" "$n"
                     # Checks if filename already lowercase.
done

}

function dos2unix(){

    if [ $# == 1 ]
	then
	if [ -d ${1} ]
	    then
	    echo "ok directory ${1} exists, replacing ${1} for ${2}"   
	else
	    echo "${1} is not a directory"
            echo "Usage: dos2unix [myfile|mydir]"
	    return 1
	fi
    else
	echo "Usage: dos2unix [myfile|mydir]"
	return 1
    fi

    DIR=`pwd`


    for file in `ls -r ${1}`
    do
      echo "${1}/${file}"
      if [ -d ${1}/${file} ]
	  then
	   dos2unix ${DIR}${1}/${file}
      else
	  # requires perl
	  #CMD="${HOME}/development/sysadmin/bin/dos2unix.pl ${DIR}/${1}/${file}"
	  #RESULT=`${CMD}`

	  E_WRONGARGS=65

	  ORIG_FILE=${DIR}/${1}/${file}
          NEWFILENAME=${ORIG_FILE}.unx

          CR='\015'  # Carriage return.
          # Lines in a DOS text file end in a CR-LF.

          tr -d $CR < $1 > $NEWFILENAME
          # Delete CR and write to new file.

          #echo "Original DOS text file is \"$ORIG_FILE\"."
          #echo "Converted UNIX text file is \"$NEWFILENAME\"."
	  
          if ask -y "replace original \"$ORIG_FILE\" with \"$NEWFILENAME\"?"
          then
	    rm ${ORIG_FILE}
	    mv ${NEWFILENAME} ${ORIG_FILE}
          fi

      fi
    done

}

