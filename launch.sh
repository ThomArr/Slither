 #!/bin/bash

compile (){

    echo "[COMPILATION]..."

    find -name "*.java" > sources.txt
    javac -d bin @sources.txt
    rm sources.txt

    echo "[COMPILATION TERMINÉE]!"

}

run (){

    echo "[LANCEMENT]..."

    java -cp bin/ main/Main

}

compile && run
