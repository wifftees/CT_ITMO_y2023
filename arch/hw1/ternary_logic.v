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

module ternary_min(a, b, out);
  input [1:0] a;
  input [1:0] b;
  output [1:0] out;

  wire check_kill1;
  wire check_kill2;
  or_gate kill_detector1(a[0], a[1], check_kill1);
  or_gate kill_detector2(b[0], b[1], check_kill2);

  wire check_generate;
  or_gate generate_detector(a[0], b[0], check_generate);


  wire inverted_check_generate;
  not_gate inversion(check_generate, inverted_check_generate);

  wire temp1;
  wire temp2;
  and_gate kill_validation_check_generate1(check_generate, check_kill1, temp1);
  and_gate kill_validation_check_generate2(temp1, check_kill2, out[0]);

  and_gate kill_validation_inverted_check_generate1(
    inverted_check_generate, check_kill1, temp2
  );
  and_gate kill_validation_inverted_check_generate2(
    temp2, check_kill2, out[1]
  );
endmodule

module ternary_max(a, b, out);
  input [1:0] a;
  input [1:0] b;
  output [1:0] out;
  wire check_propagate;
  wire check_generate;
  wire check_kill;
  wire inverted_check_propagate;

  or_gate propagate_detector(a[1], b[1], check_propagate);
  or_gate generate_detector(a[0], b[0], check_generate);
  or_gate kill_detector(check_generate, check_propagate, check_kill);

  not_gate inversion(check_propagate, inverted_check_propagate);

  and_gate kill_validation0(inverted_check_propagate, check_kill, out[0]);
  and_gate kill_validation1(check_propagate, check_kill, out[1]);
endmodule

module ternary_any(a, b, out);
  input [1:0] a;
  input [1:0] b;
  output [1:0] out;
  wire check_generate;
  wire inverted_check_generate;
  wire check_first_bit;
  wire check_second_bit;
  wire inverted_check_second_bit;
  wire check_kill_prop_control;
  wire check_return_yourself_control;
  wire second_bit;
  wire first_bit;
  wire validated_first_bit;
  wire no_generate_validation;

  or_gate generate_detector(a[0], b[0], check_generate);
  not_gate check_generate_inversion(check_generate, check_generate_inversion);
  not_xor_gate xor_first_bit(a[0], b[0], check_first_bit);
  xor_gate xor_second_bit(a[1], b[1], check_second_bit);
  not_gate check_second_bit_inversion(check_second_bit, inverted_check_second_bit);
  and_gate kill_prop_control(check_first_bit, inverted_check_second_bit, check_kill_prop_control);
  or_gate return_yourself_control(
    check_generate, check_kill_prop_control, check_return_yourself_control
  );


  or_gate return_second_bit(a[1], b[1], second_bit);
  and_gate generate_validator(second_bit, check_return_yourself_control, out[1]);
  and_gate return_first_bit(a[0], b[0], first_bit);
  and_gate validate_first_bit(first_bit, check_return_yourself_control, validated_first_bit);

  // in a case when k*p or p*k
  and_gate and_gate1(check_generate_inversion, check_second_bit, no_generate_validation);
  or_gate or_gate1(validated_first_bit, no_generate_validation, out[0]);
endmodule

module ternary_consensus(a, b, out);
  input [1:0] a;
  input [1:0] b;
  output [1:0] out;

  wire check_generate;
  wire check_diff;
  wire inverted_check_diff;
  wire check_generate_control;
  wire inverted_check_generate_control;

  or_gate generate_detector(a[0], b[0], check_generate);
  xor_gate diff_detector(a[1], b[1], check_diff);
  not_gate diff_inversion(check_diff, inverted_check_diff);
  or_gate generate_control(check_diff, check_generate, check_generate_control);
  not_gate check_generate_control_inversion(check_generate_control, inverted_check_generate_control);

  and_gate and_gate1(a[1], inverted_check_generate_control, out[1]);
  or_gate or_gate1(a[0], check_generate_control, out[0]);
endmodule