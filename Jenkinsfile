pipeline {
    agent any
    tools {
        maven 'Maven'
    }
    stages {
        stage('Ejecutar Job') {
            steps {
                wrap([$class: 'Xvfb', autoDisplayName: true, debug: true, displayNameOffset: 0, installationName: 'XVFB', screen: '1920x1080x24']) {
                        sh 'mvn clean test -DproxySet=true -Dhttp.proxyHost=10.97.67.136 -Dhttp.proxyPort=30128 -Dhttps.proxyHost=10.97.67.136 -Dhttps.proxyPort=30128'
                 }
            }
            post{
                always {
                    publishHTML (
                        target: [
                            allowMissing: false,
                            alwaysLinkToLastBuild: false,
                            includes: '**/*.html,**/img/*.png',
                            keepAll: true,
                            reportDir: 'test-report',
                            reportFiles: 'AUT-MDM-ANTEL.html',
                            reportName: "AUT-MDM-ANTEL"
                        ]
                    )
                }
            }
        }
    }
}