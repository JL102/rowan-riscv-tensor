# rowan-riscv-tensor
A RISC-V based matrix multiplication accelerator, built using Chipyard.


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

