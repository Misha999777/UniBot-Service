rsync -crlOt ./target/UniBot.jar ubuntu@ec2-18-189-141-10.us-east-2.compute.amazonaws.com:/home/ubuntu/unibot/UniBot.jar
ssh ubuntu@ec2-18-189-141-10.us-east-2.compute.amazonaws.com sudo service unibot restart
