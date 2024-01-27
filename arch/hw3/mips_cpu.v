`include "util.v"



module mips_cpu(
  clk, 
  pc, 
  pc_new, 
  instruction_memory_a, 
  instruction_memory_rd, 
  data_memory_a, 
  data_memory_rd, 
  data_memory_we, 
  data_memory_wd,
  register_a1, register_a2, register_a3, 
  register_we3, 
  register_wd3, 
  register_rd1, register_rd2
);
  // сигнал синхронизации
  input clk;
  // текущее значение регистра PC
  inout [31:0] pc;
  // новое значение регистра PC (адрес следующей команды)
  output [31:0] pc_new;
  // we для памяти данных
  output data_memory_we;
  // адреса памяти и данные для записи памяти данных
  output [31:0] instruction_memory_a, data_memory_a, data_memory_wd;
  // данные, полученные в результате чтения из памяти
  inout [31:0] instruction_memory_rd, data_memory_rd;
  // we3 для регистрового файла
  output register_we3;
  // номера регистров
  output [4:0] register_a1, register_a2, register_a3;
  // данные для записи в регистровый файл
  output [31:0] register_wd3;
  // данные, полученные в результате чтения из регистрового файла
  inout [31:0] register_rd1, register_rd2;
  wire [31:0] step = 4;

  wire [31:0] instruction;
  assign instruction_memory_a = pc;
  assign instruction = instruction_memory_rd;
  // setting opcode and funct
  wire [5:0] opcode, funct;
  assign opcode = instruction[26 +: 6];
  assign funct = instruction[0 +: 6];

  wire [4:0] rs;
  assign rs = 5'b11111;

  // setting control unit
  wire MemToReg, MemWrite, Branch, ALUSrc, RegDst, RegWrite, Jump, JumpLink, BranchNot, JumpReg;
  wire [2:0] ALUControl;
  control_unit cu(opcode, funct, MemToReg, MemWrite, Branch, ALUSrc, RegDst, RegWrite, Jump, JumpLink, JumpReg, BranchNot, ALUControl);

  // setting registers read registers 
  assign register_a1 = instruction[21 +: 5];
  assign register_a2 = instruction[16 +: 5];

  wire [1:0] ch1 = {JumpLink, RegDst};

  // allowence for register file
  assign register_we3 = RegWrite;

  // setting write register
  mux4_5 write_mux(instruction[16 +: 5], instruction[11 +: 5], rs, rs, ch1, register_a3);

  // extending constant
  wire [15:0] constant;
  assign constant = instruction[0 +: 16];
  wire [31:0] extended_constant;
  sign_extend ext(constant, extended_constant);


  // making one step
  wire [31:0] pc_plus_4;
  adder add_step(step, pc, pc_plus_4);

  // making constant amount of steps
  wire [31:0] constant_multiplied;
  shl_2 shift2(extended_constant, constant_multiplied);
  wire [31:0] pc_branch;
  adder add_constant_steps(constant_multiplied, pc_plus_4, pc_branch);

  // choosing between register and constant
  wire [31:0] src_b;
  mux2_32 mux_src_b(register_rd2, extended_constant, ALUSrc, src_b);

  // making alu calculations
  wire [31:0] alu_result;
  wire zero;
  wire inv_zero = ~zero;
  alu alu1(ALUControl, register_rd1, src_b, alu_result, zero);

  wire zero_src;
  mux2_1 mux_to_zero(zero, inv_zero, BranchNot, zero_src);
  // deciding about branch statment
  wire branch_src;
  assign branch_src = zero_src & Branch;

  // creating jump concat 
  wire [1:0] pc_src;
  wire JumpM = Jump | JumpReg;
  wire branch_srcM = branch_src | JumpReg;
  assign pc_src = {JumpM, branch_srcM};

  // getting jump constant
  wire [25:0] jump_constant;
  assign jump_constant = instruction[0 +: 26];

  wire [31:0] new_jump_constant = {pc_plus_4[31:28], jump_constant[25:0], 2'b00};

  // setting pc_new 
  mux4_32 mux_pc_new(pc_plus_4, pc_branch, new_jump_constant, register_rd1, pc_src, pc_new);

  // setting up data memory module
  assign data_memory_a = alu_result;
  assign data_memory_wd = register_rd2;
  assign data_memory_we = MemWrite;

  // choosing between register or memory
  wire [31:0] write_data;
  wire [1:0] ch2 = {JumpLink, MemToReg};
  mux4_32 mux_reg_mem(alu_result, data_memory_rd, pc_plus_4, pc_plus_4, ch2, write_data);

  // assigning register file
  assign register_wd3 = write_data;
endmodule
