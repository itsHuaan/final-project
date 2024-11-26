pipeline {
    agent any
    stages {
        stage('Build'){
            when { tag "dev_*" }
            steps {
                sh 'cd /var/lib/jenkins/workspace/asdad_${TAG_NAME}/'
                sh 'cp /var/lib/jenkins/workspace/scripts/intern/api03.sh deploy.sh'
                sh "chmod +x deploy.sh"
                sh './deploy.sh ${TAG_NAME}'
            }
        }
        // stage('Delete old tag folder') {
        //     when { tag "dev_*" }
        //     steps {
        //         echo 'Deleting'
        //         dir('/var/lib/jenkins/workspace/') {
        //             script{
        //                 def filesToDelete = sh(
        //                     returnStdout: true,
        //                     script: "ls -td CY_MS-Comm-BE_* | awk 'NR>6'"
        //                 ).trim()
        //                 def fileList = filesToDelete.split('\n')
        //                 for (String file : fileList) {
        //                     sh "sudo chown -R jenkins:jenkins $file"
        //                 }
        //             }
        //             sh "ls -td CY_MS-Comm-BE_* | awk 'NR>6' | xargs -r rm -rf"
        //         }
        //     }
        // }
    }
}
