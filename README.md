# Burst Plot Assistant

The Plot Assistant is a desktop application (JavaFx) that helps you to calculate 
your starting nonces when plotting burst coin. The assistant executes commands
in a specific order (see "Life Cycle"). In each execution you can specify a command.
Using [Apache Velocity](https://github.com/apache/velocity-engine) the assistant provides
important values to the commands that will be parsed on execution (see "Templates").

## Life Cycle
* Process Start
  * Cycle Start (looping n times)
    * Pre Plotter
    * Plotter
    * Post Plotter
    * Pre Optimizer (only if optimized = false)
    * Optimizer (^)
    * Post Optimizer (^)
  * Cycle End
* Process End

## Templates
Values:
* process
  * numericalAccountId
  * startNonce
  * nonces
  * staggerSize
* cycle
  * currentStartingNonce
  * cycleIndex
  
#### Example: 
C:\Burst\plotter.exe "D:\Plots\\\\${process.numericalAccountId}\_${cycle.currentStartingNonce}\_${process.nonces}\_${process.staggerSize}"

Note: Due to the way Apache Velocity works, you must escape backslashes behind $-symbols: \\\\${whatever}

## Build
Just "mvn package" and there should be a cute executable jar-with-dependencies in your target directory
