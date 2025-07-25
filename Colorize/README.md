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

### Report of implementation

- [v1](./report/Edge_Coloring_Graph_Implementation.pdf)

### Table with executions results

These are the results obtained running the following instances in a Macbook Air M3, 2024, 16Gb, macOs Sequoia 15.5. 

| Filename              | Colors Number | Execution Time (s) |
|-----------------------|---------------|--------------------|
| dsjc250.5.col.txt     | 189           | 5,6                |
| dsjc500.1.col.txt     | 88            | 1,1                |
| dsjc500.5.col.txt     | 377           | 7,0                |
| dsjc500.9.col.txt     | 659           | 8.792,9            |
| dsjc1000.1.col.txt    | 159           | 14,6               |
| dsjc1000.5.col.txt    | 735           | 2.006,6            |
| dsjc1000.9.col.txt    | 1288          | 15.931,1           |
| dsjr500.1c.col.txt    | 698           | 1.466,7            |
| dsjr500.5.col.txt     | 461           | 42,5               |
| flat300_28_0.col.txt  | 219           | 10,6               |
| flat1000_50_0.col.txt | 712           | 1.784,3            |
| flat1000_60_0.col.txt | 710           | 4.067              | 
| flat1000_76_0.col.txt | 701           | 1.923              | 
| latin_square.col.txt  | memory        |                    |
| le450_25c.col.txt     | 180           | 1,8                |
| le450_25d.col.txt     | 167           | 2,1                |
| r250.5.col.txt        | 222           | 3,6                |
| r1000.1c.col.txt      | 1481          | 35.879,0           |
| r1000.5.col.txt       | 918           | 882,5              |
| c2000.5.col.txt       | 1481          | 35.879,0           |
| c4000.5.col.txt       | memory        |                    |
