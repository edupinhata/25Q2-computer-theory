# This script considers that kotlinc was installed using Brew and is located in /opt/homebrew
cd src;
/opt/homebrew/bin/kotlinc Main.kt Graph.kt SymmetricBinaryMatrix.kt  AdjacencyStructure.kt -include-runtime -d ../$1; 
cd ..;
java  -Xmx14000m -jar $1
