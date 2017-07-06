pipeline {
  agent any
  stages {
    stage('Compile') {
      steps {
        withMaven(maven: 'M3') {
          sh '''mvn --batch-mode -V -U -e clean compile -Dsurefire.useFile=false
'''
        }
        
      }
    }
    stage('Unit tests') {
      steps {
        withMaven(maven: 'M3') {
          sh '''mvn --batch-mode -V -U -e test -Dsurefire.useFile=false
'''
        }
        
      }
    }
    stage('Source code quality') {
      steps {
        withMaven(maven: 'M3') {
          sh '''mvn --batch-mode -V -U -e  org.jacoco:jacoco-maven-plugin:prepare-agent test sonar:sonar -Dsurefire.useFile=false

'''
        }
        
      }
    }
  }
}