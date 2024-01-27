`include "./ternary_logic.v"

module testbench();
  reg [1:0] a = 0;
  reg [1:0] b = 2;
  wire [1:0] any_result;

  ternary_any ternary_any1(a, b, any_result);
  
   
  initial begin
    #5 $display("a = %b, b = %b, any = %b", a, b, any_result);
  end
endmodule