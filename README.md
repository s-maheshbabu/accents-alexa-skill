Install ask-cli
Install aws-cli

To generate the jar, run mvn org.apache.maven.plugins:maven-assembly-plugin:2.6:assembly -DdescriptorId=jar-with-dependencies package

To upload to Lambda, run aws lambda update-function-code --region us-east-1 --function-name arn:aws:lambda:us-east-1:837603326872:function:accents-alexa-skill-dev --zip-file fileb://target/accents-alexa-skill-1.0-jar-with-dependencies.jar