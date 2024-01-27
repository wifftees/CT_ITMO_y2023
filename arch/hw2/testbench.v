`include "./imp.v"

module testbench();
  reg a = 0;
  reg b = 1;
  wire any_result;

  implication_mos imp(a, b, any_result);
   
  initial begin
    #5 $display("a = %b, b = %b, any = %b", a, b, any_result);
  end
endmodule