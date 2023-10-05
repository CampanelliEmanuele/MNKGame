# MNKGame

Project of the course of algorithms of data structures.

## Compilation

1. Clone the repository

2. Command line compilation:
    ```
    cd MNKGame/mnkgame
    javac -cp ".." *.java
    cd ../subroutine/
    javac -cp ".." *.java
    ```
## Execution

Go to the path: `MNKGame/mnkgame/`

### Legend
  - `M` is the number of the rows;
  - `N` is the number of the columns;
  - `K` is the number of the symbols to juxtapose;
  - `P` can be one of `mnkgame.RandomPlayer`, `mnkgame.QuasiRandomPlayer` or `subroutine.GroupPlayer`.
### Game modes
- Human vs Computer (where `P` is the bot player):

  `java -cp ".." mnkgame.MNKGame M N K P`

- Computer vs Computer:

  `java -cp ".." mnkgame.MNKGame M N K P P`

### Other
#### MNKPlayerTester application:
  - Output score only:

    `java -cp ".." mnkgame.MNKPlayerTester M N K P P`
    
  - Verbose output:
      
    `java -cp ".." mnkgame.MNKPlayerTester M N K P P -v`
    
  - Verbose output and customized timeout (1 sec) and number of game repetitions (10 rounds):
    
    `java -cp ".." mnkgame.MNKPlayerTester M N K P P -v -t 1 -r 10`
    
  - Note that you can customize the use of:
    
    `-v`, `-t`, `number`, `-r`, `number`
