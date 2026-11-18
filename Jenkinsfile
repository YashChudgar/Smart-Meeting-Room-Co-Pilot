
pipeline {

    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '10'))
        skipDefaultCheckout(true)
    }

    triggers {
        pollSCM('H/5 * * * *')
    }

    tools {
        jdk 'JDK21'
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out source code from GitHub...'
                checkout scm
            }
        }

        stage('Check Environment') {
            steps {
                echo 'Checking Java and Git versions...'
                bat 'java -version'
                bat 'git --version'
            }
        }

        stage('Build and Test') {
            steps {
                echo 'Building project and executing automated tests...'
                bat 'mvnw.cmd -B clean verify'
            }
        }

        stage('Build Artifact') {
            steps {
                echo 'Spring Boot application package generated.'
                archiveArtifacts artifacts: 'target/*.jar',
                                 allowEmptyArchive: false
            }
        }
    }

    post {
        always {
            junit testResults: 'target/surefire-reports/*.xml',
                  allowEmptyResults: true
        }

        success {
            echo 'SUCCESS: Jenkins CI pipeline completed successfully.'
        }

        failure {
            echo 'FAILURE: Jenkins pipeline failed. Check the console output.'
        }
    }
}
