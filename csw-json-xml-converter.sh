#!/bin/bash

# **************************************
# ************ File Headers ************
# **************************************

<<FILE-INFO
  This Script file :
		1. Update apt-get
		2. Install curl
		3. Installs Java, Git and Gradle
		4. Clone a a Repo from github
		5. Build a Jar from the cloned Repo
FILE-INFO

# **************************************
# *********** Global Variables *********
# **************************************

_MODE="$(echo $1 | tr 'a-z' 'A-Z')"
_WORK_DIR="/opt/csw"
_JAVA_VERSION="14.0.2"
_GRADLE_VERSION="6.5"
_REPO_NAMESPACE="praveenkumar-ramachandran"
_REPO_NAME="csw-json-xml-converter"
_BRANCH_NAME="main"
_REPO_BUILD_VERSION="1.0.0-SNAPSHOT"
_ARCHIVE_TYPE_TAR_GZ="tar.gz"
_ARCHIVE_TYPE_ZIP="zip"

_INPUT_JSON_FILE_PATH=$2
_OUTPUT_XML_FILE_PATH=$3

_MODE_INSTALL="INSTALL" # default mode
_MODE_UPDATE="UPDATE"
_MODE_RUN="RUN"

if [[ -z "$_MODE" ]]; then
	_MODE="$_MODE_INSTALL"
fi

# **************************************
# ************** Functions *************
# **************************************

execute_apt_get_cmd() {
	COMMAND=$1
	APPLICATION=$2
	shift;shift;
	ARGS=$@
	log_info "Installing or Updating $APPLICATION"
	apt-get $COMMAND $APPLICATION $ARGS
	stop_if_error_on_last_cmd "Error in Installing or Updating $APPLICATION" "Completed Installing or Updating $APPLICATION"
}

apt_get_install() {
	execute_apt_get_cmd install $1 -y
}

run_cmd_and_exit_on_error() {
	COMMAND=$1
	FAILURE_MSG=$1
	SUCCESS_MSG=$2
	eval "$COMMAND"
	stop_if_error_on_last_cmd "$FAILURE_MSG" "$SUCCESS_MSG"
}

make_dir_if_not_present() {
	DIRECTORY_PATH=$1
	if [[ -z "$DIRECTORY_PATH" ]]; then
		log_failure "Dirctory Path Parameter is Invalid, Provided DIRECTORY_PATH : $DIRECTORY_PATH"
		exit 1
	fi
	if [[ ! -d "$DIRECTORY_PATH" ]]; then
		mkdir -p "$DIRECTORY_PATH"
		stop_if_error_on_last_cmd "Error in creating directory : $DIRECTORY_PATH"
	fi
}

stop_if_error_on_last_cmd() {
	FAILURE_MSG=$1
	SUCCESS_MSG=$2
	if [ $? -ne 0 ]; then
		if [[ -z "$SUCCESS_MSG" ]]; then
			log_failure "The Last Command has returned a non-zero exit code"
		else
			log_failure "$FAILURE_MSG"
		fi
		exit 1
	fi
	if [[ -z "$SUCCESS_MSG" ]]; then
		# parameter is empty, so skip message
		:
	else
		log_info  "$SUCCESS_MSG"
	fi
}

curl_install() {
	
	APPLICATION=$1
	FILE_URL=$2
	ARCHIVE_FILE_NAME=$3
	FILE_NAME=$4
	ARCHIVE_TYPE=$5
	EXPORT_VARIABLE=$6
	FILE_NAME_NEW=$7
	
	log_info "Installing $APPLICATION"
	
	## validate archive file name parameter
	if [[ -z "$ARCHIVE_FILE_NAME" ]]; then
		log_failure "Archive File Name Parameter is Invalid, Provided ARCHIVE_FILE_NAME : $ARCHIVE_FILE_NAME"
		exit 1
	fi

	## validate file name parameter
	if [[ -z "$FILE_NAME" ]]; then
		log_failure "File Name Parameter is Invalid, Provided FILE_NAME : $FILE_NAME"
		exit 1
	fi
	
	## validate file URL and download the file
	if [[ -z "$FILE_URL" ]]; then
		log_failure "Url Parameter is Invalid, Provided FILE_URL: $FILE_URL"
		exit 1
	else
		log_info "Downloading File : $FILE_URL"
		curl -L -O $FILE_URL
		stop_if_error_on_last_cmd "Unable to download the file from : $FILE_URL"
	fi
	
	## validate archive type parameter and extract the archive file
	FILE_NAME_WITH_EXT="$ARCHIVE_FILE_NAME.$ARCHIVE_TYPE"
	if [[ -z "$ARCHIVE_TYPE" ]]; then
		log_failure "Archive Type Parameter is Invalid, Provided ARCHIVE_TYPE : $ARCHIVE_TYPE"
		exit 1
	elif [[ "$ARCHIVE_TYPE" = "$_ARCHIVE_TYPE_TAR_GZ" ]]; then
		tar -xf "$FILE_NAME_WITH_EXT"
		stop_if_error_on_last_cmd "Unable to extract the archive file : $FILE_NAME_WITH_EXT"
	elif [[ "$ARCHIVE_TYPE" = "$_ARCHIVE_TYPE_ZIP" ]]; then
		unzip "$FILE_NAME_WITH_EXT"
		stop_if_error_on_last_cmd "Unable to extract the archive file : $FILE_NAME_WITH_EXT"
	else
		log_failure "Undefined Archive Type Parameter, Provided ARCHIVE_TYPE : $ARCHIVE_TYPE"
		exit 1
	fi
	
	## Remove the archive file
	rm "$FILE_NAME_WITH_EXT"
	stop_if_error_on_last_cmd "Unable to remove the archive file : $FILE_NAME_WITH_EXT"

	## rename the folder if provided a new file name
	if [[ -z "$FILE_NAME_NEW" ]]; then
		# parameter is empty, so skip folder rename process
		:
	else
		mv "$FILE_NAME" "$FILE_NAME_NEW"
		stop_if_error_on_last_cmd "Unable to rename the file from : $FILE_URL"
		FILE_NAME=$FILE_NAME_NEW
	fi

	## export the variable with the folder if provided a new file name
	if [[ -z "$EXPORT_VARIABLE" ]]; then
		# parameter is empty, so skip export variable process
		:
	else
		run_export_cmd "$EXPORT_VARIABLE" "$_WORK_DIR/$FILE_NAME"
	fi

	log_info "Completed Installing $APPLICATION"

}

run_export_cmd() {
	EXPORT_VARIABLE=$1
	EXPORT_VARIABLE_PATH=$2
	export "$EXPORT_VARIABLE"="$EXPORT_VARIABLE_PATH"
	export PATH="$PATH:$EXPORT_VARIABLE_PATH/bin"
}

create_work_dir() {
	## if mode is install, create a backup for old working directory and create a new work directory
	WORK_DIRECTORY="$_WORK_DIR"
	DATE_TIME="$(echo $(date '+%Y-%m-%d-%H-%M-%S'))"
	BACKUP_DIRECTORY="$_WORK_DIR-backup-$DATE_TIME"
	if [[ "$_MODE" == "$_MODE_INSTALL" ]]; then
		if [[ -d "$WORK_DIRECTORY" ]]; then
			log_info "Work Directory exists already, creating backup : $BACKUP_DIRECTORY"
			run_cmd_and_exit_on_error "mv $WORK_DIRECTORY $BACKUP_DIRECTORY"
			run_cmd_and_exit_on_error "mkdir $WORK_DIRECTORY"
		else
			log_info "Work Directory not exists, creating : $WORK_DIRECTORY"
			run_cmd_and_exit_on_error "mkdir $WORK_DIRECTORY"
		fi
	fi
	if [[ -d "$WORK_DIRECTORY" ]]; then
		# parameter is empty, so skip folder rename process
		:
	else
		log_failure "Work Directory not available, Run $_MODE_INSTALL mode and proceed further"
		exit 1
	fi
}

install_tools() {
	if [[ "$_MODE" == "$_MODE_INSTALL" ]]; then
		
		## update apt-get
		apt-get update
		# apt_get_install jq
		apt_get_install curl
		apt_get_install tar
		apt_get_install gzip
		apt_get_install zip
		apt_get_install unzip
		apt_get_install git-all
		
		## craete the work DIR if not exists
		make_dir_if_not_present "$_WORK_DIR"

	else
		log_info "Skipped installing TOOLS for $_MODE mode"
	fi
}

install_java() {
	JDK_FILE_NAME="jdk-$_JAVA_VERSION"
	JDK_EXPORT_VARIABLE="JAVA_HOME"
	JDK_EXPORT_VARIABLE_PATH="$_WORK_DIR/$JDK_FILE_NAME"
	if [[ "$_MODE" == "$_MODE_INSTALL" ]]; then
		run_cmd_and_exit_on_error "cd $_WORK_DIR"
		# https://download.java.net/java/GA/jdk14.0.2/205943a0976c4ed48cb16f1043c5c647/12/GPL/openjdk-14.0.2_linux-x64_bin.tar.gz
		JDK_ARCHIVE_FILE_NAME_PREFIX="openjdk-$_JAVA_VERSION"
		JDK_ARCHIVE_FILE_NAME_SUFFIX="_linux-x64_bin"
		JDK_ARCHIVE_FILE_NAME="$JDK_ARCHIVE_FILE_NAME_PREFIX$JDK_ARCHIVE_FILE_NAME_SUFFIX"
		JDK_DOWNLOAD_URL="https://download.java.net/java/GA/jdk$_JAVA_VERSION/205943a0976c4ed48cb16f1043c5c647/12/GPL/$JDK_ARCHIVE_FILE_NAME.tar.gz"
		curl_install "$JDK_FILE_NAME" "$JDK_DOWNLOAD_URL" "$JDK_ARCHIVE_FILE_NAME" "$JDK_FILE_NAME" "$_ARCHIVE_TYPE_TAR_GZ" "$JDK_EXPORT_VARIABLE" ""
		log_success "Completed installing JAVA for $_MODE mode"
	else
		run_export_cmd "$JDK_EXPORT_VARIABLE" "$JDK_EXPORT_VARIABLE_PATH"
		log_info "Skipped installing JAVA for $_MODE mode"
	fi
}

install_gradle() {
	GRADLE_FILE_NAME="gradle-$_GRADLE_VERSION"
	GRADLE_EXPORT_VARIABLE="GRADLE_HOME"
	GRADLE_EXPORT_VARIABLE_PATH="$_WORK_DIR/$GRADLE_FILE_NAME"
	if [[ "$_MODE" == "$_MODE_INSTALL" ]]; then
		run_cmd_and_exit_on_error "cd $_WORK_DIR"
		# https://services.gradle.org/distributions/gradle-6.5-bin.zip
		GRADLE_ARCHIVE_FILE_NAME="gradle-$_GRADLE_VERSION-bin"
		GRADLE_DOWNLOAD_URL="https://services.gradle.org/distributions/gradle-$_GRADLE_VERSION-bin.zip"
		curl_install "$GRADLE_FILE_NAME" "$GRADLE_DOWNLOAD_URL" "$GRADLE_ARCHIVE_FILE_NAME" "$GRADLE_FILE_NAME" "$_ARCHIVE_TYPE_ZIP" "$GRADLE_EXPORT_VARIABLE" ""
		log_success "Completed installing GRADLE for $_MODE mode"
	else
		run_export_cmd "$GRADLE_EXPORT_VARIABLE" "$GRADLE_EXPORT_VARIABLE_PATH"
		log_info "Skipped installing GRADLE for $_MODE mode"
	fi
}

install_git_repo() {
	if [[ "$_MODE" == "$_MODE_INSTALL" ]]; then
		run_cmd_and_exit_on_error "cd $_WORK_DIR"
		## install json-xml-converter using curl
		# https://github.com/praveenkumar-ramachandran/csw-json-xml-converter/archive/main.zip
		# _REPO_FILE_NAME="$_REPO_NAME-$_BRANCH_NAME"
		# _REPO_ARCHIVE_FILE_NAME="$_BRANCH_NAME"
		# _REPO_DOWNLOAD_URL="https://github.com/$_REPO_NAMESPACE/$_REPO_NAME/archive/$_BRANCH_NAME.zip"
		# curl_install "$_REPO_FILE_NAME" "$_REPO_DOWNLOAD_URL" "$_REPO_ARCHIVE_FILE_NAME" "$_REPO_FILE_NAME" "$_ARCHIVE_TYPE_ZIP" "" "$_REPO_NAME"

		## install json-xml-converter using git cli
		# https://github.com/praveenkumar-ramachandran/csw-json-xml-converter.git
		CLONE_URL="https://github.com/$_REPO_NAMESPACE/$_REPO_NAME.git"
		run_cmd_and_exit_on_error "git clone $CLONE_URL" "Unable to clone From : $CLONE_URL"
		log_success "Completed installing $_REPO_NAME for $_MODE mode"
	elif [[ "$_MODE" == "$_MODE_UPDATE" ]]; then
		run_cmd_and_exit_on_error "cd $_WORK_DIR/$_REPO_NAME"
		run_cmd_and_exit_on_error "git checkout $_BRANCH_NAME"
		run_cmd_and_exit_on_error "git pull"
		log_success "Completed Updating $_REPO_NAME for $_MODE mode"
	else
		log_info "Skipped Install / Update for $_REPO_NAME for $_MODE mode"
	fi
}

create_jar() {
	## run gradle command
	if [[ "$_MODE" == "$_MODE_INSTALL" || "$_MODE" == "$_MODE_UPDATE" ]]; then
		if [[ ! -d "$_WORK_DIR/$_REPO_NAME" ]]; then
			log_failure "Repo not available : $_REPO_NAME"
			exit 1
		else
			run_cmd_and_exit_on_error "cd $_WORK_DIR/$_REPO_NAME"
		fi
		log_info "Building Jar for $_REPO_NAME"
		run_cmd_and_exit_on_error "gradle clean build install publish -i" "Unable to build the jar file for : $_REPO_NAME"
		log_success "Completed Jar Creation $_REPO_NAME for $_MODE mode"
	else
		log_info "Skipped Jar Creation for $_REPO_NAME for $_MODE mode"
	fi
}

run_jar() {
	if [[ "$_MODE" == "$_MODE_RUN" ]]; then
		JAR_FILE_PATH="$_WORK_DIR/$_REPO_NAME/build/libs/$_REPO_NAME-$_REPO_BUILD_VERSION-all.jar"
		if [[ -e "$JAR_FILE_PATH" ]]; then
			run_cmd_and_exit_on_error "java -jar $JAR_FILE_PATH $_INPUT_JSON_FILE_PATH $_OUTPUT_XML_FILE_PATH"
		else
			log_failure "Jar file Not Found : $JAR_FILE_PATH"
			exit 1
		fi
	else
		log_info "Skipped running jar for $_REPO_NAME for $_MODE mode"
	fi
}

log_info() {
	MESSAGE=$1
	echo "INFO     : $MESSAGE"
}

log_failure() {
	MESSAGE=$1
	echo "FAILED   : $MESSAGE"
}

log_success() {
	MESSAGE=$1
	echo "SUCCESS  : $MESSAGE"
}

# **************************************
# ********* Script Execution ***********
# **************************************

echo "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
echo "START    : Executing install-json-xml-converter.sh"
echo "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"

log_info "Executing $_MODE mode"

## create work directory based on mode
create_work_dir

## install necessary packages
install_tools

## install java
install_java

## install gradle
install_gradle

## install json-xml-converter
install_git_repo

## create jar file
create_jar

## Run the jar file with provided parameters, only on run mode
run_jar

echo "------------------------------------------------------------------------------------------------------"
echo "END      : Successfully executed install-json-xml-converter.sh "
echo "------------------------------------------------------------------------------------------------------"
