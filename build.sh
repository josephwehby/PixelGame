mkdir -p out

javac $(find src -name "*.java") -d out

java -cp out game.Main
