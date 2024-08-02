 #!/bin/bash

compile (){

    echo "[COMPILATION]..."

    if [ ! -d "bin" ]; then
        mkdir bin
    fi

    find -name "*.java" > sources.txt
    javac -d bin @sources.txt
    rm sources.txt

    echo "[COMPILATION TERMINÉE]!"

}

run (){

    echo "[LANCEMENT]..."

    java -cp bin/ server/MainServer

}

compile && run
