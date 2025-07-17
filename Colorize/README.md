# Colorize Problem

## Run instructions

To run, compile the code in `src` with the output in the Colorize main path.

You can also use the script `compileAndRun_<os>.sh`. 

- **MaxOS:** consider that `kotlinc` is installed in `/opt/homebrew`. Change it if necessary.
- **Linux:** consider that `kotlinc` can be called anywhere (is in PATH).

### Memory
The scripts have a flag to determine how many RAM the java VM will use. Change it if necessary.

### Determine which instance to run

The `Main.kt` has a list with all instances in `resources` directory. Uncomment the ones that you want to run.


## Results

These are the results obtained running the following instances in a Macbook Air M3. 

| Filename              | Colors Number | Execution Time (ms) |
|-----------------------|---------------|---------------------|
| dsjc250.5.col.txt     | 189           | 5666                |
| dsjc500.1.col.txt     | 88            | 1122                |
| dsjc500.5.col.txt     | 377           | 70930               |
| dsjc500.9.col.txt     | 659           | 8792908             |
| dsjc1000.1.col.txt    | 159           | 14621               |
| dsjc1000.5.col.txt    | 735           | 2006689             |
| dsjc1000.9.col.txt    | memory        |                     |
| dsjr500.1c.col.txt    | 698           | 1466743             |
| dsjr500.5.col.txt     | 461           | 42520               |
| flat300_28_0.col.txt  | 219           | 10611               |
| flat1000_50_0.col.txt | 712           | 1784328             |
| flat1000_60_0.col.txt | 710           | 4067076             | 
| flat1000_76_0.col.txt | 701           | 1923028             | 
| latin_square.col.txt  | memory        |                     |
| le450_25c.col.txt     | 180           | 1820                |
| le450_25d.col.txt     | 167           | 2170                |
| r250.5.col.txt        | 222           | 3667                |
| r1000.1c.col.txt      | memory        |                     |
| r1000.5.col.txt       | 918           | 882594              |
| c2000.5.col.txt       | memory        |                     |
| c4000.5.col.txt       | memory        |                     |
