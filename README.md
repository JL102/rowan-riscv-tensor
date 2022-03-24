# rowan-riscv-tensor
A RISC-V based matrix multiplication accelerator, built using Chipyard.

To begin working on this repo, go to your chipyard install folder, and navigate to 'generators'. Clone the repository to this directory. Next navigate back up to the chipyard directory and edit 'build.sbt'. Copy the lines inside 'notes.txt' from this repository into 'build.sbt'. Finally find 'lazy val chipyard = (project in file("generators/chipyard))' and add the folder for this project to the list. For example, by default the repositry is cloned to a folder called 'rowan-riscv-tensor', so you would add that to the list.

Now, navigate to 'chipyard/generators/chipyard/src/main/scala/config'

ToDo
- [ ] Create a working decoder (Demux)
  - [ ] Create Test bench for preposed decoder
- [ ] Model data flow an hierarchy
- [ ] Stack and connect design according to above


Optimizations and Improvements
- [ ] Support loading two 32 bit numbers from memory or one 64 bit number
  - [ ] Support loading two 32 bit numbers
  - [ ] support loading one 64 bit number
- [ ] When working with 32 bit numbers, matrix B should load 64 bits of information, and store the back half of the data (The unused half) in a buffer so it can be retrieved later when it is needed. That way it doesn't have be loaded twice.

