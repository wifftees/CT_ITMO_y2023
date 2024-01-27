// модуль, который реализует расширенение
// 16-битной знаковой константы до 32-битной
module sign_extend(in, out);
  input [15:0] in;
  output [31:0] out;

  assign out = {{16{in[15]}}, in};
endmodule

// модуль, который реализует побитовый сдвиг числа
// влево на 2 бита
module shl_2(in, out);
  input [31:0] in;
  output [31:0] out;

  assign out = {in[29:0], 2'b00};
endmodule

// 32 битный сумматор
module adder(a, b, out);
  input [31:0] a, b;
  output [31:0] out;

  assign out = a + b;
endmodule

// 32-битный мультиплексор
module mux2_32(d0, d1, a, out);
  input [31:0] d0, d1;
  input a;
  output [31:0] out;
  assign out = a ? d1 : d0;
endmodule

module mux2_1(d0, d1, a, out);
  input d0, d1;
  input a;
  output out;
  assign out = a ? d1 : d0;
endmodule

// 5 - битный мультиплексор
module mux2_5(d0, d1, a, out);
  input [4:0] d0, d1;
  input a;
  output [4:0] out;
  assign out = a ? d1 : d0;
endmodule

module mux4_32(d0, d1, d2, d3, key, out);
  input [31:0] d0, d1, d2, d3;
  input [1:0] key;
  output reg [31:0] out;

  always @(key or d0 or d1 or d2 or d3) begin
    if (key == 0)
      assign out = d0;
    else if (key == 1)
      assign out = d1;
    else if (key == 2) 
      assign out = d2;
    else if (key == 3)
      assign out = d3;
  end
endmodule

module mux4_5(d0, d1, d2, d3, key, out);
  input [4:0] d0, d1, d2, d3;
  input [1:0] key;
  output reg [4:0] out;

  always @(key or d0 or d1 or d2 or d3) begin
    if (key == 0)
      assign out = d0;
    else if (key == 1)
      assign out = d1;
    else if (key == 2) 
      assign out = d2;
    else if (key == 3)
      assign out = d3;
  end
endmodule


module control_unit (
  opcode, funct, 
  MemToReg,
  MemWrite,
  Branch,
  ALUSrc,
  RegDst,
  RegWrite, 
  Jump,
  JumpLink,
  JumpReg,
  BranchNot,
  ALUControl
);
  input [5:0] opcode, funct;
  output MemToReg, MemWrite, Branch, ALUSrc, RegDst, RegWrite, Jump, JumpLink, BranchNot, JumpReg;
  output [2:0] ALUControl;
  wire [1:0] ALUOp;

  main_decoder md(opcode, MemToReg, MemWrite, Branch, ALUSrc, RegDst, RegWrite, Jump, JumpLink, BranchNot, ALUOp);
  ALU_decoder alud(funct, ALUOp, ALUControl, JumpReg);

endmodule

module main_decoder (
  opcode, 
  MemToReg,
  MemWrite,
  Branch,
  ALUSrc,
  RegDst,
  RegWrite,
  Jump,
  JumpLink,
  BranchNot,
  ALUOp
);
  input [5:0] opcode;
  output reg MemToReg, MemWrite, Branch, ALUSrc, RegDst, RegWrite, Jump, JumpLink, BranchNot;
  output reg [1:0] ALUOp;

  always @(opcode) begin
    case (opcode)
      // bne
      6'b000101: begin
        RegWrite <= 0;
        RegDst <= 0;
        ALUSrc <= 0;
        Branch <= 1;
        BranchNot <= 1;
        MemWrite <= 0;
        MemToReg <= 0;
        ALUOp <= 0;
        Jump <= 0;
        JumpLink <= 0;
      end
      // add
      6'b000000: begin
        RegWrite <= 1;
        RegDst <= 1;
        ALUSrc <= 0;
        Branch <= 0;
        BranchNot <= 0;
        MemWrite <= 0;
        MemToReg <= 0;
        ALUOp <= 2;
        Jump <= 0;
        JumpLink <= 0;
      end
      // addi
      6'b001000: begin
        RegWrite <= 1;
        RegDst <= 0;
        ALUSrc <= 1;
        Branch <= 0;
        BranchNot <= 0;
        MemWrite <= 0;
        MemToReg <= 0;
        ALUOp <= 0;
        Jump <= 0;
        JumpLink <= 0;
      end
      // andi
      6'b001100: begin
        RegWrite <= 1;
        RegDst <= 0;
        ALUSrc <= 1;
        Branch <= 0;
        BranchNot <= 0;
        MemWrite <= 0;
        MemToReg <= 0;
        ALUOp <= 3;
        Jump <= 0;
        JumpLink <= 0;
      end
      // lw
      6'b100011: begin
        RegWrite <= 1;
        RegDst <= 0;
        ALUSrc <= 1;
        Branch <= 0;
        BranchNot <= 0;
        MemWrite <= 0;
        MemToReg <= 1;
        ALUOp <= 0;
        Jump <= 0;
        JumpLink <= 0;
      end
      // sw
      6'b101011: begin
        RegWrite <= 0;
        RegDst <= 0;
        ALUSrc <= 1;
        Branch <= 0;
        BranchNot <= 0;
        MemWrite <= 1;
        ALUOp <= 0;
        Jump <= 0;
        JumpLink <= 0;
      end
      // beq
      6'b000100: begin
        RegWrite <= 0;
        RegDst <= 0;
        ALUSrc <= 0;
        Branch <= 1;
        BranchNot <= 0;
        MemWrite <= 0;
        ALUOp <= 1;
        Jump <= 0;
        JumpLink <= 0;
      end 
      // j
      6'b000010: begin
        RegWrite <= 0;
        RegDst <= 0;
        ALUSrc <= 0;
        Branch <= 0;
        BranchNot <= 0;
        MemWrite <= 0;
        ALUOp <= 0;
        Jump <= 1;
        JumpLink <= 0;
      end
      // jal
      6'b000011: begin
        RegWrite <= 1;
        ALUSrc <= 0;
        Branch <= 0;
        BranchNot <= 0;
        MemWrite <= 0;
        ALUOp <= 0;
        Jump <= 1;
        JumpLink <= 1;
      end
    endcase
  end
endmodule

module ALU_decoder (
  funct,
  ALUOp,
  ALUControl,
  JumpReg
);
  input [5:0] funct;
  input [1:0] ALUOp;
  output reg JumpReg;
  output reg [2:0] ALUControl;

  always @(funct or ALUOp) begin
    JumpReg <= 0;
    case (ALUOp) 
      2'b00: ALUControl <= 2;
      2'b01: ALUControl <= 6;
      2'b11: ALUControl <= 6;
      2'b10: begin
        case (funct)
          6'b100000: ALUControl <= 2;
          6'b100010: ALUControl <= 6;
          6'b100100: ALUControl <= 0;
          6'b100101: ALUControl <= 1;
          6'b101010: ALUControl <= 7;
          6'b001000: begin
            JumpReg <= 1;
            ALUControl <= 0;
          end
        endcase
      end
    endcase
  end
endmodule

module alu(key, a, b, out, zero);
  input [2:0] key;
  input [31:0] a, b;
  output reg zero;
  output reg [31:0] out;
  always @(key or a or b) begin
    zero <= 0;
    if (a == b) zero <= 1;

    case(key)
      3'b000: out = a & b;
      3'b001: out = a | b;
      3'b010: out = a + b;
      3'b011: out = a - b;
      3'b100: out = a & (~b);
      3'b101: out = a | (~b);
      3'b110: out = a - b;
      3'b111: out = (a < b) ? 1 : 0; 
    endcase
  end
endmodule