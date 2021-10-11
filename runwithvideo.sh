mvn clean test
allure generate target/allure-results -o allure-report --clean
for i in target/*.mov; do ffmpeg -i "$i" "${i%.*}.mp4"; done
echo Video transcoding success
mv target/*.mp4 allure-report/data/attachments
