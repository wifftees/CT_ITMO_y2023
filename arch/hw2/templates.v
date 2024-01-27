module not_gate(a, out);
  input a;
  output out;

  supply1 pwr;
  supply0 gnd;

  pmos pmos1(out, pwr, a);
  nmos nmos1(out, gnd, a);
endmodule

module and_gate(a, b, out);
  input a;
  input b;
  output out;
  wire temp;

  supply1 pwr;
  supply0 gnd;

  nmos nmos1(temp, pwr, a);
  nmos nmos2(out, temp, b);

  pmos pmos1(out, gnd, a);
  pmos pmos2(out, gnd, b);
endmodule

module or_gate(a, b, out);
  input a;
  input b;
  output out;
  wire temp;
  
  supply1 pwr;
  supply0 gnd;

  nmos nmos1(out, pwr, a);
  nmos nmos2(out, pwr, b);

  pmos pmos1(temp, gnd, a);
  pmos pmos2(out, temp, b);
endmodule

module xor_gate(a, b, out);
  input a;
  input b;
  output out;

  wire inverted_a;
  wire inverted_b;
  wire temp1;
  wire temp2;

  not_gate inversion_a(a, inverted_a);
  not_gate inversion_b(b, inverted_b);

  and_gate and_gate1(a, inverted_b, temp1);
  and_gate and_gate2(b, inverted_a, temp2);

  or_gate or_gate1(temp1, temp2, out);
endmodule

module not_xor_gate(a, b, out);
  input a;
  input b;
  output out;
  wire output_xor;
  
  xor_gate xor_gate1(a, b, output_xor);
  not_gate not_gate1(output_xor, out);
endmodule

module median3_gate(a, b, c, out);
  input a;
  input b; 
  input c;
  output out;
  wire ab;
  wire bc; 
  wire ac;
  and_gate and_gate1(a, b, ab);
  and_gate and_gate2(b, c, bc);
  and_gate and_gate3(a, c, ac);
  wire temp;
  or_gate or_gate1(ab, bc, temp);
  or_gate or_gate2(temp, ac, out);
endmodule

module and4_gate(a, b, out);
  input [3:0] a;
  input [3:0] b;
  output [3:0] out;

  and_gate and1(a[0], b[0], out[0]);
  and_gate and2(a[1], b[1], out[1]);
  and_gate and3(a[2], b[2], out[2]);
  and_gate and4(a[3], b[3], out[3]);
endmodule

module or4_gate(a, b, out);
  input [3:0] a;
  input [3:0] b;
  output [3:0] out;

  or_gate or1(a[0], b[0], out[0]);
  or_gate or2(a[1], b[1], out[1]);
  or_gate or3(a[2], b[2], out[2]);
  or_gate or4(a[3], b[3], out[3]);
endmodule

module admission_4_by_1(a, pass, out);
  input [3:0] a;
  input pass;
  output [3:0] out;
  and_gate and1(a[0], pass, out[0]);
  and_gate and2(a[1], pass, out[1]);
  and_gate and3(a[2], pass, out[2]);
  and_gate and4(a[3], pass, out[3]);
endmodule

module admission_4_by_2(a, pass1, pass2, out);
  input [3:0] a;
  input pass1;
  input pass2;
  output [3:0] out;
  wire [3:0] temp;
  and_gate and1(a[0], pass1, temp[0]);
  and_gate and2(a[1], pass1, temp[1]);
  and_gate and3(a[2], pass1, temp[2]);
  and_gate and4(a[3], pass1, temp[3]);

  and_gate and5(temp[0], pass2, out[0]);
  and_gate and6(temp[1], pass2, out[1]);
  and_gate and7(temp[2], pass2, out[2]);
  and_gate and8(temp[3], pass2, out[3]);
endmodule

module admission_1_by_2(a, pass1, pass2, out);
  input a;
  input pass1;
  input pass2;
  output out;
  wire temp;
  and_gate and1(a, pass1, temp);
  and_gate and2(temp, pass2, out);
endmodule

module multiplexor_8_by_1(inv_a, a, control, out);
  input [3:0] a;
  input [3:0] inv_a;
  input control;
  output [3:0] out;
  
  wire control_inverse;
  not_gate not1(control, control_inverse);

  wire [3:0] temp1;
  wire [3:0] temp2;
  admission_4_by_1 allow1(a, control_inverse, temp1);
  admission_4_by_1 allow2(inv_a, control, temp2);

  or4_gate or4_gate1(temp1, temp2, out);
endmodule

module multiplexor_4_by_2(a, control, out);
  input [3:0] a;
  input [1:0] control;
  output out;
  
  wire [1:0] control_inverse;
  not_gate not1(control[0], control_inverse[0]);
  not_gate not2(control[1], control_inverse[1]);
  wire [3:0] temp;
  admission_1_by_2 allow1(a[0], control_inverse[0], control_inverse[1], temp[0]);
  admission_1_by_2 allow2(a[1], control[0], control_inverse[1], temp[1]);
  admission_1_by_2 allow3(a[2], control[1], control_inverse[0], temp[2]);
  admission_1_by_2 allow4(a[3], control[0], control[1], temp[3]);
  
  wire [1:0] or_temp;
  or_gate or1(temp[0], temp[1], or_temp[0]);
  or_gate or2(or_temp[0], temp[2], or_temp[1]);
  or_gate or3(or_temp[1], temp[3], out);
endmodule

module multiplexor_16_by_2(a, b, c, d, control, out);
  input [3:0] a;
  input [3:0] b;
  input [3:0] c;
  input [3:0] d;
  input [1:0] control;
  output [3:0] out;

  wire [1:0] inv_control;
  not_gate not1(control[0], inv_control[0]);
  not_gate not2(control[1], inv_control[1]);

  wire [3:0] temp1;
  wire [3:0] temp2;
  wire [3:0] temp3;
  wire [3:0] temp4;
  admission_4_by_2 allow1(d, inv_control[0], inv_control[1], temp1);
  admission_4_by_2 allow2(c, control[0], inv_control[1], temp2);
  admission_4_by_2 allow3(b, inv_control[0], control[1], temp3);
  admission_4_by_2 allow4(a, control[0], control[1], temp4);

  wire [3:0] or_temp1;
  wire [3:0] or_temp2;
  or4_gate or1(temp1, temp2, or_temp1);
  or4_gate or2(temp3, or_temp1, or_temp2);
  or4_gate or3(or_temp2, temp4, out);
endmodule

module bit_shift_4_by_2(a, shamt, out);
  input [3:0] a;
  input [1:0] shamt;
  output [3:0] out;

  supply0 gnd;
  wire[3:0] temp1;
  wire[3:0] temp2;
  wire[3:0] temp3;
  wire[3:0] temp4;
  assign temp1 = {gnd, gnd, gnd, a[3]};
  assign temp2 = {gnd, gnd, a[2], a[3]};
  assign temp3 = {gnd, a[1], a[2], a[3]};
  assign temp4 = a;
  multiplexor_4_by_2 mp1(temp1, shamt, out[3]);
  multiplexor_4_by_2 mp2(temp2, shamt, out[2]);
  multiplexor_4_by_2 mp3(temp3, shamt, out[1]);
  multiplexor_4_by_2 mp4(temp4, shamt, out[0]);
endmodule

module xor3_gate(a, b, c, out);
  input a;
  input b;
  input c;
  output out;
  wire temp;
  xor_gate xor_gate1(a, b, temp);
  xor_gate xor_gate2(temp, c, out);
endmodule

module full_adder(a, b, carry_bit_in, out, carry_bit_out);
  input a;
  input b;
  input carry_bit_in;
  output carry_bit_out;
  output out;
  xor3_gate xor3_gate1(a, b, carry_bit_in, out);
  median3_gate median3_gate1(a, b, carry_bit_in, carry_bit_out);
endmodule

module adder4(a, b, carry_bit_in, out);
  input [3:0] a;
  input [3:0] b;
  input carry_bit_in;
  output [3:0] out;
  wire [4:0] carry_bit;
  

  full_adder full_adder1(a[0], b[0], carry_bit_in, out[0], carry_bit[1]);
  full_adder full_adder2(a[1], b[1], carry_bit[1], out[1], carry_bit[2]);
  full_adder full_adder3(a[2], b[2], carry_bit[2], out[2], carry_bit[3]);
  full_adder full_adder4(a[3], b[3], carry_bit[3], out[3], carry_bit[4]);
endmodule

module adder4_overflow_last(a, b, carry_bit_in, overflow, last);
  input [3:0] a;
  input [3:0] b;
  input carry_bit_in;
  output overflow;
  output last;
  wire [4:0] carry_bit;
  wire [3:0] out;
  

  full_adder full_adder1(a[0], b[0], carry_bit_in, out[0], carry_bit[1]);
  full_adder full_adder2(a[1], b[1], carry_bit[1], out[1], carry_bit[2]);
  full_adder full_adder3(a[2], b[2], carry_bit[2], out[2], carry_bit[3]);
  full_adder full_adder4(a[3], b[3], carry_bit[3], out[3], carry_bit[4]);
  assign overflow = {carry_bit[4]};
  assign last = {out[3]};
endmodule

module invertor4_plus(a, out);
  input [3:0] a;
  output [3:0] out;

  supply0 gnd;
  reg [3:0] one = 1;
  wire [3:0] inverse_number;
  not_gate inv1(a[0], inverse_number[0]);
  not_gate inv2(a[1], inverse_number[1]);
  not_gate inv3(a[2], inverse_number[2]);
  not_gate inv4(a[3], inverse_number[3]);
  adder4 adder4(inverse_number, one, gnd, out);
endmodule

module invertor4(a, out);
  input [3:0] a;
  output [3:0] out;

  not_gate inv1(a[0], out[0]);
  not_gate inv2(a[1], out[1]);
  not_gate inv3(a[2], out[2]);
  not_gate inv4(a[3], out[3]);
endmodule

module decide_bit(a, b, last, out);
  input a;
  input b;
  input last;
  output out;
  wire xor_result;
  xor_gate xor1(a, b, xor_result);
  wire inv_xor_result;
  wire temp1;
  wire temp2;
  and_gate and1(a, xor_result, temp1);
  not_gate not1(xor_result, inv_xor_result);
  and_gate and2(last, inv_xor_result, temp2);
  or_gate or1(temp1, temp2, out);
endmodule

module decoder(control, out);
  input [1:0] control;
  output [3:0] out;
  
  supply1 pwr;
  supply0 gnd;

  wire [1:0] inverse_control;
  not_gate not1(control[0], inverse_control[0]);
  not_gate not2(control[1], inverse_control[1]);
  
  and_gate and1(inverse_control[0], inverse_control[1], out[0]);
  and_gate and2(control[0], inverse_control[1], out[1]);
  and_gate and3(control[1], inverse_control[0], out[2]);
  and_gate and4(control[0], control[1], out[3]);
endmodule

module alu(a, b, control, res);
  input [3:0] a, b; // Операнды
  input [2:0] control; // Управляющие сигналы для выбора операции

  output [3:0] res; // Результат

  wire [3:0] inv_b;
  invertor4_plus inv(b, inv_b);
  
  wire [3:0] next_b;
  multiplexor_8_by_1 mp1(inv_b, b, control[0], next_b);

  wire [3:0] and_ab;
  wire [3:0] or_ab;
  and4_gate and1(a, b, and_ab);
  or4_gate or1(a, b, or_ab);


  wire [3:0] next_and_ab;
  wire [3:0] next_or_ab;
  wire [3:0] inv_and_ab;
  wire [3:0] inv_or_ab;
  invertor4 inv1(and_ab, inv_and_ab);
  invertor4 inv2(or_ab, inv_or_ab);

  multiplexor_8_by_1 mp2(inv_and_ab, and_ab, control[0], next_and_ab);
  multiplexor_8_by_1 mp3(inv_or_ab, or_ab, control[0], next_or_ab);
  
  wire [3:0] sum_ab;
  supply0 gnd;
  adder4 add1(a, next_b, gnd, sum_ab);

  wire overflow;
  wire last;
  adder4_overflow_last add2(a, inv_b, gnd, overflow, last);
  wire temp;
  decide_bit db(a[3], b[3], last, temp);
  wire [3:0] slt;
  assign slt = {gnd, gnd, gnd, temp};
  wire [1:0] ctrl12;
  assign ctrl12 = {control[2], control[1]};
  multiplexor_16_by_2 mp16(slt, sum_ab, next_or_ab, next_and_ab, ctrl12, res);
endmodule

module d_latch(clk, d, we, q);
  input clk; // Сигнал синхронизации
  input d; // Бит для записи в ячейку
  input we; // Необходимо ли перезаписать содержимое ячейки

  output reg q; // Сама ячейка
  // Изначально в ячейке хранится 0
  initial begin
    q <= 0;
  end
  // Значение изменяется на переданное на спаде сигнала синхронизации
  always @ (negedge clk) begin
    // Запись происходит при we = 1
    if (we) begin
      q <= d;
    end
  end
endmodule

module element(clk, we_data, rd_data, sync);
  input clk;
  input sync;
  input [3:0] we_data;
  output [3:0] rd_data;

  // Запись происходит при we = 1
  d_latch dl0(clk, we_data[0], sync, rd_data[0]);
  d_latch dl1(clk, we_data[1], sync, rd_data[1]);
  d_latch dl2(clk, we_data[2], sync, rd_data[2]);
  d_latch dl3(clk, we_data[3], sync, rd_data[3]);
endmodule

module register_file(clk, rd_addr, we_addr, we_data, rd_data, we);
  input clk; // Сигнал синхронизации
  input [1:0] rd_addr, we_addr; // Номера регистров для чтения и записи
  input [3:0] we_data; // Данные для записи в регистровый файл
  input we; // Необходимо ли перезаписать содержимое регистра

  output [3:0] rd_data; // Данные, полученные в результате чтения из регистрового файла
  wire [3:0] cell_wr_addr;

  decoder dc1(we_addr, cell_wr_addr);

  wire [0:3] temp_rd_data_0;
  wire [0:3] temp_rd_data_1;
  wire [0:3] temp_rd_data_2;
  wire [0:3] temp_rd_data_3;

  wire [3:0] new_we;
  and_gate a0(we, cell_wr_addr[0], new_we[0]);
  and_gate a1(we, cell_wr_addr[1], new_we[1]);
  and_gate a2(we, cell_wr_addr[2], new_we[2]);
  and_gate a3(we, cell_wr_addr[3], new_we[3]);

  element cell0(clk, we_data, temp_rd_data_0, new_we[3]);
  element cell1(clk, we_data, temp_rd_data_1, new_we[2]);
  element cell2(clk, we_data, temp_rd_data_2, new_we[1]);
  element cell3(clk, we_data, temp_rd_data_3, new_we[0]);

  multiplexor_16_by_2 mp(
    temp_rd_data_0,
    temp_rd_data_1,
    temp_rd_data_2, 
    temp_rd_data_3,
    rd_addr,
    rd_data
  );
endmodule


module counter(clk, addr, control, immediate, data);
  input clk; // Сигнал синхронизации
  input [1:0] addr; // Номер значения счетчика которое читается или изменяется
  input [3:0] immediate; // Целочисленная константа, на которую увеличивается/уменьшается значение счетчика
  input control; // 0 - операция инкремента, 1 - операция декремента

  output [3:0] data; // Данные из значения под номером addr, подающиеся на выход
  // TODO: implementation
  wire [3:0] read_data;
  wire [3:0] write_data; 
  supply1 pwr;
  supply0 gnd;
  wire we;
  assign we = pwr;
  register_file rf(clk, addr, addr, write_data, read_data, we);
  wire [3:0] decide;
  assign decide = {pwr, gnd, control};
  wire [3:0] alu_out;
  alu alu1(read_data, immediate, decide, write_data);
  assign data = read_data;
endmodule