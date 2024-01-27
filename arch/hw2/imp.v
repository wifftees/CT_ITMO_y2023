module not_gate(a, out);
  input a;
  output out;

  supply1 pwr;
  supply0 gnd;

  pmos pmos1(out, pwr, a);
  nmos nmos1(out, gnd, a);
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

module implication_mos(a, b, out);
  input a, b;
  output out;

  wire not_a;
  not_gate not1(a, not_a);
  
  or_gate or1(not_a, b, out);
endmodule