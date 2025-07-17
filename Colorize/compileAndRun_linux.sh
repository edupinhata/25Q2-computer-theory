# This script considers that kotlinc is installed and set in the PATH
cd src;
kotlinc Main.kt Graph.kt SymmetricBinaryMatrix.kt -include-runtime -d ../myapp.jar;
cd ..;
java  -Xmx14000m -jar myapp.jar