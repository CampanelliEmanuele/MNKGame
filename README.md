# MNKGame

Project of the course of algorithms of data structures.

- Clone the project
- Command-line compile:
    cd mnkgame
    javac -cp ".." *.java
    cd ..
    cd subroutine
    javac -cp ".." *.java
    
- U can run: In the mnkgame/ folder:
  - Legend:
    - M,N,K are natural numbers
    - P = P1 or P2 or P3
    - P1 = mnkgame.RandomPlayer
    - P2 = mnkgame.QuasiRandomPlayer
    - P3 = subroutine.GroupPlayer
  - Human vs Computer:
      java -cp ".." mnkgame.MNKGame M N K *P*
  - Computer vs Computer:
      java -cp ".." mnkgame.MNKGame M N K *P* *P*
  - MNKPlayerTester application:
    - Output score only:
      java -cp ".." mnkgame.MNKPlayerTester M N K *P* *P*
    - Verbose output:
      java -cp ".." mnkgame.MNKPlayerTester M N K *P* *P* -v
    - Verbose output and customized timeout (1 sec) and number of game repetitions (10 rounds):
      java -cp ".." mnkgame.MNKPlayerTester M N K *P* *P* -v -t 1 -r 10

      P.S: U can customize the use of -v, -t *number*, -r *number*
