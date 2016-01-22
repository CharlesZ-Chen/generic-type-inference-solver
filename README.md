Dataflow type system README
===========================

Requirements
------------

In order to use the dataflow type system, you first need to set up the
following four projects:

- https://bitbucket.org/typetools/jsr308-langtools
- https://github.com/typetools/annotation-tools
- https://github.com/typetools/checker-framework
- https://github.com/typetools/checker-framework-inference

You'll need environment variables `CHECKERFRAMEWORK`, `JSR308`,
`JAVA_HOME`, and `AFU` set up appropriately.
The `insert-annotations-to-source` script from AFU must be on your path.

The tool `do-like-javac` makes usage easier:

- https://github.com/SRI-CSL/pascali-public.git                                        

I created a shortcut for this tool:

```
alias do-like-javac='python /the/path/to/do-like-javac/bin/do-like-javac.py'
```

The tool `graphviz` could visualize the dot files that are generated by checker-framework.

- http://www.graphviz.org


Building
--------

1. Clone this repository into the `$JSR308` directory.

  ```
  cd $JSR308
  git clone https://github.com/Jianchu/generic-type-inference-solver.git
  ```

2. In the `$JSR308` directory, run
`LogiqlSolver/dataflowSetup.sh`. This bash script will set everything
up, recompile `checker-framework-inference`, and put the
`DataflowExample` directory into the `$JSR308` directory.

  ```
  bash LogiqlSolver/dataflowSetup.sh
  ```

3. Now you can use `checker-framework-inference` to invoke the
Dataflow Checker and Dataflow Solver like this:

  ```
  $JSR308/checker-framework-inference/scripts/inference
    --checker dataflow.DataflowChecker
    --solver dataflow.solver.DataflowSolver --mode INFER [List of files]
  ```


Running Example
---------------

After the second step of Building, a `dataflowexample` directory will be
placed under `$JSR308`.  This is a sample project that is annotated with
@Dataflow annotations, and you can play around with it: type check,
type infer, insert the inferred annotations to source code, visualize the control flow graph, etc.

Here are some instructions that shows how to do these tasks with
`do-like-javac`:

1. Change into the dataflowexample directory:

  ```
  cd $JSR308/dataflowexample
  ```

2. Invoke the inference tool using `do-like-javac`.
The ROUNDTRIP mode will generate and solve the constraints 
and then inserts the results back into the original source code. 
If the whole process runs successfully, the inserted output will be placed in `annotated` directory.

  ```
  do-like-javac -t inference --checker dataflow.DataflowChecker
    --solver dataflow.solver.DataflowSolver -o logs 
    -m ROUNDTRIP -afud annotated -- ant compile-project
  ```

3. Invoke the checker tool with `do-like-javac`.
This step will type check the newly created source code, and generate
`.dot` files (in the `dotfiles` directory) that visualize the
control flow graph.

  ```
  do-like-javac -t checker --checker "dataflow.DataflowChecker -Aflowdotdir=./dotfiles" -o logs -- ant check-annotated-src
  ```
  Note the quotes around the `--checker` argument to ensure the
whole string is used.


4. Visualize the dot files by tool `graphviz`. This step will generate a pdf file that contains the control flow graph.

  ```
  dot -Tpdf dotfiles/_init_Dataflow.dot -o CFG.pdf
  ```

If you compare the original source code with the source code generated
by the third step, you can find the string field
`thisIsString` and `thisShouldbeString` are annotated with
`@DataFlow(typeNames={"java.lang.String"})` in the new source code, although the declared type of `thisShouldbeString` is `Object`.
