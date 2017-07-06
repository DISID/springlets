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
    stage('Integration tests') {
      steps {
        withMaven(maven: 'M3') {
          sh '''mvn --batch-mode -V -U -e verify -Dsurefire.useFile=false
'''
        }
        
      }
    }
  }
}